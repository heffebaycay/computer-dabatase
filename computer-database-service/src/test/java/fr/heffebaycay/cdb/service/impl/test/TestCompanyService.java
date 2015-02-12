package fr.heffebaycay.cdb.service.impl.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import fr.heffebaycay.cdb.dao.ICompanyDao;
import fr.heffebaycay.cdb.dao.IComputerDao;
import fr.heffebaycay.cdb.dao.exception.DaoException;
import fr.heffebaycay.cdb.model.Company;
import fr.heffebaycay.cdb.model.CompanyPageRequest;
import fr.heffebaycay.cdb.service.impl.CompanyServiceJDBCImpl;
import fr.heffebaycay.cdb.util.CompanySortCriteria;
import fr.heffebaycay.cdb.util.SortOrder;
import fr.heffebaycay.cdb.wrapper.SearchWrapper;

public class TestCompanyService {

  private static final Long NUMBER_OF_RESULTS = 10L;

  ICompanyDao               companyDao;

  CompanyServiceJDBCImpl    companyService;

  List<Company>             companiesDB;

  @Before
  public void setUp() {

    IComputerDao computerDao = mock(IComputerDao.class);

    companyDao = mock(ICompanyDao.class);

    companyService = new CompanyServiceJDBCImpl(computerDao, companyDao);

    Company c1 = new Company.Builder().id(1).name("Apple").build();

    Company c2 = new Company.Builder().id(2).name("HP").build();

    Company c3 = new Company.Builder().id(3).name("IBM").build();


    companiesDB = new ArrayList<Company>();
    companiesDB.add(c1);
    companiesDB.add(c2);
    companiesDB.add(c3);

  }

  @Test
  public void testFindAll() {

    if (companyService == null) {
      fail("Company Service isn't initialized");
    }

    when(companyDao.findAll()).thenReturn(companiesDB);

    List<Company> companies = companyService.findAll();

    assertEquals(companiesDB, companies);

  }

  @Test
  public void testFindById() {

    if (companyService == null) {
      fail("Company Service isn't initialized");
    }

    doAnswer(new Answer<Company>() {

      @Override
      public Company answer(InvocationOnMock invocation) {
        long idToFind = invocation.getArgumentAt(0, Long.class);

        return companiesDB.stream().filter(c -> c.getId() == idToFind).findFirst().get();
      }

    }).when(companyDao).findById(Matchers.anyLong());

    Random rnd = new Random();
    int randomIndex = rnd.nextInt(companiesDB.size());

    Company rndCompany = companiesDB.get(randomIndex);

    Company company = companyService.findById(rndCompany.getId());

    assertEquals(rndCompany, company);

  }

  @Test
  public void testCreate() {

    if (companyService == null) {
      fail("CompanyService isn't initialized");
    }

      doAnswer(new Answer<Long>() {

        @Override
        public Long answer(InvocationOnMock invocation) {

          Company companyToCreate = invocation.getArgumentAt(0, Company.class);

          companiesDB.add(companyToCreate);

          return companyToCreate.getId();
        }

      }).when(companyDao).create(Matchers.any(Company.class));

    Company c4 = new Company.Builder().id(4).name("Compaq").build();

    companyService.create(c4);

    Company foundCompany = companiesDB.stream().filter(c -> c.getId() == c4.getId()).findFirst()
        .get();

    assertEquals(c4, foundCompany);

  }

  @Test
  public void testFindAllWithOffset() {

    if (companyService == null) {
      fail("CompanyService isn't initialized");
    }

    SearchWrapper<Company> wrapper = new SearchWrapper<Company>();

    try {
      when(
          companyDao.findAll(Matchers.any(CompanyPageRequest.class))).thenReturn(wrapper);
    } catch (DaoException e) {
      fail("testFindAllWithOffset(): Failed to setup Mockito: " + e.getMessage());
    }

    CompanyPageRequest request = new CompanyPageRequest.Builder()
        .offset(0L)
        .nbRequested(NUMBER_OF_RESULTS)
        .sortCriterion(CompanySortCriteria.ID)
        .sortOrder(SortOrder.ASC)
        .build();

    SearchWrapper<Company> returnedWrapper = companyService.findAll(request);

    assertEquals(wrapper, returnedWrapper);

  }

}
