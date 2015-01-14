package fr.heffebaycay.cdb.model.tests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import fr.heffebaycay.cdb.model.Company;

public class TestCompany {

  Company company;
  
  @Before
  public void initialize() {
    
    company = new Company.Builder()
                            .id(42)
                            .name("Mapple")
                            .build();
    
  }
  
  @Test
  public void testGetId() {
    
    long id = company.getId();
    
    assertEquals(42, id);
    
  }
  
  @Test
  public void testGetName() {
    
    String name = company.getName();
    
    assertEquals("Mapple", name);
    
  }

}
