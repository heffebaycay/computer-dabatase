package fr.heffebaycay.cdb.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import fr.heffebaycay.cdb.dao.IComputerDao;
import fr.heffebaycay.cdb.dao.exception.DaoException;
import fr.heffebaycay.cdb.dao.impl.mapper.ComputerMySQLRowMapper;
import fr.heffebaycay.cdb.dao.manager.DaoManager;
import fr.heffebaycay.cdb.model.Computer;
import fr.heffebaycay.cdb.model.ComputerPageRequest;
import fr.heffebaycay.cdb.util.ComputerSortCriteria;
import fr.heffebaycay.cdb.util.SortOrder;
import fr.heffebaycay.cdb.wrapper.SearchWrapper;

@Repository
public class SQLComputerDao implements IComputerDao {

  private static final Logger LOGGER = LoggerFactory.getLogger(SQLComputerDao.class);

  private JdbcTemplate        jdbcTemplate;

  @Autowired
  public SQLComputerDao(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<Computer> findAll() throws DaoException {

    String query = "SELECT c.id, c.name, c.introduced, c.discontinued, cp.id AS cpId, cp.name AS cpName FROM computer AS c LEFT JOIN company AS cp ON c.company_id = cp.id";

    List<Computer> computers = null;

    try {
      jdbcTemplate.query(query, new ComputerMySQLRowMapper());
    } catch (DataAccessException e) {
      LOGGER.warn("findAll(): Failed to query DB: {}", e);
      throw new DaoException("findAll(): Failed to query DB", e);
    }

    return computers;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Computer findById(long id) throws DaoException {
    Computer computer = null;

    String query = "SELECT c.id, c.name, c.introduced, c.discontinued, cp.id AS cpId, cp.name AS cpName FROM computer AS c LEFT JOIN company AS cp ON c.company_id = cp.id WHERE c.id = ?";

    try {
      computer = jdbcTemplate.queryForObject(query, new ComputerMySQLRowMapper(), id);
    } catch (IncorrectResultSizeDataAccessException e) {
      LOGGER.warn("findById(): Invalid number of result: {}", e);
      throw new DaoException("findById(): Invalid number of result", e);
    } catch (DataAccessException e) {
      LOGGER.warn("findById(): Failed to query DB: {}", e);
      throw new DaoException("findById(): Failed to query DB");
    }

    return computer;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean remove(long id) throws DaoException {
    String query = "DELETE FROM computer WHERE id = ?";

    try {
      jdbcTemplate.update(query, id);
      return true;
    } catch (DataAccessException e) {
      LOGGER.warn("remove(): Failed to query DB: {}", e);
      throw new DaoException("remove(): Failed to query DB", e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public long create(Computer computer) throws DaoException {

    if (computer == null) {
      throw new IllegalArgumentException("'computer' argument cannot be null");
    }

    String query = "INSERT INTO computer(name, introduced, discontinued, company_id) VALUES(?,?,?,?)";

    KeyHolder holder = new GeneratedKeyHolder();
    PreparedStatementCreator creator = new PreparedStatementCreator() {

      @Override
      public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
        PreparedStatement ps = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

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

        return ps;
      }
    };

    try {
      jdbcTemplate.update(creator, holder);
    } catch (DataAccessException e) {
      LOGGER.warn("create(): Failed to query DB: {}", e);
      throw new DaoException("create(): Failed to query DB", e);
    }

    long newComputerId = holder.getKey().longValue();

    return newComputerId;

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void update(Computer computer) throws DaoException {
    if (computer == null) {
      throw new IllegalArgumentException("'computer' argument cannot be null");
    }

    String query = "UPDATE computer SET name=?, introduced=?, discontinued=?, company_id=? WHERE id=?";

    Long companyId = null;
    if (computer.getCompany() != null) {
      companyId = computer.getCompany().getId();
    }

    Timestamp introduced, discontinued;
    if (computer.getIntroduced() != null) {
      introduced = Timestamp.valueOf(computer.getIntroduced());
    } else {
      introduced = null;
    }

    if (computer.getDiscontinued() != null) {
      discontinued = Timestamp.valueOf(computer.getDiscontinued());
    } else {
      discontinued = null;
    }

    try {
      jdbcTemplate.update(query, computer.getName(), introduced, discontinued, companyId,
          computer.getId());
    } catch (DataAccessException e) {
      LOGGER.warn("update(): Failed to query DB: {}", e);
      throw new DaoException("update(): Failed to query DB", e);
    }

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int removeForCompany(long companyId) throws DaoException {
    String removeForCompanySQL = "DELETE FROM computer WHERE company_id = ?";

    int computersAffected;
    try {
      computersAffected = jdbcTemplate.update(removeForCompanySQL, companyId);
    } catch (DataAccessException e) {
      LOGGER.warn("removeForCompany(): Failed to query DB: {}", e);
      throw new DaoException("removeForCompany(): Failed to query DB, e");
    }

    return computersAffected;

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public SearchWrapper<Computer> findByName(ComputerPageRequest request) throws DaoException {
    if (request == null) {
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

    if (ComputerSortCriteria.COMPANY_NAME.equals(request.getSortCriterion())) {
      orderPart = generateOrderPart("cp", request.getSortCriterion(), request.getSortOrder());
    } else {
      orderPart = generateOrderPart("c", request.getSortCriterion(), request.getSortOrder());
    }

    String query = "SELECT c.id, c.name, c.introduced, c.discontinued, cp.id AS cpId, cp.name AS cpName FROM computer AS c LEFT JOIN company AS cp ON c.company_id = cp.id WHERE c.name LIKE ? OR cp.name LIKE ? ORDER BY "
        + orderPart + " LIMIT ?, ?";
    String countQuery = "SELECT COUNT(c.id) AS count FROM computer AS c LEFT JOIN company AS cp ON c.company_id = cp.id WHERE c.name LIKE ? OR cp.name LIKE ?";

    // Setting up Search keyword
    // Escaping '%' character
    String searchKeyword = request.getSearchQuery().replace("%", "\\%");
    searchKeyword = String.format("%%%s%%", searchKeyword);
    LOGGER.debug("Search keyword: " + searchKeyword);

    Long count;
    try {
      count = jdbcTemplate.queryForObject(countQuery, Long.class, searchKeyword, searchKeyword);
      searchWrapper.setTotalCount(count);

      // Current page
      long currentPage = (long) Math.ceil(request.getOffset() * 1.0 / request.getNbRequested()) + 1;
      searchWrapper.setCurrentPage(currentPage);

      // Total page
      long totalPage = (long) Math.ceil(searchWrapper.getTotalCount() * 1.0
          / request.getNbRequested());
      searchWrapper.setTotalPage(totalPage);

      computers = jdbcTemplate.query(query, new ComputerMySQLRowMapper(), searchKeyword,
          searchKeyword, request.getOffset(), request.getNbRequested());
      searchWrapper.setResults(computers);
    } catch (IncorrectResultSizeDataAccessException e) {
      LOGGER.warn("findByName(): Invalid number of result: {}", e);
      throw new DaoException("findByName(): Invalid number of result", e);
    } catch (DataAccessException e) {
      LOGGER.warn("findByName(): Failed to query DB: {}", e);
      throw new DaoException("findByName(): Failed to query DB", e);
    }

    return searchWrapper;
  }

  @Override
  public SearchWrapper<Computer> findAll(ComputerPageRequest request) throws DaoException {

    if (request == null) {
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
    String countQuery = "SELECT COUNT(c.id) AS count FROM computer AS c LEFT JOIN company AS cp ON c.company_id = cp.id";

    Long count;
    try {
      // First, we need to know exactly how many results there are, aggregated
      count = jdbcTemplate.queryForObject(countQuery, Long.class);
      searchWrapper.setTotalCount(count);

      // Current page
      long currentPage = (long) Math.ceil(request.getOffset() * 1.0 / request.getNbRequested()) + 1;
      searchWrapper.setCurrentPage(currentPage);

      // Total page
      long totalPage = (long) Math.ceil(searchWrapper.getTotalCount() * 1.0
          / request.getNbRequested());
      searchWrapper.setTotalPage(totalPage);

      computers = jdbcTemplate.query(query, new ComputerMySQLRowMapper(), request.getOffset(),
          request.getNbRequested());
      searchWrapper.setResults(computers);

    } catch (IncorrectResultSizeDataAccessException e) {
      LOGGER.warn("findAll(): Invalid number of result: {}", e);
      throw new DaoException("findAll(): Invalid number of result", e);

    } catch (DataAccessException e) {
      LOGGER.warn("findAll(): Failed to query DB: {}", e);
      throw new DaoException("findAll(): Failed to query DB", e);
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
