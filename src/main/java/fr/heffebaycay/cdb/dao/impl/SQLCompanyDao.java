package fr.heffebaycay.cdb.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import fr.heffebaycay.cdb.dao.ICompanyDao;
import fr.heffebaycay.cdb.dao.exception.DaoException;
import fr.heffebaycay.cdb.dao.impl.mapper.CompanyMySQLRowMapper;
import fr.heffebaycay.cdb.model.Company;
import fr.heffebaycay.cdb.model.CompanyPageRequest;
import fr.heffebaycay.cdb.util.CompanySortCriteria;
import fr.heffebaycay.cdb.util.SortOrder;
import fr.heffebaycay.cdb.wrapper.SearchWrapper;

@Repository
public class SQLCompanyDao implements ICompanyDao {

  private static final Logger LOGGER = LoggerFactory.getLogger(SQLCompanyDao.class);

  
  private JdbcTemplate jdbcTemplate;
    
  @Autowired
  public SQLCompanyDao(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<Company> findAll() throws DaoException {

    final String query = "SELECT id, name FROM company";
    List<Company> companies = jdbcTemplate.query(query, new Object[] { }, new CompanyMySQLRowMapper());

    return companies;

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Company findById(long id) throws DaoException {
    
    Company company = null;

    String query = "SELECT id, name FROM company WHERE id = ?";
    
    try {
      company = jdbcTemplate.queryForObject(query, new CompanyMySQLRowMapper(), id);
    } catch(IncorrectResultSizeDataAccessException e) {
      LOGGER.warn("findById(): No result for id {}: {}", id, e);
      throw new DaoException(e);
    }
    
    return company;

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public SearchWrapper<Company> findAll(CompanyPageRequest request) throws DaoException {
    
    if(request == null) {
      throw new DaoException("CompanyPageRequest parameter cannot be null");
    }
    
    SearchWrapper<Company> searchWrapper = new SearchWrapper<Company>();
    List<Company> companies = new ArrayList<Company>();
    
    searchWrapper.setSortOrder(request.getSortOrder().getName());
    searchWrapper.setSortCriterion(request.getSortCriterion().getName());

    if (request.getOffset() < 0 || request.getNbRequested() <= 0) {
      searchWrapper.setResults(companies);
      searchWrapper.setCurrentPage(0);
      searchWrapper.setTotalPage(0);
      searchWrapper.setTotalCount(0);

      return searchWrapper;
    }

    String orderPart = generateOrderPart("c", request.getSortCriterion(), request.getSortOrder());

    String query = "SELECT c.id, c.name FROM company AS c ORDER BY " + orderPart + " LIMIT ?, ?";
    String countQuery = "SELECT COUNT(c.id) AS count FROM company AS c ORDER BY " + orderPart;

    // Total count
    Long count;
    try {
      count = jdbcTemplate.queryForObject(countQuery, Long.class);
    } catch(IncorrectResultSizeDataAccessException e) {
      LOGGER.warn("findAll(request): Incorrect countQuery result: {}", e);
      throw new DaoException(e);
    }
    searchWrapper.setTotalCount(count);
    
    // Current Page
    long currentPage = (long) Math.ceil(request.getOffset() * 1.0 / request.getNbRequested()) + 1;
    searchWrapper.setCurrentPage(currentPage);
    
    // Total page
    long totalPage = (long) Math.ceil(searchWrapper.getTotalCount() * 1.0 / request.getNbRequested());
    searchWrapper.setTotalPage(totalPage);
    
    companies = jdbcTemplate.query(query, new CompanyMySQLRowMapper(), request.getOffset(), request.getNbRequested());
    searchWrapper.setResults(companies);

    return searchWrapper;
  }

  @Override
  public void create(Company company) throws DaoException {
    String query = "INSERT INTO company(name) VALUES(?)";
    
    try {
      jdbcTemplate.update(query, company.getName());
    } catch(DataAccessException e) {
      LOGGER.warn("create(): failed to create Company: {}", e);
      throw new DaoException("create(): failed to create Company", e);
    }
    
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int remove(long id) throws DaoException {
    
    String removeCompanyQuery = "DELETE FROM company WHERE id = ?";
    
    int nbCompany;
    try {
      nbCompany = jdbcTemplate.update(removeCompanyQuery, id);
    } catch(DataAccessException e) {
      LOGGER.warn("remove(): failed to remove Company: {}", e);
      throw new DaoException("remove(): failed to remove Company", e);
    }
    
    return nbCompany;
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
  public SearchWrapper<Company> findByName(CompanyPageRequest request) throws DaoException {
    
    if(request == null) {
      throw new DaoException("CompanyPageRequest parameter cannot be null");
    }
    
    SearchWrapper<Company> searchWrapper = new SearchWrapper<Company>();
    List<Company> companies = new ArrayList<Company>();
    
    searchWrapper.setSearchQuery(request.getSearchQuery());
    searchWrapper.setSortOrder(request.getSortOrder().getName());
    searchWrapper.setSortCriterion(request.getSortCriterion().getName());
    

    if (request.getSearchQuery() == null || request.getSearchQuery().isEmpty() || request.getOffset() < 0 || request.getNbRequested() <= 0) {
      searchWrapper.setResults(companies);
      searchWrapper.setCurrentPage(0);
      searchWrapper.setTotalPage(0);
      searchWrapper.setTotalCount(0);

      return searchWrapper;
    }

    

    String orderPart = generateOrderPart("c", request.getSortCriterion(), request.getSortOrder());

    String query = "SELECT c.id, c.name FROM company AS c WHERE c.name LIKE ? ORDER BY "
        + orderPart + " LIMIT ?, ?";
    String countQuery = "SELECT COUNT(c.id) AS count FROM company AS c WHERE c.name LIKE ?";

    // Setting up search keyword
    String searchKeyword = request.getSearchQuery().replace("%", "\\%");
    searchKeyword = String.format("%%%s%%", searchKeyword);
    LOGGER.debug(String.format("findByName(): Keyword={%s}", searchKeyword));
    
    Long count;
    try {
      count = jdbcTemplate.queryForObject(countQuery, Long.class, searchKeyword);
      searchWrapper.setTotalCount(count);
      
      // Current Page
      long currentPage = (long) Math.ceil(request.getOffset() * 1.0 / request.getNbRequested()) + 1;
      searchWrapper.setCurrentPage(currentPage);
      
      // Total Page
      long totalPage = (long) Math.ceil(searchWrapper.getTotalCount() * 1.0 / request.getNbRequested());
      searchWrapper.setTotalPage(totalPage);
      
      companies = jdbcTemplate.query(query, new CompanyMySQLRowMapper(), searchKeyword, request.getOffset(), request.getNbRequested());
      searchWrapper.setResults(companies);
    } catch(IncorrectResultSizeDataAccessException e) {
      LOGGER.warn("findbyName(): Incorrect countQuery result: {}", e);
      throw new DaoException("findByName(): Incorrect countQuery result", e);
    } catch(DataAccessException e) {
      LOGGER.warn("findByName(): Failed to issue DB query: {}", e);
      throw new DaoException("findByName(): Failed to issue DB query", e);
    }
    
    return searchWrapper;
  }

}
