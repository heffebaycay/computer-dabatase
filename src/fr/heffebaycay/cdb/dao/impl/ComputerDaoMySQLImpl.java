package fr.heffebaycay.cdb.dao.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import fr.heffebaycay.cdb.dao.IComputerDao;
import fr.heffebaycay.cdb.model.Company;
import fr.heffebaycay.cdb.model.Computer;
import fr.heffebaycay.cdb.util.AppSettings;

public class ComputerDaoMySQLImpl implements IComputerDao {

  @Override
  public List<Computer> getComputers() {

    Connection conn = getConnection();
    
    String query = "SELECT c.id, c.name, c.introduced, c.discontinued, cp.id AS cpId, cp.name AS cpName FROM computer AS c LEFT JOIN company AS cp ON c.company_id = cp.id";
    
    ResultSet results;
    List<Computer> computers = new ArrayList<Computer>();
    
    try {
      Statement stmt = conn.createStatement();
      results = stmt.executeQuery(query);
      
      while(results.next()) {
        
        Computer computer = createComputer(results);
        
        computers.add(computer);
        
      }
    } catch(SQLException e) {
      System.out.printf("[Error] SQLException: %s\n", e.getMessage());
    } finally {
      closeConnection(conn);
    }
    
    return computers;
  }

  @Override
  public Computer findById(long id) {
    
    Computer computer = null;
    Connection conn = getConnection();
    
    String query = "SELECT c.id, c.name, c.introduced, c.discontinued, cp.id AS cpId, cp.name AS cpName FROM computer AS c LEFT JOIN company AS cp ON c.company_id = cp.id WHERE c.id = ?";
    
    ResultSet results;
    
    try {
      
      PreparedStatement ps = conn.prepareStatement(query);
      ps.setLong(1, id);
      
      results = ps.executeQuery();
      if(results.first()) {
        
       computer = createComputer(results);
        
      }
      
    } catch(SQLException e) {
      System.out.printf("[Error] SQLException: %s\n", e.getMessage());
    } finally {
      closeConnection(conn);
    }
    
    return computer;
  }

  @Override
  public boolean remove(long id) {
   
    Connection conn = getConnection();
    String query = "DELETE FROM computer WHERE id = ?";
    
    try {
      
      PreparedStatement ps = conn.prepareStatement(query);
      ps.setLong(1, id);
      
      ps.executeUpdate();
      
      return true;
      
    } catch(SQLException e) {
      System.out.printf("[Error] SQLException: %s\n", e.getMessage());
      return false;
    } finally {
      closeConnection(conn);
    }
    
    
  }

  @Override
  public void create(Computer computer) {
    // TODO Auto-generated method stub
    
    String query = "INSERT INTO computer(name, introduced, discontinued, company_id) VALUES(?,?,?,?)";
    
    Connection conn = getConnection();
    
    try {
      
      PreparedStatement ps = conn.prepareStatement(query);
      ps.setString(1, computer.getName());
      
      if(computer.getIntroduced() != null) {
        ps.setTimestamp(2, Timestamp.valueOf(computer.getIntroduced()));
      }
      
      if(computer.getDiscontinued() != null) {
        ps.setTimestamp(3, Timestamp.valueOf(computer.getDiscontinued()));
      }
      
      if(computer.getCompany() != null) {
        ps.setLong(4, computer.getCompany().getId());
      }
      
      ps.executeUpdate();
      
    } catch(SQLException e) {
      System.out.printf("[Error] SQLException: %s\n", e.getMessage());
    } finally {
      closeConnection(conn);
    }
    
  }

  @Override
  public void update(Computer computer) {
    // TODO Auto-generated method stub
    
  }
  
  private void closeConnection(Connection conn) {
    try {
      if(conn != null) {
        conn.close();
      }
    } catch(SQLException e) {
      System.out.printf("[Error] Failed to close DB connection: %s", e.getMessage());
    }
  }
  
  private Connection getConnection() {
    Connection conn = null;
    
    try {
      conn = DriverManager.getConnection(AppSettings.DB_URL, AppSettings.DB_USER, AppSettings.DB_PASSWORD);
    } catch(SQLException e) {
      System.out.printf("[Error] Failed to get SQL Connection - %s\n", e.getMessage());
    }
    
    return conn;
    
  }
  
  private Computer createComputer(ResultSet computerSet) throws SQLException {
    
    Computer computer;
    Company company = null;
    
    long computerId = computerSet.getLong("id");
    String computerName = computerSet.getString("name");
    
    LocalDateTime introduced = null;
    Timestamp introducedStamp = computerSet.getTimestamp("introduced");
    if(introducedStamp != null) {
      introduced = introducedStamp.toLocalDateTime();
    }
    
    LocalDateTime discontinued = null;
    Timestamp discontinuedStamp = computerSet.getTimestamp("discontinued");
    if(discontinuedStamp != null) {
      discontinued = discontinuedStamp.toLocalDateTime();
    }

    long companyId = computerSet.getLong("cpId");
    String companyName = computerSet.getString("cpName");
    
    if(companyId > 0) {
      company = new Company.Builder()
                              .id(companyId)
                              .name(companyName)
                              .build();
    }
    
    
    
    computer = new Computer.Builder()
                                .id(computerId)
                                .name(computerName)
                                .introduced(introduced)
                                .discontinued(discontinued)
                                .company(company)
                                .build();
    
    
    return computer;
    
  }

  
  
  
}
