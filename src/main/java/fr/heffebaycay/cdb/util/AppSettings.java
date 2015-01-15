package fr.heffebaycay.cdb.util;

public class AppSettings {

  
  public static final String DB_USER;
  public static final String DB_PASSWORD;
  public static final String DB_PROD_NAME;
  public static final String DB_TEST_NAME;
  
  public static final long NB_RESULTS_PAGE;
  
  static {
    DB_USER = "admincdb";
    DB_PASSWORD = "qwerty1234";
    DB_PROD_NAME = "computer-database-db";
    DB_TEST_NAME = "computer-database-db-test";
    
    NB_RESULTS_PAGE = 10;
  }
  
}
