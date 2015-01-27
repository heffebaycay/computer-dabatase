package fr.heffebaycay.cdb.service.impl.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import fr.heffebaycay.cdb.dao.IComputerDao;
import fr.heffebaycay.cdb.dao.exception.DaoException;
import fr.heffebaycay.cdb.dao.impl.SQLComputerDao;
import fr.heffebaycay.cdb.dao.manager.DaoManager;
import fr.heffebaycay.cdb.model.Company;
import fr.heffebaycay.cdb.model.Computer;
import fr.heffebaycay.cdb.model.ComputerPageRequest;
import fr.heffebaycay.cdb.service.impl.ComputerServiceJDBCImpl;
import fr.heffebaycay.cdb.util.ComputerSortCriteria;
import fr.heffebaycay.cdb.util.SortOrder;
import fr.heffebaycay.cdb.wrapper.SearchWrapper;

public class TestComputerService {

  private static final Long NUMBER_OF_RESULTS = 10L;

  IComputerDao              computerDao;

  ComputerServiceJDBCImpl   computerService;

  List<Computer>            computersDB;

  @Before
  public void setUp() {

    computerDao = mock(IComputerDao.class);
    DaoManager daoManager = mock(DaoManager.class);
    computerService = new ComputerServiceJDBCImpl(daoManager, computerDao);

    computersDB = new ArrayList<Computer>();

    Company apple = new Company.Builder().id(1).name("Apple Inc.").build();

    Company thinkingMachines = new Company.Builder().id(2).name("Thinking Machines").build();

    Computer c1 = new Computer.Builder().id(1).company(apple).name("MacBook Pro 15.4 inch").build();

    Computer c2 = new Computer.Builder().id(2).name("CM-2a").company(thinkingMachines).build();

    Computer c3 = new Computer.Builder().id(3).name("CM-200").company(thinkingMachines).build();

    Computer c4 = new Computer.Builder().id(4).name("CM-5e").company(thinkingMachines).build();

    Computer c5 = new Computer.Builder().id(5).name("CM-5").company(thinkingMachines)
        .introduced(LocalDateTime.parse("1991-01-01T00:00:00")).build();

    Computer c6 = new Computer.Builder().id(6).name("MacBook Pro").company(apple)
        .introduced(LocalDateTime.parse("2006-01-10T00:00:00")).build();

    // This one isn't going to be added to the data source
    Computer c7 = new Computer.Builder().id(42).name("My Awesome Computer").company(apple).build();

    // Updated version of c1
    Computer c8 = new Computer.Builder().id(1).company(apple).name("Ipad 27").build();

    computersDB.add(c1);
    computersDB.add(c2);
    computersDB.add(c3);
    computersDB.add(c4);
    computersDB.add(c5);
    computersDB.add(c6);
  }

  @Test
  public void testFindAll() {

    if (computerService == null) {
      fail("Computer Service not initialized");
    }

    try {
      when(computerDao.findAll()).thenReturn(computersDB);
    } catch (DaoException e) {
      fail("testFindAll(): Failed to setup Mockito: " + e.getMessage());
    }

    List<Computer> computers = computerService.findAll();

    assertEquals(computersDB, computers);

  }

  @Test
  public void testFindById() {

    if (computerService == null) {
      fail("Computer Service not initialized");
    }

    final long COMPUTER_ID = 2;

    Computer computerExpected = computersDB.stream().filter(c -> c.getId() == COMPUTER_ID)
        .findFirst().get();

    try {
      when(computerDao.findById(Matchers.eq(COMPUTER_ID))).thenReturn(computerExpected);
    } catch (DaoException e) {
      fail("testFindById(): Failed to setup Mockito: " + e.getMessage());
    }

    Computer computer = computerService.findById(COMPUTER_ID);

    assertEquals(computerExpected, computer);

  }

  @Test
  public void testRemove() {

    if (computerService == null) {
      fail("Computer Service not initialized");
    }

    try {
      doAnswer(new Answer<Object>() {
        public Object answer(InvocationOnMock invocation) {

          long idToRemove = invocation.getArgumentAt(0, Long.class);

          return computersDB.removeIf(c -> c.getId() == idToRemove);

        }
      }).when(computerDao).remove(Matchers.anyLong());
    } catch (DaoException e) {
      fail("testRemove(): Failed to setup Mockito: " + e.getMessage());
    }

    Random rand = new Random();
    int randomIndex = rand.nextInt(computersDB.size());

    Computer computerExpected = computersDB.get(randomIndex);

    computerService.remove(computerExpected.getId());

    // Returns true if no elements are matched
    boolean result = computersDB.stream().noneMatch(c -> c == computerExpected);

    assertEquals(true, result);

  }

  @Test
  public void testCreate() {

    if (computerService == null) {
      fail("Computer Service not initialized");
    }

    try {
      doAnswer(new Answer<Long>() {
        public Long answer(InvocationOnMock invocation) {

          Computer computerToCreate = invocation.getArgumentAt(0, Computer.class);

          computersDB.add(computerToCreate);

          return computerToCreate.getId();
        }
      }).when(computerDao).create(Matchers.any(Computer.class));
    } catch (DaoException e) {
      fail("testCreate(): Failed to setup Mockito: " + e.getMessage());
    }

    Company apple = new Company.Builder().id(1).name("Apple Inc.").build();
    Computer computer = new Computer.Builder().id(42).name("My Awesome Computer").company(apple)
        .build();

    computerService.create(computer);

    Computer foundComputer = computersDB.stream().filter(c -> c == computer).findFirst().get();

    assertEquals(computer, foundComputer);

  }

  @Test
  public void testUpdate() {
    if (computerService == null) {
      fail("Computer Service not initialized");
    }

    try {
      doAnswer(new Answer<Object>() {
        public Object answer(InvocationOnMock invocation) {

          Computer computerToUpdate = invocation.getArgumentAt(0, Computer.class);

          computersDB.removeIf(c -> c.getId() == computerToUpdate.getId());
          computersDB.add(computerToUpdate);

          return null;

        }
      }).when(computerDao).update(Matchers.any(Computer.class));
    } catch (DaoException e) {
      fail("testUpdate(): Failed to setup Mockito: " + e.getMessage());
    }

    Random rnd = new Random();
    int randomIndex = rnd.nextInt(computersDB.size());

    Computer originalComputer = computersDB.get(randomIndex);

    Computer copyComputer = new Computer.Builder().id(originalComputer.getId())
        .name(originalComputer.getName()).introduced(originalComputer.getIntroduced())
        .discontinued(originalComputer.getDiscontinued()).company(originalComputer.getCompany())
        .build();

    copyComputer.setName("ABCDEFGHIJKLMNOPQZRS");
    copyComputer.setIntroduced(LocalDateTime.now());
    copyComputer.setDiscontinued(LocalDateTime.now());

    computerService.update(copyComputer);

    originalComputer = computersDB.stream().filter(c -> c.getId() == copyComputer.getId())
        .findFirst().get();

    assertEquals(copyComputer, originalComputer);
  }

  @Test
  public void testFindAllWithOffset() {
    if (computerService == null) {
      fail("Computer Service not initialized");
    }

    ComputerPageRequest request = new ComputerPageRequest.Builder()
        .offset(0L)
        .nbRequested(NUMBER_OF_RESULTS)
        .sortCriterion(ComputerSortCriteria.ID)
        .sortOrder(SortOrder.ASC)
        .build();

    SearchWrapper<Computer> wrapper = new SearchWrapper<Computer>();
    try {
      when(computerDao.findAll(Matchers.any(ComputerPageRequest.class))).thenReturn(wrapper);
    } catch (DaoException e) {
      fail("testFindAllWithOffset(): Failed to setup Mockito: " + e.getMessage());
    }

    SearchWrapper<Computer> returnedWrapper = computerService.findAll(request);

    assertEquals(wrapper, returnedWrapper);
  }

}
