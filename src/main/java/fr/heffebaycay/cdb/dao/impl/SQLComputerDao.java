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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import fr.heffebaycay.cdb.dao.IComputerDao;
import fr.heffebaycay.cdb.dao.exception.DaoException;
import fr.heffebaycay.cdb.dao.impl.mapper.ComputerMySQLRowMapper;
import fr.heffebaycay.cdb.dao.impl.util.MySQLUtils;
import fr.heffebaycay.cdb.dao.manager.DaoManager;
import fr.heffebaycay.cdb.model.Computer;
import fr.heffebaycay.cdb.model.ComputerPageRequest;
import fr.heffebaycay.cdb.util.ComputerSortCriteria;
import fr.heffebaycay.cdb.util.SortOrder;
import fr.heffebaycay.cdb.wrapper.SearchWrapper;

@Repository
public class SQLComputerDao implements IComputerDao {

  private static final Logger LOGGER = LoggerFactory.getLogger(SQLComputerDao.class);

  
  private DaoManager daoManager;
  
  @Autowired
  public SQLComputerDao(DaoManager daoManager) {
    this.daoManager = daoManager;
  }


  /**
   * {@inheritDoc}
   */
  @Override
  public List<Computer> findAll() throws DaoException {
    Connection conn = daoManager.getConnection();
    Statement stmt = null;

    String query = "SELECT c.id, c.name, c.introduced, c.discontinued, cp.id AS cpId, cp.name AS cpName FROM computer AS c LEFT JOIN company AS cp ON c.company_id = cp.id";

    ResultSet results;
    List<Computer> computers = new ArrayList<Computer>();

    try {
      stmt = conn.createStatement();
      results = stmt.executeQuery(query);

      while (results.next()) {

        ComputerMySQLRowMapper mapper = new ComputerMySQLRowMapper();
        Computer computer = mapper.mapRow(results);

        computers.add(computer);

      }
    } catch (SQLException e) {
      LOGGER.error("findAll(): SQLException: ", e);
      throw new DaoException(e);
    } finally {
      daoManager.closeStatement(stmt);
    }

    return computers;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Computer findById(long id) throws DaoException {
    Connection conn = daoManager.getConnection();
    Computer computer = null;
    PreparedStatement ps = null;

    String query = "SELECT c.id, c.name, c.introduced, c.discontinued, cp.id AS cpId, cp.name AS cpName FROM computer AS c LEFT JOIN company AS cp ON c.company_id = cp.id WHERE c.id = ?";

    ResultSet results;

    try {

      ps = conn.prepareStatement(query);
      ps.setLong(1, id);

      results = ps.executeQuery();
      if (results.first()) {

        ComputerMySQLRowMapper mapper = new ComputerMySQLRowMapper();
        computer = mapper.mapRow(results);

      }

    } catch (SQLException e) {
      LOGGER.error("findById(): SQLException:", e);
      throw new DaoException(e);
    } finally {
      daoManager.closeStatement(ps);
    }

    return computer;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean remove(long id) throws DaoException {
    Connection conn = daoManager.getConnection();
    String query = "DELETE FROM computer WHERE id = ?";
    PreparedStatement ps = null;

    try {

      ps = conn.prepareStatement(query);
      ps.setLong(1, id);

      ps.executeUpdate();

      return true;

    } catch (SQLException e) {
      LOGGER.error("remove(): SQLException: ", e);
      throw new DaoException(e);
    } finally {
      daoManager.closeStatement(ps);
    }

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public long create(Computer computer) throws DaoException {
    Connection conn = daoManager.getConnection();
    
    if (computer == null) {
      throw new IllegalArgumentException("'computer' argument cannot be null");
    }

    long newComputerId = -1;

    String query = "INSERT INTO computer(name, introduced, discontinued, company_id) VALUES(?,?,?,?)";

    PreparedStatement ps = null;

    try {

      ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
      ps.setString(1, computer.getName());

      if (computer.getIntroduced() != null) {
        ps.setTimestamp(2, Timestamp.valueOf(computer.getIntroduced()));
      } else {
        ps.setTimestamp(2, null);
      }

      if (computer.getDiscontinued() != null) {
        ps.setTimestamp(3, Timestamp.valueOf(computer.getDiscontinued()));
      } else {
        ps.setTimestamp(3, null);
      }

      if (computer.getCompany() != null) {
        ps.setLong(4, computer.getCompany().getId());
      } else {
        ps.setObject(4, null);
      }

      ps.executeUpdate();
      ResultSet resultSet = ps.getGeneratedKeys();
      if (resultSet.next()) {
        newComputerId = resultSet.getLong(1);
      }

    } catch (SQLException e) {
      LOGGER.error("create(): SQLException: ", e);
      throw new DaoException(e);
    } finally {
      daoManager.closeStatement(ps);
    }

    return newComputerId;

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void update(Computer computer) throws DaoException {
    Connection conn = daoManager.getConnection();
    
    if (computer == null) {
      throw new IllegalArgumentException("'computer' argument cannot be null");
    }

    String query = "UPDATE computer SET name=?, introduced=?, discontinued=?, company_id=? WHERE id=?";
    PreparedStatement ps = null;

    try {

      ps = conn.prepareStatement(query);
      ps.setString(1, computer.getName());

      if (computer.getIntroduced() == null) {
        ps.setTimestamp(2, null);
      } else {
        ps.setTimestamp(2, Timestamp.valueOf(computer.getIntroduced()));
      }

      if (computer.getDiscontinued() == null) {
        ps.setTimestamp(3, null);
      } else {
        ps.setTimestamp(3, Timestamp.valueOf(computer.getDiscontinued()));
      }

      if (computer.getCompany() != null) {
        ps.setLong(4, computer.getCompany().getId());
      } else {
        ps.setObject(4, null);
      }

      ps.setLong(5, computer.getId());

      ps.executeUpdate();

    } catch (SQLException e) {
      LOGGER.error("update(): SQLException: ", e);
      throw new DaoException(e);
    } finally {
      daoManager.closeStatement(ps);
    }

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int removeForCompany(long companyId) throws DaoException {
    Connection conn = daoManager.getConnection();
    
    String removeForCompanySQL = "DELETE FROM computer WHERE company_id = ?";

    try {
      PreparedStatement ps = conn.prepareStatement(removeForCompanySQL);
      ps.setLong(1, companyId);
      return ps.executeUpdate();
    } catch (SQLException e) {
      LOGGER.warn("removeForCompany(): SQL Exception: ", e);
      throw new DaoException(e);
    }

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public SearchWrapper<Computer> findByName(ComputerPageRequest request) throws DaoException {
    Connection conn = daoManager.getConnection();
    
    if(request == null) {
      throw new DaoException("PageRequest object cannot be null");
    }
    
    SearchWrapper<Computer> searchWrapper = new SearchWrapper<Computer>();
    List<Computer> computers = new ArrayList<Computer>();

    searchWrapper.setSearchQuery(request.getSearchQuery());
    searchWrapper.setSortOrder(request.getSortOrder().getName());
    searchWrapper.setSortCriterion(request.getSortCriterion().getName());
    
    if (request.getOffset() < 0 || request.getNbRequested() <= 0) {
      searchWrapper.setResults(computers);
      searchWrapper.setCurrentPage(0);
      searchWrapper.setTotalPage(0);
      searchWrapper.setTotalCount(0);

      return searchWrapper;
    }

    String orderPart;

    if(ComputerSortCriteria.COMPANY_NAME.equals(request.getSortCriterion())) {
      orderPart = generateOrderPart("cp", request.getSortCriterion(), request.getSortOrder());
    } else {
      orderPart = generateOrderPart("c", request.getSortCriterion(), request.getSortOrder());
    }
    

    String query = "SELECT c.id, c.name, c.introduced, c.discontinued, cp.id AS cpId, cp.name AS cpName FROM computer AS c LEFT JOIN company AS cp ON c.company_id = cp.id WHERE c.name LIKE ? OR cp.name LIKE ? ORDER BY "
        + orderPart + " LIMIT ?, ?";
    String countQuery = "SELECT COUNT(c.id) AS count FROM computer AS c LEFT JOIN company AS cp ON c.company_id = cp.id WHERE c.name LIKE ? OR cp.name LIKE ? ORDER BY "
        + orderPart;

    PreparedStatement ps = null;

    try {

      //  Escaping '%' character
      String searchKeyword = request.getSearchQuery().replace("%", "\\%");
      searchKeyword = String.format("%%%s%%", searchKeyword);
      LOGGER.debug("Search keyword: " + searchKeyword);

      PreparedStatement countStmt = conn.prepareStatement(countQuery);
      countStmt.setString(1, searchKeyword);
      countStmt.setString(2, searchKeyword);

      ResultSet countResult = countStmt.executeQuery();
      countResult.first();
      searchWrapper.setTotalCount(countResult.getLong("count"));
      daoManager.closeStatement(countStmt);

      long currentPage = (long) Math.ceil(request.getOffset() * 1.0 / request.getNbRequested()) + 1;
      searchWrapper.setCurrentPage(currentPage);

      long totalPage = (long) Math.ceil(searchWrapper.getTotalCount() * 1.0 / request.getNbRequested());
      searchWrapper.setTotalPage(totalPage);

      ps = conn.prepareStatement(query);

      ps.setString(1, searchKeyword);
      ps.setString(2, searchKeyword);
      ps.setLong(3, request.getOffset());
      ps.setLong(4, request.getNbRequested());

      ResultSet rs = ps.executeQuery();

      while (rs.next()) {
        ComputerMySQLRowMapper mapper = new ComputerMySQLRowMapper();
        Computer computer = mapper.mapRow(rs);

        computers.add(computer);
      }

      searchWrapper.setResults(computers);

    } catch (SQLException e) {
      LOGGER.warn("findByName(): SQLException: ", e);
      throw new DaoException(e);
    } finally {
      daoManager.closeStatement(ps);
    }

    return searchWrapper;
  }

  @Override
  public SearchWrapper<Computer> findAll(ComputerPageRequest request) throws DaoException {
    Connection conn = daoManager.getConnection();
    
    if(request == null) {
      throw new DaoException("PageRequest object cannot be null");
    }
    
    SearchWrapper<Computer> searchWrapper = new SearchWrapper<Computer>();
    List<Computer> computers = new ArrayList<Computer>();
    
    searchWrapper.setSortOrder(request.getSortOrder().getName());
    searchWrapper.setSortCriterion(request.getSortCriterion().getName());

    if (request.getOffset() < 0 || request.getNbRequested() <= 0) {
      searchWrapper.setResults(computers);
      searchWrapper.setCurrentPage(0);
      searchWrapper.setTotalPage(0);
      searchWrapper.setTotalCount(0);

      return searchWrapper;
    }

    String orderPart;
    
    if (ComputerSortCriteria.COMPANY_NAME.equals(request.getSortCriterion())) {
      orderPart = generateOrderPart("cp", request.getSortCriterion(), request.getSortOrder());
    } else {
      orderPart = generateOrderPart("c", request.getSortCriterion(), request.getSortOrder());
    }

    String query = "SELECT c.id, c.name, c.introduced, c.discontinued, cp.id AS cpId, cp.name AS cpName FROM computer AS c LEFT JOIN company AS cp ON c.company_id = cp.id ORDER BY "
        + orderPart + " LIMIT ?, ?";
    String countQuery = "SELECT COUNT(c.id) AS count FROM computer AS c LEFT JOIN company AS cp ON c.company_id = cp.id ORDER BY "
        + orderPart;

    PreparedStatement ps = null;

    try {

      // First, we need to know exactly how many results they are, aggregated
      Statement stmt = conn.createStatement();
      ResultSet countResult = stmt.executeQuery(countQuery);
      countResult.first();
      searchWrapper.setTotalCount(countResult.getLong("count"));
      daoManager.closeStatement(stmt);

      long currentPage = (long) Math.ceil(request.getOffset() * 1.0 / request.getNbRequested()) + 1;
      searchWrapper.setCurrentPage(currentPage);

      long totalPage = (long) Math.ceil(searchWrapper.getTotalCount() * 1.0 / request.getNbRequested());
      searchWrapper.setTotalPage(totalPage);

      ps = conn.prepareStatement(query);
      ps.setLong(1, request.getOffset());
      ps.setLong(2, request.getNbRequested());
      ResultSet rs = ps.executeQuery();

      while (rs.next()) {
        ComputerMySQLRowMapper mapper = new ComputerMySQLRowMapper();
        Computer computer = mapper.mapRow(rs);

        computers.add(computer);
      }

      searchWrapper.setResults(computers);

    } catch (SQLException e) {
      LOGGER.error("findAll: SQL Exception: ", e);
      throw new DaoException(e);
    } finally {
      daoManager.closeStatement(ps);
    }

    return searchWrapper;
  }

  private String generateOrderPart(String entityAlias, ComputerSortCriteria sortCriterion,
      SortOrder sortOrder) {
    // Thread synchronization isn't an issue in this scope
    // So using a StringBuilder is safe
    StringBuilder stringBuilder = new StringBuilder(entityAlias);

    switch (sortCriterion) {

      case ID:
        stringBuilder.append(".id");
        break;
      case NAME:
        stringBuilder.append(".name");
        break;
      case DATE_DISCONTINUED:
        stringBuilder.append(".discontinued");
        break;
      case DATE_INTRODUCED:
        stringBuilder.append(".introduced");
        break;
      case COMPANY_NAME:
        stringBuilder.append(".name");
        break;
      default:
        stringBuilder.append(".id");
    }

    if (sortOrder.equals(SortOrder.DESC)) {
      stringBuilder.append(" desc");
    } else {
      stringBuilder.append(" asc");
    }

    return stringBuilder.toString();

  }

}
