package fr.heffebaycay.cdb.util;

public class AppSettings {

  
  public static final String DB_USER;
  public static final String DB_PASSWORD;
  public static final String DB_NAME;
  public static final String DB_URL;
  
  static {
    DB_USER = "admincdb";
    DB_PASSWORD = "qwerty1234";
    DB_NAME = "computer-database-db";
    
    DB_URL = getMySQLConnectionURL();
  }
  
  private static String getMySQLConnectionURL() {
    
    String url = String.format("jdbc:mysql://127.0.0.1:3306/%s?zeroDateTimeBehavior=convertToNull", DB_NAME );
    
    return url;
    
  }
  
  
}
