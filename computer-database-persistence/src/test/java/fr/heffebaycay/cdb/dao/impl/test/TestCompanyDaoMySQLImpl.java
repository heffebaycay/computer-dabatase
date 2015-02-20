package fr.heffebaycay.cdb.dao.impl.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.heffebaycay.cdb.dao.impl.SQLCompanyDao;
import fr.heffebaycay.cdb.model.Company;
import fr.heffebaycay.cdb.model.CompanyPageRequest;
import fr.heffebaycay.cdb.util.CompanySortCriteria;
import fr.heffebaycay.cdb.util.SortOrder;
import fr.heffebaycay.cdb.wrapper.SearchWrapper;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContextPersistenceTest.xml" })
@Transactional
public class TestCompanyDaoMySQLImpl {

  //Passing a reference to the test SQL utils class to the DAO
  @Autowired
  SQLCompanyDao companyDao;
  
  @Autowired
  MySQLUtils sqlUtils;

  List<Company> localCompanies;

  @Before
  public void setUp() throws Exception {

    sqlUtils.truncateTables();

    final Company c1 = new Company.Builder().id(1).name("Apple").build();

    final Company c2 = new Company.Builder().id(2).name("HP").build();

    final Company c3 = new Company.Builder().id(3).name("IBM").build();

    final Company c4 = new Company.Builder().id(4).name("Compaq").build();

    final Company c5 = new Company.Builder().id(5).name("Acer").build();

    final Company c6 = new Company.Builder().id(6).name("Atari").build();

    final Company c7 = new Company.Builder().id(7).name("Xerox").build();

    final Company c8 = new Company.Builder().id(8).name("Sanyo").build();

    final Company c9 = new Company.Builder().id(9).name("Cray").build();

    final Company c10 = new Company.Builder().id(10).name("Asus").build();

    localCompanies = new ArrayList<Company>();

    localCompanies.add(c1);
    localCompanies.add(c2);
    localCompanies.add(c3);
    localCompanies.add(c4);
    localCompanies.add(c5);
    localCompanies.add(c6);
    localCompanies.add(c7);
    localCompanies.add(c8);
    localCompanies.add(c9);
    localCompanies.add(c10);

    Connection conn = sqlUtils.getConnection();
    final String insertSQL = "INSERT INTO company(id, name) VALUES(?,?)";
    final PreparedStatement ps = conn.prepareStatement(insertSQL);

    for (Company c : localCompanies) {
      ps.setLong(1, c.getId());
      ps.setString(2, c.getName());

      ps.executeUpdate();
    }
    
    
    sqlUtils.closeStatement(ps);
    sqlUtils.closeConnection(conn);

  }

  @Test
  public void testFindAll() {

    List<Company> companies = null;
    companies = companyDao.findAll();

    assertEquals(localCompanies, companies);
  }

  @Test
  public void testFindById() {

    Company company = null;

    company = companyDao.findById(2);

    assertEquals(2, company.getId());

    boolean checked = false;

    for (Company c : localCompanies) {
      if (c.getId() == 2) {

        assertEquals(c, company);
        checked = true;
      }
    }

    if (!checked) {
      fail("Returned company wasn't part of localCompanies list");
    }

  }

  @Test
  public void testFindByIdNegativeId() {
    Company company = null;

    company = companyDao.findById(-1);

    //fail("A DaoException was expected but was not thrown");
    assertEquals(null, company);
  }

  @Test
  public void testFindAllWithOffset() {

    CompanyPageRequest request = new CompanyPageRequest.Builder()
        .offset(0L)
        .nbRequested(5L)
        .sortCriterion(CompanySortCriteria.ID)
        .sortOrder(SortOrder.ASC)
        .build();

    SearchWrapper<Company> wrapper = companyDao.findAll(request);

    assertEquals(2, wrapper.getTotalPage());
    assertEquals(1, wrapper.getCurrentPage());
    assertEquals(10, wrapper.getTotalCount()); // We stored 10 elements in the Database

    List<Company> reducedList = new ArrayList<Company>();
    for (int i = 0; i < 5; i++) {
      reducedList.add(localCompanies.get(i));
    }

    assertEquals(reducedList, wrapper.getResults());

  }

  @Test
  public void testFindByNameNull() {

    CompanyPageRequest request = new CompanyPageRequest.Builder()
        .offset(0L)
        .nbRequested(10L)
        .searchQuery(null)
        .sortCriterion(CompanySortCriteria.NAME)
        .sortOrder(SortOrder.DESC)
        .build();

    SearchWrapper<Company> wrapper = companyDao.findByName(request);

    assertEquals(0, wrapper.getResults().size());
  }

  @Test
  public void testFindByNameEmptyValue() {
    CompanyPageRequest request = new CompanyPageRequest.Builder()
        .offset(0L)
        .nbRequested(10L)
        .searchQuery("")
        .sortCriterion(CompanySortCriteria.NAME)
        .sortOrder(SortOrder.ASC)
        .build();

    SearchWrapper<Company> wrapper = companyDao.findByName(request);

    assertEquals(true, wrapper.getResults().isEmpty());
  }

  @Test
  public void testFindByName() {
    CompanyPageRequest request = new CompanyPageRequest.Builder()
        .offset(0L)
        .nbRequested(10L)
        .searchQuery("a")
        .sortCriterion(CompanySortCriteria.ID)
        .sortOrder(SortOrder.ASC)
        .build();

    SearchWrapper<Company> wrapper = companyDao.findByName(request);

    List<Company> companies = wrapper.getResults();

    List<Company> filteredCompanies = localCompanies.stream()
        .filter(c -> c.getName().toLowerCase().contains("a")).collect(Collectors.toList());

    assertEquals(filteredCompanies, companies);

  }

  @Test
  public void testFindByNameInvalidOffset() {
    CompanyPageRequest request = new CompanyPageRequest.Builder()
        .offset(-25L)
        .nbRequested(10L)
        .searchQuery("ple")
        .sortCriterion(CompanySortCriteria.ID)
        .sortOrder(SortOrder.ASC)
        .build();

    SearchWrapper<Company> wrapper = companyDao.findByName(request);

    assertEquals(0, wrapper.getResults().size());
  }

  @Test
  public void testFindByNameInvalidResultNumber() {
    CompanyPageRequest request = new CompanyPageRequest.Builder()
        .offset(0L)
        .nbRequested(-31L)
        .searchQuery("ple")
        .sortCriterion(CompanySortCriteria.NAME)
        .sortOrder(SortOrder.ASC)
        .build();

    SearchWrapper<Company> wrapper = companyDao.findByName(request);

    assertEquals(0, wrapper.getResults().size());
  }

  public void setCompanyDao(SQLCompanyDao companyDao) {
    this.companyDao = companyDao;
  }

  public void setSqlUtils(MySQLUtils sqlUtils) {
    this.sqlUtils = sqlUtils;
  }
  
  

}
