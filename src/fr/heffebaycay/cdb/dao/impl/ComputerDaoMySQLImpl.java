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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.heffebaycay.cdb.dao.IComputerDao;
import fr.heffebaycay.cdb.dao.impl.util.MySQLUtils;
import fr.heffebaycay.cdb.model.Company;
import fr.heffebaycay.cdb.model.Computer;
import fr.heffebaycay.cdb.util.AppSettings;
import fr.heffebaycay.cdb.wrapper.SearchWrapper;

public class ComputerDaoMySQLImpl implements IComputerDao {

  private final Logger logger = LoggerFactory.getLogger(ComputerDaoMySQLImpl.class);
  
  /**
   * {@inheritDoc}
   */
  @Override
  public List<Computer> getComputers() {

    Connection conn = MySQLUtils.getConnection();
    
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
      logger.error("SQLException: {}", e);
    } finally {
      MySQLUtils.closeConnection(conn);
    }
    
    return computers;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Computer findById(long id) {
    
    Computer computer = null;
    Connection conn = MySQLUtils.getConnection();
    
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
      logger.error("SQLException: {}", e);
    } finally {
      MySQLUtils.closeConnection(conn);
    }
    
    return computer;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean remove(long id) {
   
    Connection conn = MySQLUtils.getConnection();
    String query = "DELETE FROM computer WHERE id = ?";
    
    try {
      
      PreparedStatement ps = conn.prepareStatement(query);
      ps.setLong(1, id);
      
      ps.executeUpdate();
      
      return true;
      
    } catch(SQLException e) {
      logger.error("SQLException: {}", e);
      return false;
    } finally {
      MySQLUtils.closeConnection(conn);
    }
    
    
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void create(Computer computer) {
    
    String query = "INSERT INTO computer(name, introduced, discontinued, company_id) VALUES(?,?,?,?)";
    
    Connection conn = MySQLUtils.getConnection();
    
    try {
      
      PreparedStatement ps = conn.prepareStatement(query);
      ps.setString(1, computer.getName());
      
      if(computer.getIntroduced() != null) {
        ps.setTimestamp(2, Timestamp.valueOf(computer.getIntroduced()));
      } else {
        ps.setTimestamp(2, null);
      }
      
      if(computer.getDiscontinued() != null) {
        ps.setTimestamp(3, Timestamp.valueOf(computer.getDiscontinued()));
      } else {
        ps.setTimestamp(3, null);
      }
      
      if(computer.getCompany() != null) {
        ps.setLong(4, computer.getCompany().getId());
      } else {
        ps.setObject(4, null);
      }
      
      ps.executeUpdate();
      
    } catch(SQLException e) {
      logger.error("SQLException: {}", e);
    } finally {
      MySQLUtils.closeConnection(conn);
    }
    
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void update(Computer computer) {
    
    String query = "UPDATE computer SET name=?, introduced=?, discontinued=?, company_id=? WHERE id=?";
    Connection conn = MySQLUtils.getConnection();
    
    
    try {
       
      PreparedStatement ps = conn.prepareStatement(query);
      ps.setString(1, computer.getName());
      
      if(computer.getIntroduced() == null) {
        ps.setTimestamp(2, null);
      } else {
        ps.setTimestamp(2, Timestamp.valueOf(computer.getIntroduced()));
      }
      
      if(computer.getDiscontinued() == null) {
        ps.setTimestamp(3, null);
      } else {
        ps.setTimestamp(3, Timestamp.valueOf(computer.getDiscontinued()));
      }
      
      
      if(computer.getCompany() != null) {
        ps.setLong(4, computer.getCompany().getId());
      } else {
        ps.setObject(4, null);
      }
      
      ps.setLong(5, computer.getId());
      
      ps.executeUpdate();
      
    } catch(SQLException e) {
      logger.error("SQLException: {}", e);
    } finally {
      MySQLUtils.closeConnection(conn);
    }
    
  }
  
  /**
   * Creates a <i>Computer</i> object for the current ResultSet cursor
   * 
   * @param computerSet         A <i>ResultSet</i> containing computers
   * @return                    A computer created from the data in the <i>ResultSet</i> object
   * @throws SQLException
   */
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

  /**
   * {@inheritDoc}
   */
  @Override
  public SearchWrapper<Computer> getComputers(long offset, long nbRequested) {
    
    SearchWrapper<Computer> searchWrapper = new SearchWrapper<Computer>();
    List<Computer> computers = new ArrayList<Computer>();
    
    if(offset < 0 || nbRequested < 0) {
      searchWrapper.setResults(computers);
      searchWrapper.setCurrentPage(0);
      searchWrapper.setTotalPage(0);
      searchWrapper.setTotalQueryCount(0);
      
      return searchWrapper;
    }
    
    String query = "SELECT c.id, c.name, c.introduced, c.discontinued, cp.id AS cpId, cp.name AS cpName FROM computer AS c LEFT JOIN company AS cp ON c.company_id = cp.id LIMIT ?, ?";
    String countQuery = "SELECT COUNT(c.id) AS count FROM computer AS c LEFT JOIN company AS cp ON c.company_id = cp.id";
    
    Connection conn = MySQLUtils.getConnection();
    
    try {
      Statement stmt = conn.createStatement();
      ResultSet countResult = stmt.executeQuery(countQuery);
      countResult.first();
      
      searchWrapper.setTotalQueryCount(countResult.getLong("count"));
      
      long currentPage = (long) Math.ceil(offset * 1.0 / AppSettings.NB_RESULTS_PAGE) + 1;
      searchWrapper.setCurrentPage(currentPage);
      
      long totalPage = (long) Math.ceil(searchWrapper.getTotalQueryCount() * 1.0 / AppSettings.NB_RESULTS_PAGE);
      searchWrapper.setTotalPage(totalPage);
      
      PreparedStatement ps = conn.prepareStatement(query);
      ps.setLong(1, offset);
      ps.setLong(2, nbRequested);
      ResultSet rs = ps.executeQuery();
      
      while(rs.next()) {
        Computer computer = createComputer(rs);
        
        computers.add(computer);
      }
      
      searchWrapper.setResults(computers);
      
    } catch(SQLException e) {
      logger.error("SQL Exception: {}", e);
    } finally {
      MySQLUtils.closeConnection(conn);
    }
    
    return searchWrapper;
  }
  
  

  
  
  
}
