package fr.heffebaycay.cdb.service.impl.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.naming.ldap.Rdn;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import fr.heffebaycay.cdb.dao.ICompanyDao;
import fr.heffebaycay.cdb.dao.impl.CompanyDaoMySQLImpl;
import fr.heffebaycay.cdb.model.Company;
import fr.heffebaycay.cdb.model.Computer;
import fr.heffebaycay.cdb.service.ICompanyService;
import fr.heffebaycay.cdb.service.impl.CompanyServiceMockImpl;
import fr.heffebaycay.cdb.util.CompanySortCriteria;
import fr.heffebaycay.cdb.util.SortOrder;
import fr.heffebaycay.cdb.wrapper.SearchWrapper;

public class TestCompanyService {

  private static final int NUMBER_OF_RESULTS = 10;

  ICompanyService          companyService;
  ICompanyDao              companyDao;

  Connection               conn              = null;

  List<Company>            companiesDB;

  @Before
  public void setUp() {

    companyDao = mock(CompanyDaoMySQLImpl.class);
    companyService = new CompanyServiceMockImpl(companyDao);

    Company c1 = new Company.Builder().id(1).name("Apple").build();

    Company c2 = new Company.Builder().id(2).name("HP").build();

    Company c3 = new Company.Builder().id(3).name("IBM").build();

    Company c4 = new Company.Builder().id(2).name("Compaq").build();

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

    when(companyDao.findAll(Matchers.any(Connection.class))).thenReturn(companiesDB);

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

    }).when(companyDao).findById(Matchers.anyLong(), Matchers.any(Connection.class));

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

    }).when(companyDao).create(Matchers.any(Company.class), Matchers.any(Connection.class));

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

    when(
        companyDao.findAll(Matchers.anyLong(), Matchers.anyLong(),
            Matchers.any(CompanySortCriteria.class), Matchers.any(SortOrder.class),
            Matchers.any(Connection.class))).thenReturn(wrapper);

    SearchWrapper<Company> returnedWrapper = companyService.findAll(0, NUMBER_OF_RESULTS,
        CompanySortCriteria.ID, SortOrder.ASC);

    
    assertEquals(wrapper, returnedWrapper);
    
  }

}
