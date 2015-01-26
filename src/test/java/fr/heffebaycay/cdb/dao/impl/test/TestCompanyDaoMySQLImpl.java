package fr.heffebaycay.cdb.dao.impl.test;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import fr.heffebaycay.cdb.dao.impl.CompanyDaoMySQLImpl;
import fr.heffebaycay.cdb.dao.impl.util.MySQLUtils;
import fr.heffebaycay.cdb.dao.manager.DaoManager;
import fr.heffebaycay.cdb.model.Company;
import fr.heffebaycay.cdb.util.CompanySortCriteria;
import fr.heffebaycay.cdb.util.ComputerSortCriteria;
import fr.heffebaycay.cdb.util.SortOrder;
import fr.heffebaycay.cdb.wrapper.SearchWrapper;

public class TestCompanyDaoMySQLImpl {

  MySQLUtils          sqlUtils   = new MySQLUtils();

  //Passing a reference to the test SQL utils class to the DAO
  CompanyDaoMySQLImpl companyDao = new CompanyDaoMySQLImpl(sqlUtils);
  List<Company>       localCompanies;

  Connection          conn;

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

    conn = DaoManager.INSTANCE.getConnection();
    final String insertSQL = "INSERT INTO company(id, name) VALUES(?,?)";
    final PreparedStatement ps = conn.prepareStatement(insertSQL);

    for (Company c : localCompanies) {
      ps.setLong(1, c.getId());
      ps.setString(2, c.getName());

      ps.executeUpdate();
    }

    sqlUtils.closeStatement(ps);

  }

  @After
  public void tearDown() throws Exception {

    sqlUtils.truncateTables();

    DaoManager.INSTANCE.closeConnection(conn);

  }

  @Test
  public void testFindAll() {

    final List<Company> companies = companyDao.findAll(conn);

    assertEquals(localCompanies, companies);
  }

  @Test
  public void testFindById() {

    Company company = companyDao.findById(2, conn);

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
    Company company = companyDao.findById(-1, conn);

    assertEquals(null, company);
  }

  @Test
  public void testFindAllWithOffset() {

    SearchWrapper<Company> wrapper = companyDao.findAll(0, 5, CompanySortCriteria.ID,
        SortOrder.ASC, conn);

    assertEquals(2, wrapper.getTotalPage());
    assertEquals(1, wrapper.getCurrentPage());
    assertEquals(10, wrapper.getTotalQueryCount()); // We stored 10 elements in the Database

    List<Company> reducedList = new ArrayList<Company>();
    for (int i = 0; i < 5; i++) {
      reducedList.add(localCompanies.get(i));
    }

    assertEquals(reducedList, wrapper.getResults());

  }

  @Test
  public void testFindByNameNull() {

    SearchWrapper<Company> wrapper = companyDao.findByName(null, 0, 10, CompanySortCriteria.NAME,
        SortOrder.DESC, conn);

    assertEquals(0, wrapper.getResults().size());
  }
  
  @Test
  public void testFindByNameEmptyValue() {
    SearchWrapper<Company> wrapper = companyDao.findByName("", 0, 10, CompanySortCriteria.NAME, SortOrder.ASC, conn);
    
    assertEquals(true, wrapper.getResults().isEmpty());
  }

  @Test
  public void testFindByName() {
    SearchWrapper<Company> wrapper = companyDao.findByName("a", 0, 10, CompanySortCriteria.ID,
        SortOrder.ASC, conn);

    List<Company> companies = wrapper.getResults();

    List<Company> filteredCompanies = localCompanies.stream()
        .filter(c -> c.getName().toLowerCase().contains("a")).collect(Collectors.toList());

    assertEquals(filteredCompanies, companies);

  }

  @Test
  public void testFindByNameInvalidOffset() {
    SearchWrapper<Company> wrapper = companyDao.findByName("ple", -25, 10, CompanySortCriteria.ID,
        SortOrder.ASC, conn);

    assertEquals(0, wrapper.getResults().size());
  }
  
  @Test
  public void testFindByNameInvalidResultNumber() {
    SearchWrapper<Company> wrapper = companyDao.findByName("ple", 0, -31, CompanySortCriteria.ID, SortOrder.ASC, conn);
    
    assertEquals(0, wrapper.getResults().size());
  }
  
  

}
