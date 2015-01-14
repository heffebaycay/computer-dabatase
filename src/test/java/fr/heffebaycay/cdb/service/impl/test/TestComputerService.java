package fr.heffebaycay.cdb.service.impl.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import fr.heffebaycay.cdb.dao.impl.ComputerDaoMySQLImpl;
import fr.heffebaycay.cdb.model.Company;
import fr.heffebaycay.cdb.model.Computer;
import fr.heffebaycay.cdb.service.IComputerService;
import fr.heffebaycay.cdb.service.impl.ComputerServiceMockImpl;
import fr.heffebaycay.cdb.wrapper.SearchWrapper;

public class TestComputerService {

  private static final int NUMBER_OF_RESULTS = 10;
  
  IComputerService computerService;  
  
  @Before
  public void setUp() {
    
    ComputerDaoMySQLImpl computerDao = mock(ComputerDaoMySQLImpl.class);
    computerService = new ComputerServiceMockImpl(computerDao);
    
    List<Computer> computers = new ArrayList<Computer>();

    Company apple = new Company.Builder().id(1).name("Apple Inc.").build();

    Company thinkingMachines = new Company.Builder().id(2).name("Thinking Machines").build();

    Computer c1 = new Computer.Builder()
                                  .id(1)
                                  .company(apple)
                                  .name("MacBook Pro 15.4 inch")
                                  .build();
    
    Computer c2 = new Computer.Builder()
                                  .id(2)
                                  .name("CM-2a")
                                  .company(thinkingMachines)
                                  .build();
    
    Computer c3 = new Computer.Builder()
                                  .id(3)
                                  .name("CM-200")
                                  .company(thinkingMachines)
                                  .build();

    Computer c4 = new Computer.Builder()
                                  .id(4)
                                  .name("CM-5e")
                                  .company(thinkingMachines)
                                  .build();

    Computer c5 = new Computer.Builder()
                                  .id(5)
                                  .name("CM-5")
                                  .company(thinkingMachines)
                                  .introduced(LocalDateTime.parse("1991-01-01T00:00:00"))
                                  .build();

    Computer c6 = new Computer.Builder()
                                 .id(6)
                                 .name("MacBook Pro")
                                 .company(apple)
                                 .introduced(LocalDateTime.parse("2006-01-10T00:00:00"))
                                 .build();
    
    // This one isn't going to be added to the data source
    Computer c7 = new Computer.Builder()
                                .id(42)
                                .name("My Awesome Computer")
                                .company(apple)
                                .build();
    
    // Updated version of c1
    Computer c8 = new Computer.Builder()
                                    .id(1)
                                    .company(apple)
                                    .name("Ipad 27")
                                    .build();

    computers.add(c1);
    computers.add(c2);
    computers.add(c3);
    computers.add(c4);
    computers.add(c5);
    computers.add(c6);
    
    // findAll()
    when(computerDao.findAll()).thenReturn(computers);
    
    // findById()
    doAnswer(new Answer<Object>() {
      public Object answer(InvocationOnMock invocation) {
                
        Object[] args = invocation.getArguments();
        long computerId = (long) args[0];
        
        for(Computer c : computers) {
          if(c.getId() == computerId) {
            return c;
          }
        }
        
        return null;
        
      }
      
    }).when(computerDao).findById(c1.getId());
    
    when(computerDao.findById(c2.getId())).thenReturn(c2);
    when(computerDao.findById(c3.getId())).thenReturn(c3);
    when(computerDao.findById(c4.getId())).thenReturn(c4);
    when(computerDao.findById(c5.getId())).thenReturn(c5);
    when(computerDao.findById(c6.getId())).thenReturn(c6);
    when(computerDao.findById(c7.getId())).thenReturn(c7);
    
    // remove()
    doAnswer(new Answer<Object>() {
      public Object answer(InvocationOnMock invocation) {
        Object[] args = invocation.getArguments();
        
        // First argument is of type long
        long computerId = (long) args[0];
        
        for(int i = 0; i < computers.size(); i++) {
          if(computers.get(i).getId() == computerId) {
            computers.remove(i);
            break;
          }
        }
        
        return null;
      }
    }).when(computerDao).remove(3);
    
    // create()
    doAnswer(new Answer<Object>() {
      
      public Object answer(InvocationOnMock invocation) {
        Object[] args = invocation.getArguments();
        
        // First argument is of type Computer
        Computer computer = (Computer)args[0];
        
        computers.add(computer);
        
        return null;
      }
      
    }).when(computerDao).create(c7);
    
    // update
    doAnswer(new Answer<Object>() {
      public Object answer(InvocationOnMock invocation) {
        Object[] args = invocation.getArguments();
        
        // First argument is of type Computer
        Computer computer = (Computer) args[0];
        
        for(int i = 0; i < computers.size(); i++) {
          if(computers.get(i).getId() == computer.getId() ) {
            computers.remove(i);
            break;
          }
        }
        
        computers.add(computer);
               
        //when(computerDao.findById(computer.getId())).thenReturn(computer);
        
        return null;
      }
    }).when(computerDao).update(c8);
    
    // findAllWithOffset
    SearchWrapper<Computer> wrapper = new SearchWrapper<Computer>();
    wrapper.setResults(computers);
    wrapper.setCurrentPage(1);
    wrapper.setTotalPage(1);
    wrapper.setTotalQueryCount(computers.size());
    
    when(computerDao.findAll(0, NUMBER_OF_RESULTS)).thenReturn(wrapper);
    
    
  }

  @Test
  public void testFindAll() {
    
    if(computerService == null) {
      fail("Computer Service not initialized");
    }
    
    List<Computer> computers = computerService.findAll();
    
    assertEquals(6, computers.size());
    
    for(Computer c : computers) {
      
      long computerId = c.getId();
      
      if(computerId == 1) {
        
        assertEquals("MacBook Pro 15.4 inch", c.getName());
        assertEquals("Apple Inc.", c.getCompany().getName());
        assertEquals(1, c.getCompany().getId());
        
      } else if(computerId == 5) {
        
        assertEquals("CM-5", c.getName());
        assertEquals("Thinking Machines", c.getCompany().getName());
        assertEquals(2, c.getCompany().getId());
        assertEquals(LocalDateTime.parse("1991-01-01T00:00:00"), c.getIntroduced());
        assertEquals(null, c.getDiscontinued());
        
      }
      
    }
  }
  
  @Test
  public void testFindById() {
    
    if(computerService == null) {
      fail("Computer Service not initialized");
    }
    
    Computer computer = computerService.findById(6);
    
    assertEquals(6, computer.getId());
    assertEquals("MacBook Pro", computer.getName());
    
    assertEquals(1, computer.getCompany().getId());
    assertEquals("Apple Inc.", computer.getCompany().getName());
    
    
  }
  
  @Test
  public void testRemove() {
    
    if(computerService == null) {
      fail("Computer Service not initialized");
    }
    
    Company thinkingMachines = new Company.Builder().id(2).name("Thinking Machines").build();
    Computer computer = new Computer.Builder()
                                        .id(3)
                                        .name("CM-200")
                                        .company(thinkingMachines)
                                        .build();
    
    computerService.remove(computer.getId());
    
    List<Computer> computers = computerService.findAll();
    
    for(Computer c : computers ) {
      
      if(c.getId() == computer.getId()) {
        
        fail("Computer is stil in the data source");
        
      }
      
    }
    
  }
  
  @Test
  public void testCreate() {
    
    if(computerService == null) {
      fail("Computer Service not initialized");
    }
    
    
    Company apple = new Company.Builder().id(1).name("Apple Inc.").build();
    Computer computer = new Computer.Builder()
    .id(42)
    .name("My Awesome Computer")
    .company(apple)
    .build();
    
    computerService.create(computer);
    
    
    
    Computer foundComputer = computerService.findById(computer.getId());
    
    
    assertEquals(computer, foundComputer);
    
  }

  @Test
  public void testUpdate() {
    if(computerService == null) {
      fail("Computer Service not initialized");
    }
    
    Company apple = new Company.Builder().id(1).name("Apple Inc.").build();
    Computer c1 = new Computer.Builder()
                                .id(1)
                                .company(apple)
                                .name("MacBook Pro 15.4 inch")
                                .build();
                                
    
    c1.setName("Ipad 27");
    
    computerService.update(c1);
    
    Computer foundComputer = computerService.findById(c1.getId());
    
    assertEquals(c1.getName(), foundComputer.getName());
    assertEquals(c1, foundComputer);
    
    
  }
  
  @Test
  public void testFindAllWithOffset() {
    if(computerService == null) {
      fail("Computer Service not initialized");
    }
    
    SearchWrapper<Computer> wrapper = computerService.findAll(0, NUMBER_OF_RESULTS);
    
    if(wrapper.getResults().size() > NUMBER_OF_RESULTS) {
      fail("Wrapper cannot contain more results than requested");
    }
  }
  
}
