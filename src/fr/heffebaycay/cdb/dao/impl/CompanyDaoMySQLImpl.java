package fr.heffebaycay.cdb.dao.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.heffebaycay.cdb.dao.ICompanyDao;
import fr.heffebaycay.cdb.dao.impl.util.MySQLUtils;
import fr.heffebaycay.cdb.model.Company;
import fr.heffebaycay.cdb.util.AppSettings;
import fr.heffebaycay.cdb.wrapper.SearchWrapper;

public class CompanyDaoMySQLImpl implements ICompanyDao {

  private final Logger logger = LoggerFactory.getLogger(CompanyDaoMySQLImpl.class);
  
  /**
   * {@inheritDoc}
   */
  @Override
  public List<Company> getCompanies() {

    Connection conn = MySQLUtils.getConnection();

    final String query = "SELECT id, name FROM company";
    ResultSet results;
    List<Company> companies = new ArrayList<Company>();

    try {
      Statement stmt = conn.createStatement();
      results = stmt.executeQuery(query);

      while (results.next()) {
        long id = results.getLong("id");
        String name = results.getString("name");

        Company company = new Company.Builder().id(id).name(name).build();

        companies.add(company);

      }

    } catch (SQLException e) {
      logger.error("SQLException: {}", e);

    } finally {
      MySQLUtils.closeConnection(conn);
    }

    return companies;

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Company findById(long id) {

    Company company = null;
    Connection conn = MySQLUtils.getConnection();

    String query = "SELECT name FROM company WHERE id = ?";
    ResultSet results;

    try {
      PreparedStatement ps = conn.prepareStatement(query);
      ps.setLong(1, id);

      results = ps.executeQuery();
      if (results.first()) {

        String name = results.getString("name");

        company = new Company.Builder().id(id).name(name).build();
      }

    } catch (SQLException e) {
      logger.error("SQLException: {}", e);

    } finally {
      MySQLUtils.closeConnection(conn);
    }

    return company;

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public SearchWrapper<Company> getCompanies(long offset, long nbRequested) {
    SearchWrapper<Company> searchWrapper = new SearchWrapper<Company>();
    List<Company> companies = new ArrayList<Company>();

    if (offset < 0 || nbRequested < 0) {
      searchWrapper.setResults(companies);
      searchWrapper.setCurrentPage(0);
      searchWrapper.setTotalPage(0);
      searchWrapper.setTotalQueryCount(0);

      return searchWrapper;
    }

    String query = "SELECT id, name FROM company LIMIT ?, ?";
    String countQuery = "SELECT COUNT(id) AS count FROM company";

    Connection conn = MySQLUtils.getConnection();

    try {

      Statement stmt = conn.createStatement();
      ResultSet countResult = stmt.executeQuery(countQuery);
      countResult.first();
      searchWrapper.setTotalQueryCount(countResult.getLong("count"));

      long currentPage = (long) Math.ceil(offset * 1.0 / AppSettings.NB_RESULTS_PAGE) + 1;
      searchWrapper.setCurrentPage(currentPage);

      long totalPage = (long) Math.ceil(searchWrapper.getTotalQueryCount() * 1.0
          / AppSettings.NB_RESULTS_PAGE);
      searchWrapper.setTotalPage(totalPage);

      PreparedStatement ps = conn.prepareStatement(query);
      ps.setLong(1, offset);
      ps.setLong(2, nbRequested);
      ResultSet rs = ps.executeQuery();

      while (rs.next()) {
        long id = rs.getLong("id");
        String name = rs.getString("name");

        Company company = new Company.Builder().id(id).name(name).build();

        companies.add(company);

      }

      searchWrapper.setResults(companies);

    } catch (SQLException e) {
      logger.error("SQLException: {}", e);
    } finally {
      MySQLUtils.closeConnection(conn);
    }

    return searchWrapper;
  }

  @Override
  public void create(Company company) {
    // Not implemented
    logger.warn("Call to an unimplemented method");
  }

}
