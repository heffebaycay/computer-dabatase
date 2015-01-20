package fr.heffebaycay.cdb.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.heffebaycay.cdb.dao.IComputerDao;
import fr.heffebaycay.cdb.dao.impl.util.IMySQLUtils;
import fr.heffebaycay.cdb.dao.impl.util.MySQLProdUtils;
import fr.heffebaycay.cdb.model.Computer;
import fr.heffebaycay.cdb.wrapper.SearchWrapper;

public class ComputerDaoMySQLImpl implements IComputerDao {

  private static final Logger LOGGER = LoggerFactory.getLogger(ComputerDaoMySQLImpl.class);
  
  private IMySQLUtils sqlUtils;
  
  public ComputerDaoMySQLImpl() {
    this.sqlUtils = new MySQLProdUtils();
  }
  
  public ComputerDaoMySQLImpl(IMySQLUtils sqlUtils) {
    this.sqlUtils = sqlUtils;
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public List<Computer> findAll() {

    Connection conn = sqlUtils.getConnection();
    
    String query = "SELECT c.id, c.name, c.introduced, c.discontinued, cp.id AS cpId, cp.name AS cpName FROM computer AS c LEFT JOIN company AS cp ON c.company_id = cp.id";
    
    ResultSet results;
    List<Computer> computers = new ArrayList<Computer>();
    
    try {
      Statement stmt = conn.createStatement();
      results = stmt.executeQuery(query);
      
      while(results.next()) {
        
        ComputerMySQLRowMapper mapper = new ComputerMySQLRowMapper();
        Computer computer = mapper.mapRow(results);
        
        computers.add(computer);
        
      }
    } catch(SQLException e) {
      LOGGER.error("SQLException: {}", e);
    } finally {
      sqlUtils.closeConnection(conn);
    }
    
    return computers;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Computer findById(long id) {
    
    Computer computer = null;
    Connection conn = sqlUtils.getConnection();
    
    String query = "SELECT c.id, c.name, c.introduced, c.discontinued, cp.id AS cpId, cp.name AS cpName FROM computer AS c LEFT JOIN company AS cp ON c.company_id = cp.id WHERE c.id = ?";
    
    ResultSet results;
    
    try {
      
      PreparedStatement ps = conn.prepareStatement(query);
      ps.setLong(1, id);
      
      results = ps.executeQuery();
      if(results.first()) {
        
        ComputerMySQLRowMapper mapper = new ComputerMySQLRowMapper();
        computer = mapper.mapRow(results);
        
      }
      
    } catch(SQLException e) {
      LOGGER.error("SQLException: {}", e);
    } finally {
      sqlUtils.closeConnection(conn);
    }
    
    return computer;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean remove(long id) {
   
    Connection conn = sqlUtils.getConnection();
    String query = "DELETE FROM computer WHERE id = ?";
    
    try {
      
      PreparedStatement ps = conn.prepareStatement(query);
      ps.setLong(1, id);
      
      ps.executeUpdate();
      
      return true;
      
    } catch(SQLException e) {
      LOGGER.error("SQLException: {}", e);
      return false;
    } finally {
      sqlUtils.closeConnection(conn);
    }
    
    
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public long create(Computer computer) {
    
    if(computer == null) {
      throw new IllegalArgumentException("'computer' argument cannot be null");
    }
    
    long newComputerId = -1;
    
    String query = "INSERT INTO computer(name, introduced, discontinued, company_id) VALUES(?,?,?,?)";
    
    Connection conn = sqlUtils.getConnection();
    
    try {
      
      PreparedStatement ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
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
      ResultSet resultSet = ps.getGeneratedKeys();
      if(resultSet.next() ) {
        newComputerId = resultSet.getLong(1);
      }
      
      
    } catch(SQLException e) {
      LOGGER.error("SQLException: {}", e);
    } finally {
      sqlUtils.closeConnection(conn);
    }
    
    return newComputerId;
    
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void update(Computer computer) {
    
    if(computer == null) {
      throw new IllegalArgumentException("'computer' argument cannot be null");
    }
    
    String query = "UPDATE computer SET name=?, introduced=?, discontinued=?, company_id=? WHERE id=?";
    Connection conn = sqlUtils.getConnection();
    
    
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
      LOGGER.error("SQLException: {}", e);
    } finally {
      sqlUtils.closeConnection(conn);
    }
    
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public SearchWrapper<Computer> findAll(long offset, long nbRequested) {
    
    SearchWrapper<Computer> searchWrapper = new SearchWrapper<Computer>();
    List<Computer> computers = new ArrayList<Computer>();
    
    if(offset < 0 || nbRequested <= 0) {
      searchWrapper.setResults(computers);
      searchWrapper.setCurrentPage(0);
      searchWrapper.setTotalPage(0);
      searchWrapper.setTotalQueryCount(0);
      
      return searchWrapper;
    }
    
    String query = "SELECT c.id, c.name, c.introduced, c.discontinued, cp.id AS cpId, cp.name AS cpName FROM computer AS c LEFT JOIN company AS cp ON c.company_id = cp.id LIMIT ?, ?";
    String countQuery = "SELECT COUNT(c.id) AS count FROM computer AS c LEFT JOIN company AS cp ON c.company_id = cp.id";
    
    Connection conn = sqlUtils.getConnection();
    
    try {
      Statement stmt = conn.createStatement();
      ResultSet countResult = stmt.executeQuery(countQuery);
      countResult.first();
      
      searchWrapper.setTotalQueryCount(countResult.getLong("count"));
      
      long currentPage = (long) Math.ceil(offset * 1.0 / nbRequested) + 1;
      searchWrapper.setCurrentPage(currentPage);
      
      long totalPage = (long) Math.ceil(searchWrapper.getTotalQueryCount() * 1.0 / nbRequested);
      searchWrapper.setTotalPage(totalPage);
      
      PreparedStatement ps = conn.prepareStatement(query);
      ps.setLong(1, offset);
      ps.setLong(2, nbRequested);
      ResultSet rs = ps.executeQuery();
      
      while(rs.next()) {
        ComputerMySQLRowMapper mapper = new ComputerMySQLRowMapper();
        Computer computer = mapper.mapRow(rs);
        
        computers.add(computer);
      }
      
      searchWrapper.setResults(computers);
      
    } catch(SQLException e) {
      LOGGER.error("SQL Exception: {}", e);
    } finally {
      sqlUtils.closeConnection(conn);
    }
    
    return searchWrapper;
  }
  
  

  
  
  
}
