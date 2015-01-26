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
import fr.heffebaycay.cdb.dao.exception.DaoException;
import fr.heffebaycay.cdb.dao.impl.mapper.ComputerMySQLRowMapper;
import fr.heffebaycay.cdb.dao.impl.util.MySQLUtils;
import fr.heffebaycay.cdb.model.Computer;
import fr.heffebaycay.cdb.util.ComputerSortCriteria;
import fr.heffebaycay.cdb.util.SortOrder;
import fr.heffebaycay.cdb.wrapper.SearchWrapper;

public class SQLComputerDao implements IComputerDao {

  private static final Logger LOGGER = LoggerFactory.getLogger(SQLComputerDao.class);

  private MySQLUtils          sqlUtils;

  public SQLComputerDao() {
    this.sqlUtils = new MySQLUtils();
  }

  public SQLComputerDao(MySQLUtils sqlUtils) {
    this.sqlUtils = sqlUtils;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<Computer> findAll(Connection conn) throws DaoException {
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
      sqlUtils.closeStatement(stmt);
    }

    return computers;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Computer findById(long id, Connection conn) throws DaoException {
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
      sqlUtils.closeStatement(ps);
    }

    return computer;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean remove(long id, Connection conn) throws DaoException {
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
      sqlUtils.closeStatement(ps);
    }

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public long create(Computer computer, Connection conn) throws DaoException {

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
      sqlUtils.closeStatement(ps);
    }

    return newComputerId;

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void update(Computer computer, Connection conn) throws DaoException {

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
      sqlUtils.closeStatement(ps);
    }

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public SearchWrapper<Computer> findAll(long offset, long nbRequested,
      ComputerSortCriteria sortCriterion, SortOrder sortOrder, Connection conn) throws DaoException {

    SearchWrapper<Computer> searchWrapper = new SearchWrapper<Computer>();
    List<Computer> computers = new ArrayList<Computer>();

    if (offset < 0 || nbRequested <= 0) {
      searchWrapper.setResults(computers);
      searchWrapper.setCurrentPage(0);
      searchWrapper.setTotalPage(0);
      searchWrapper.setTotalQueryCount(0);

      return searchWrapper;
    }

    String orderPart;
    if (sortCriterion.equals(ComputerSortCriteria.COMPANY_NAME)) {
      orderPart = generateOrderPart("cp", sortCriterion, sortOrder);
    } else {
      orderPart = generateOrderPart("c", sortCriterion, sortOrder);
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
      searchWrapper.setTotalQueryCount(countResult.getLong("count"));
      sqlUtils.closeStatement(stmt);

      long currentPage = (long) Math.ceil(offset * 1.0 / nbRequested) + 1;
      searchWrapper.setCurrentPage(currentPage);

      long totalPage = (long) Math.ceil(searchWrapper.getTotalQueryCount() * 1.0 / nbRequested);
      searchWrapper.setTotalPage(totalPage);

      ps = conn.prepareStatement(query);
      ps.setLong(1, offset);
      ps.setLong(2, nbRequested);
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
      sqlUtils.closeStatement(ps);
    }

    return searchWrapper;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int removeForCompany(long companyId, Connection conn) throws DaoException {

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
  public SearchWrapper<Computer> findByName(String name, long offset, long nbRequested,
      ComputerSortCriteria sortCriterion, SortOrder sortOrder, Connection conn) throws DaoException {
    SearchWrapper<Computer> searchWrapper = new SearchWrapper<Computer>();
    List<Computer> computers = new ArrayList<Computer>();

    if (offset < 0 || nbRequested <= 0) {
      searchWrapper.setResults(computers);
      searchWrapper.setCurrentPage(0);
      searchWrapper.setTotalPage(0);
      searchWrapper.setTotalQueryCount(0);

      return searchWrapper;
    }

    // Escaping '%' character
    name = name.replace("%", "\\%");

    String orderPart;

    if (sortCriterion.equals(ComputerSortCriteria.COMPANY_NAME)) {
      orderPart = generateOrderPart("cp", sortCriterion, sortOrder);
    } else {
      orderPart = generateOrderPart("c", sortCriterion, sortOrder);
    }

    String query = "SELECT c.id, c.name, c.introduced, c.discontinued, cp.id AS cpId, cp.name AS cpName FROM computer AS c LEFT JOIN company AS cp ON c.company_id = cp.id WHERE c.name LIKE ? OR cp.name LIKE ? ORDER BY "
        + orderPart + " LIMIT ?, ?";
    String countQuery = "SELECT COUNT(c.id) AS count FROM computer AS c LEFT JOIN company AS cp ON c.company_id = cp.id WHERE c.name LIKE ? OR cp.name LIKE ? ORDER BY "
        + orderPart;

    PreparedStatement ps = null;

    try {

      String searchKeyword = String.format("%%%s%%", name);
      LOGGER.debug("Search keyword: " + searchKeyword);

      PreparedStatement countStmt = conn.prepareStatement(countQuery);
      countStmt.setString(1, searchKeyword);
      countStmt.setString(2, searchKeyword);

      ResultSet countResult = countStmt.executeQuery();
      countResult.first();
      searchWrapper.setTotalQueryCount(countResult.getLong("count"));
      sqlUtils.closeStatement(countStmt);

      long currentPage = (long) Math.ceil(offset * 1.0 / nbRequested) + 1;
      searchWrapper.setCurrentPage(currentPage);

      long totalPage = (long) Math.ceil(searchWrapper.getTotalQueryCount() * 1.0 / nbRequested);
      searchWrapper.setTotalPage(totalPage);

      ps = conn.prepareStatement(query);

      ps.setString(1, searchKeyword);
      ps.setString(2, searchKeyword);
      ps.setLong(3, offset);
      ps.setLong(4, nbRequested);

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
      sqlUtils.closeStatement(ps);
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
