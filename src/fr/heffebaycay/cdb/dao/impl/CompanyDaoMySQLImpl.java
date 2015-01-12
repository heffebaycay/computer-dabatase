package fr.heffebaycay.cdb.dao.impl;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import fr.heffebaycay.cdb.dao.ICompanyDao;
import fr.heffebaycay.cdb.model.Company;
import fr.heffebaycay.cdb.util.AppSettings;

public class CompanyDaoMySQLImpl implements ICompanyDao {

   
  @Override
  public List<Company> getCompanies() {

    Connection conn = getConnection();
    
    final String query = "SELECT id, name FROM company";
    ResultSet results;
    List<Company> companies = new ArrayList<Company>();
    
    
    try {
      Statement stmt = conn.createStatement();
      results = stmt.executeQuery(query);
      
      while(results.next()) {
        long id = results.getLong("id");
        String name = results.getString("name");
        
        Company company = new Company.Builder()
                                          .id(id)
                                          .name(name)
                                          .build();
        
        companies.add(company);
        
      }
      
    } catch(SQLException e) {
      
      System.out.printf("[Error] SQLException: %s\n", e.getMessage());
      
    } finally {
      
      try {
        conn.close();
      } catch (SQLException e) {
        System.out.printf("[Error] Failed to close DB connection: %s", e.getMessage());
      }
    }
    
    return companies;
    
  }

  @Override
  public Company findById(long id) {
    
    Company company = null;
    Connection conn = getConnection();
    
    String query = "SELECT name FROM company WHERE id = ?";
    ResultSet results;
    
    try {
      PreparedStatement ps = conn.prepareStatement(query);
      ps.setLong(1, id);
      
      results = ps.executeQuery();
      if(results.first()) {
        
        String name = results.getString("name");
        
        company = new Company.Builder()
                                  .id(id)
                                  .name(name)
                                  .build();
      }
      
    } catch(SQLException e) {
      
      System.out.printf("[Error] SQLException: %s\n", e.getMessage());
      
      
    } finally {
      try {
        conn.close();
      } catch (SQLException e) {
        System.out.printf("[Error] Failed to close DB connection: %s", e.getMessage());
      }
    }
    
    return company;
    
  }

  @Override
  public void create(Company company) {

    // Not implemented
    
    
  }
  
  private Connection getConnection() {
    
    Connection conn = null;
    
    try {
      conn = DriverManager.getConnection(AppSettings.DB_URL, AppSettings.DB_USER, AppSettings.DB_PASSWORD);
    } catch (SQLException e) {
      System.out.printf("[Error] Failed to get SQL Connection - %s\n", e.getMessage());
    }
    
    return conn;
    
    
    
    
  }

  
  
  
}
