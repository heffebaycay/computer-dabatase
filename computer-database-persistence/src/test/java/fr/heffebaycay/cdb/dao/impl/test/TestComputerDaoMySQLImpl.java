package fr.heffebaycay.cdb.dao.impl.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.heffebaycay.cdb.dao.exception.DaoException;
import fr.heffebaycay.cdb.dao.impl.SQLComputerDao;
import fr.heffebaycay.cdb.model.Company;
import fr.heffebaycay.cdb.model.Computer;
import fr.heffebaycay.cdb.model.ComputerPageRequest;
import fr.heffebaycay.cdb.util.ComputerSortCriteria;
import fr.heffebaycay.cdb.util.SortOrder;
import fr.heffebaycay.cdb.wrapper.SearchWrapper;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContextPersistence.xml" })
@Transactional
public class TestComputerDaoMySQLImpl {

  @Autowired
  MySQLUtils     sqlUtils;

  //Passing a reference to the test SQL utils class to the DAO
  @Autowired
  SQLComputerDao computerDao;

  List<Computer> localComputers;

  @Before
  public void setUp() throws Exception {

    Connection conn;

    sqlUtils.truncateTables();

    // Setting up Companies
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

    List<Company> companies = new ArrayList<Company>();
    companies.add(c1);
    companies.add(c2);
    companies.add(c3);
    companies.add(c4);
    companies.add(c5);
    companies.add(c6);
    companies.add(c7);
    companies.add(c8);
    companies.add(c9);
    companies.add(c10);

    conn = sqlUtils.getConnection();

    final String insertCompanySQL = "INSERT INTO company(id, name) VALUES(?,?)";
    final PreparedStatement companyPS = conn.prepareStatement(insertCompanySQL);

    for (Company company : companies) {
      companyPS.setLong(1, company.getId());
      companyPS.setString(2, company.getName());

      companyPS.executeUpdate();
    }

    sqlUtils.closeStatement(companyPS);

    // Setting up computers

    final Computer comp1 = new Computer.Builder().id(1).company(c1).name("MacBook Pro 15.4 inch")
        .build();

    final Computer comp2 = new Computer.Builder().id(2).name("HP Awesome Computer")
        .introduced(LocalDateTime.parse("2013-01-06T00:00:00")).company(c2).build();

    final Computer comp3 = new Computer.Builder().id(3).name("HP Oldie")
        .introduced(LocalDateTime.parse("1998-06-12T00:00:00"))
        .discontinued(LocalDateTime.parse("2001-10-25T00:00:00")).company(c2).build();

    final Computer comp4 = new Computer.Builder().id(4).name("Acer Inspire o/")
        .introduced(LocalDateTime.parse("2003-12-25T00:00:00"))
        .discontinued(LocalDateTime.parse("2007-02-02T00:00:00")).company(c5).build();

    final Computer comp5 = new Computer.Builder().id(5).name("A Computer With No Company")
        .introduced(LocalDateTime.parse("1975-12-01T00:00:00"))
        .discontinued(LocalDateTime.parse("1980-01-01T00:00:00")).build();

    final Computer comp6 = new Computer.Builder().id(6).name("MacBook Pro").company(c1)
        .introduced(LocalDateTime.parse("2006-01-10T00:00:00")).build();

    final Computer comp7 = new Computer.Builder().id(7).name("My Awesome Computer").company(c3)
        .build();

    localComputers = new ArrayList<Computer>();

    localComputers.add(comp1);
    localComputers.add(comp2);
    localComputers.add(comp3);
    localComputers.add(comp4);
    localComputers.add(comp5);
    localComputers.add(comp6);
    localComputers.add(comp7);

    String insertComputerSQL = "INSERT INTO computer(id, name, introduced, discontinued, company_id) VALUES(?,?,?,?,?)";
    PreparedStatement computerPS = conn.prepareStatement(insertComputerSQL);

    for (Computer computer : localComputers) {
      computerPS.setLong(1, computer.getId());
      computerPS.setString(2, computer.getName());

      if (computer.getIntroduced() != null) {
        computerPS.setTimestamp(3, Timestamp.valueOf(computer.getIntroduced()));
      } else {
        computerPS.setTimestamp(3, null);
      }

      if (computer.getDiscontinued() != null) {
        computerPS.setTimestamp(4, Timestamp.valueOf(computer.getDiscontinued()));
      } else {
        computerPS.setTimestamp(4, null);
      }

      if (computer.getCompany() != null) {
        computerPS.setLong(5, computer.getCompany().getId());
      } else {
        computerPS.setObject(5, null);
      }

      computerPS.executeUpdate();
    }

    sqlUtils.closeStatement(computerPS);
    sqlUtils.closeConnection(conn);

  }

  @Test
  public void testFindAll() {

    List<Computer> computers = null;

    try {
      computers = computerDao.findAll();
    } catch (DaoException e) {
      fail("DaoException thrown by findAll()");
    }

    assertEquals(localComputers, computers);

  }

  @Test
  public void testFindAllWithOffset() {

    ComputerPageRequest request = new ComputerPageRequest.Builder()
        .offset(0L)
        .nbRequested(5L)
        .sortCriterion(ComputerSortCriteria.ID)
        .sortOrder(SortOrder.ASC)
        .build();

    SearchWrapper<Computer> wrapper = null;
    try {
      wrapper = computerDao.findAll(request);
    } catch (DaoException e) {
      fail("IComputerDao::findAll() threw a DaoException: " + e.getMessage());
    }

    assertEquals(1, wrapper.getCurrentPage());
    assertEquals(7, wrapper.getTotalCount());
    assertEquals(2, wrapper.getTotalPage());

    List<Computer> reducedList = localComputers.subList(0, 5);

    assertEquals(reducedList, wrapper.getResults());

  }

  @Test
  public void testFindById() {

    Computer computer = null;
    try {
      computer = computerDao.findById(4);
    } catch (DaoException e) {
      fail("IComputerDao::findById() threw a DaoException: " + e.getMessage());
    }

    boolean checked = false;

    for (Computer localComputer : localComputers) {
      if (localComputer.getId() == computer.getId()) {

        assertEquals(localComputer, computer);
        checked = true;
      }
    }

    if (!checked) {
      fail("Fetched computer isn't part of localComputers list");
    }

  }

  @Test
  public void testCreate() {

    Company company = new Company.Builder().id(4).name("Compaq").build();

    Computer computer = new Computer.Builder().name("Testing Computer")
        .introduced(LocalDateTime.parse("2014-06-26T00:00:00"))
        .discontinued(LocalDateTime.parse("2015-01-01T00:00:00")).company(company).build();

    long computerId = -1;
    try {
      computerId = computerDao.create(computer);
    } catch (DaoException e) {
      fail("IComputerDao::create() threw a DaoException: " + e.getMessage());
    }

    if (computerId == -1) {
      fail("Computer was not created, as no valid id was returned");
    }

    computer.setId(computerId);

    Computer fetchedComputer = null;
    try {
      fetchedComputer = computerDao.findById(computerId);
    } catch (DaoException e) {
      fail("IComputerDao::findById() threw a DaoException: " + e.getMessage());
    }

    assertEquals(computer, fetchedComputer);
  }

  @Test
  public void testCreateWithNullComputer() {

    try {
      computerDao.create(null);

      fail("Expected an IllegalArgumentException to be thrown");

    } catch (IllegalArgumentException e) {

      assertEquals("'computer' argument cannot be null", e.getMessage());

    } catch (DaoException e) {
      fail("IComputerDao::create() threw a DaoException: " + e.getMessage());
    }

  }

  @Test
  public void testUpdate() {

    Computer computer = null;
    try {
      computer = computerDao.findById(7);
    } catch (DaoException e) {
      fail("IComputerDao::findById() threw a DaoException: " + e.getMessage());
    }

    Company company = new Company.Builder().id(8).name("Sanyo").build();

    computer.setCompany(company);
    computer.setName("Awesome Computer, Built by Sanyo");
    computer.setIntroduced(LocalDateTime.parse("2011-09-02T00:00:00"));
    computer.setDiscontinued(LocalDateTime.parse("2013-03-27T00:00:00"));

    try {
      computerDao.update(computer);
    } catch (DaoException e) {
      fail("IComputerDao::update() threw a DaoException: " + e.getMessage());
    }

    Computer updatedComputer = null;
    try {
      updatedComputer = computerDao.findById(7);
    } catch (DaoException e) {
      fail("IComputerDao::findById() threw a DaoException: " + e.getMessage());
    }

    assertEquals(computer, updatedComputer);
  }

  @Test
  public void testUpdatedWithNullComputer() {

    try {

      computerDao.update(null);

      fail("Expected an IllegalArgumentException to be thrown");

    } catch (IllegalArgumentException e) {

      assertEquals("'computer' argument cannot be null", e.getMessage());

    } catch (DaoException e) {
      fail("IComputerDao::update() threw a DaoException: " + e.getMessage());
    }

  }
}
