package fr.heffebaycay.cdb.service.impl.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import fr.heffebaycay.cdb.dao.impl.CompanyDaoMySQLImpl;
import fr.heffebaycay.cdb.model.Company;
import fr.heffebaycay.cdb.service.ICompanyService;
import fr.heffebaycay.cdb.service.impl.CompanyServiceMockImpl;
import fr.heffebaycay.cdb.util.CompanySortCriteria;
import fr.heffebaycay.cdb.util.SortOrder;
import fr.heffebaycay.cdb.wrapper.SearchWrapper;

public class TestCompanyService {

  private static final int NUMBER_OF_RESULTS = 10;
  
  ICompanyService     companyService;
  CompanyDaoMySQLImpl companyDao;

  Connection conn = null;
  
  @Before
  public void setUp() {

    companyDao = mock(CompanyDaoMySQLImpl.class);
    companyService = new CompanyServiceMockImpl(companyDao);

    
    Company c1 = new Company.Builder()
                                .id(1)
                                .name("Apple")
                                .build();
    
    Company c2 = new Company.Builder()
                                .id(2)
                                .name("HP")
                                .build();
    
    Company c3 = new Company.Builder()
                                .id(3)
                                .name("IBM")
                                .build();
    
    Company c4 = new Company.Builder()
                                .id(2)
                                .name("Compaq")
                                .build();
    
    List<Company> companies = new ArrayList<Company>();
    companies.add(c1);
    companies.add(c2);
    companies.add(c3);
    
    // findAll()
    when(companyDao.findAll(conn)).thenReturn(companies);
    
    // findById()
    when(companyDao.findById(1, conn)).thenReturn(c1);
    when(companyDao.findById(2, conn)).thenReturn(c2);
    when(companyDao.findById(3, conn)).thenReturn(c3);
    
    // create()
    doAnswer(new Answer<Object>() {
      
      public Object answer(InvocationOnMock invocation) {
        
        Object[] args = invocation.getArguments();
        
        // The one and only argument of the method is of type "Company"
        Company company = (Company) args[0];
        companies.add(company);
        
        return null;
        
      }
      
    }).when(companyDao).create(c4, conn);
    
    // findAllWithOffset
    SearchWrapper<Company> wrapper = new SearchWrapper<Company>();
    wrapper.setResults(companies);
    wrapper.setCurrentPage(1);
    wrapper.setTotalPage(1);
    wrapper.setTotalQueryCount(companies.size());
    
    when(companyDao.findAll(0, NUMBER_OF_RESULTS, CompanySortCriteria.ID, SortOrder.ASC, conn)).thenReturn(wrapper);
    
  }

  @Test
  public void testFindAll() {

    if (companyService == null) {
      fail("Company Service isn't initialized");
    }

    List<Company> companies = companyService.findAll();

    assertEquals(3, companies.size());
    
    for(Company c : companies) {
      long computerId = c.getId();
      
      if(computerId == 1) {
        assertEquals("Apple", c.getName());
      } else if(computerId == 2) {
        assertEquals("HP", c.getName());
      } else if(computerId == 3) {
        assertEquals("IBM", c.getName());
      } else {
        fail("Unknown company in list");
      }
      
    }

  }

  @Test
  public void testFindById() {

    if (companyService == null) {
      fail("Company Service isn't initialized");
    }

    Company company = companyService.findById(2);

    assertEquals("HP", company.getName());

  }

  @Test
  public void testCreate() {

    if (companyService == null) {
      fail("CompanyService isn't initialized");
    }

    Company c4 = new Company.Builder()
                                .id(4)
                                .name("Compaq")
                                .build();

    companyService.create(c4);

    List<Company> companies = companyService.findAll();
    
    for(Company c : companies) {
      if(c.getId() == 4) {
        assertEquals("Compaq", c.getName());
      }
    }
    
    
    
  }

  @Test
  public void testFindAllWithOffset() {
    
    if (companyService == null) {
      fail("CompanyService isn't initialized");
    }

    SearchWrapper<Company> wrapper = companyService.findAll(0, NUMBER_OF_RESULTS, CompanySortCriteria.ID, SortOrder.ASC);

    if(wrapper.getResults().size() > NUMBER_OF_RESULTS) {
      fail("Wrapper cannot contain more results than requested");
    }
  }

}
