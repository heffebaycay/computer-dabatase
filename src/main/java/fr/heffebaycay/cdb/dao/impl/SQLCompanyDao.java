package fr.heffebaycay.cdb.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.heffebaycay.cdb.dao.ICompanyDao;
import fr.heffebaycay.cdb.dao.exception.DaoException;
import fr.heffebaycay.cdb.dao.impl.mapper.CompanyMySQLRowMapper;
import fr.heffebaycay.cdb.dao.impl.util.MySQLUtils;
import fr.heffebaycay.cdb.model.Company;
import fr.heffebaycay.cdb.util.CompanySortCriteria;
import fr.heffebaycay.cdb.util.SortOrder;
import fr.heffebaycay.cdb.wrapper.SearchWrapper;

public class SQLCompanyDao implements ICompanyDao {

  private static final Logger LOGGER = LoggerFactory.getLogger(SQLCompanyDao.class);

  private MySQLUtils          sqlUtils;

  public SQLCompanyDao() {
    sqlUtils = new MySQLUtils();
  }

  public SQLCompanyDao(MySQLUtils sqlUtils) {
    this.sqlUtils = sqlUtils;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<Company> findAll(Connection conn) throws DaoException {

    Statement stmt = null;

    final String query = "SELECT id, name FROM company";
    ResultSet results;
    List<Company> companies = new ArrayList<Company>();

    try {
      stmt = conn.createStatement();
      results = stmt.executeQuery(query);

      while (results.next()) {

        CompanyMySQLRowMapper mapper = new CompanyMySQLRowMapper();
        Company company = mapper.mapRow(results);

        companies.add(company);
      }

    } catch (SQLException e) {
      LOGGER.error("SQLException: {}", e);
      throw new DaoException(e);
    } finally {
      sqlUtils.closeStatement(stmt);
    }

    return companies;

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Company findById(long id, Connection conn) throws DaoException {

    Company company = null;
    PreparedStatement ps = null;

    String query = "SELECT id, name FROM company WHERE id = ?";
    ResultSet results;

    try {
      ps = conn.prepareStatement(query);
      ps.setLong(1, id);

      results = ps.executeQuery();
      if (results.first()) {

        CompanyMySQLRowMapper mapper = new CompanyMySQLRowMapper();
        company = mapper.mapRow(results);
      }

    } catch (SQLException e) {
      LOGGER.error("SQLException: {}", e);
      throw new DaoException(e);

    } finally {
      sqlUtils.closeStatement(ps);
    }

    return company;

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public SearchWrapper<Company> findAll(long offset, long nbRequested,
      CompanySortCriteria sortCriterion, SortOrder sortOrder, Connection conn) throws DaoException {
    SearchWrapper<Company> searchWrapper = new SearchWrapper<Company>();
    List<Company> companies = new ArrayList<Company>();

    if (offset < 0 || nbRequested <= 0) {
      searchWrapper.setResults(companies);
      searchWrapper.setCurrentPage(0);
      searchWrapper.setTotalPage(0);
      searchWrapper.setTotalQueryCount(0);

      return searchWrapper;
    }

    String orderPart = generateOrderPart("c", sortCriterion, sortOrder);

    String query = "SELECT c.id, c.name FROM company AS c ORDER BY " + orderPart + " LIMIT ?, ?";
    String countQuery = "SELECT COUNT(c.id) AS count FROM company AS c ORDER BY " + orderPart;

    PreparedStatement ps = null;

    try {

      // Counting the total number of elements first
      Statement stmt = conn.createStatement();
      ResultSet countResult = stmt.executeQuery(countQuery);
      countResult.first();
      searchWrapper.setTotalQueryCount(countResult.getLong("count"));

      // Closing the first statement
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

        CompanyMySQLRowMapper mapper = new CompanyMySQLRowMapper();
        Company company = mapper.mapRow(rs);

        companies.add(company);

      }

      searchWrapper.setResults(companies);

    } catch (SQLException e) {
      LOGGER.error("SQLException: {}", e);
      throw new DaoException(e);
    } finally {
      sqlUtils.closeStatement(ps);
    }

    return searchWrapper;
  }

  @Override
  public void create(Company company, Connection conn) throws DaoException {

    String query = "INSERT INTO company(name) VALUES(?)";

    PreparedStatement ps = null;

    try {

      ps = conn.prepareStatement(query);
      ps.setString(1, company.getName());

      ps.executeUpdate();

    } catch (SQLException e) {
      LOGGER.error("SQLException: {}", e);
      throw new DaoException(e);
    } finally {
      sqlUtils.closeStatement(ps);
    }

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int remove(long id, Connection conn) throws DaoException {

    String removeCompanyQuery = "DELETE FROM company WHERE id = ?";
    PreparedStatement ps = null;
    try {
      ps = conn.prepareStatement(removeCompanyQuery);
      ps.setLong(1, id);
      int nbCompany = ps.executeUpdate();
      return nbCompany;

    } catch (SQLException e) {
      LOGGER.warn("remove(): SQLException: ", e);
      throw new DaoException(e);
    } finally {
      sqlUtils.closeStatement(ps);
    }

  }

  private String generateOrderPart(String entityAlias, CompanySortCriteria sortCriterion,
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

  @Override
  public SearchWrapper<Company> findByName(String name, long offset, long nbRequested,
      CompanySortCriteria sortCriterion, SortOrder sortOrder, Connection conn) throws DaoException {

    SearchWrapper<Company> searchWrapper = new SearchWrapper<Company>();
    List<Company> companies = new ArrayList<Company>();

    if (name == null || name.isEmpty() || offset < 0 || nbRequested <= 0) {
      searchWrapper.setResults(companies);
      searchWrapper.setCurrentPage(0);
      searchWrapper.setTotalPage(0);
      searchWrapper.setTotalQueryCount(0);

      return searchWrapper;
    }

    name = name.replace("%", "\\%");

    String orderPart = generateOrderPart("c", sortCriterion, sortOrder);

    String query = "SELECT c.id, c.name FROM company AS c WHERE c.name LIKE ? ORDER BY "
        + orderPart + " LIMIT ?, ?";
    String countQuery = "SELECT COUNT(c.id) AS count FROM company AS c WHERE c.name LIKE ? ORDER BY "
        + orderPart;

    PreparedStatement ps = null;

    try {
      String searchKeyword = String.format("%%%s%%", name);
      LOGGER.debug(String.format("findByName(): Keyword={%s}", searchKeyword));

      PreparedStatement countStmt = conn.prepareStatement(countQuery);
      countStmt.setString(1, searchKeyword);

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
      ps.setLong(2, offset);
      ps.setLong(3, nbRequested);

      ResultSet rs = ps.executeQuery();

      while (rs.next()) {
        CompanyMySQLRowMapper mapper = new CompanyMySQLRowMapper();
        Company company = mapper.mapRow(rs);

        companies.add(company);
      }

      searchWrapper.setResults(companies);
    } catch (SQLException e) {
      LOGGER.warn("findByName(): SQLException: ", e);
      throw new DaoException(e);
    } finally {
      sqlUtils.closeStatement(ps);
    }

    return searchWrapper;
  }

}
