package fr.heffebaycay.cdb.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import fr.heffebaycay.cdb.dao.ICompanyDao;
import fr.heffebaycay.cdb.dao.exception.DaoException;
import fr.heffebaycay.cdb.model.Company;
import fr.heffebaycay.cdb.model.CompanyPageRequest;
import fr.heffebaycay.cdb.util.CompanySortCriteria;
import fr.heffebaycay.cdb.util.SortOrder;
import fr.heffebaycay.cdb.wrapper.SearchWrapper;

@Repository
public class SQLCompanyDao implements ICompanyDao {

  private static final Logger LOGGER = LoggerFactory.getLogger(SQLCompanyDao.class);

  @Autowired
  private SessionFactory      sessionFactory;

  public SQLCompanyDao() {

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<Company> findAll() {
    Session session = sessionFactory.getCurrentSession();

    @SuppressWarnings("unchecked")
    List<Company> companies = session.createQuery("FROM Company").list();

    return companies;

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Company findById(long id) {
    Session session = sessionFactory.getCurrentSession();

    Company company = (Company) session
        .createQuery("FROM Company as company WHERE company.id = :id")
        .setLong("id", id)
        .uniqueResult();

    return company;

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public SearchWrapper<Company> findAll(CompanyPageRequest request) throws DaoException {

    if (request == null) {
      throw new DaoException("CompanyPageRequest parameter cannot be null");
    }

    SearchWrapper<Company> searchWrapper = new SearchWrapper<Company>();

    searchWrapper.setSortOrder(request.getSortOrder().getName());
    searchWrapper.setSortCriterion(request.getSortCriterion().getName());

    if (request.getOffset() < 0 || request.getNbRequested() <= 0) {
      searchWrapper.setResults(new ArrayList<Company>());
      searchWrapper.setCurrentPage(0);
      searchWrapper.setTotalPage(0);
      searchWrapper.setTotalCount(0);

      return searchWrapper;
    }

    Session session = sessionFactory.getCurrentSession();

    String orderPart = generateOrderPart("company", request.getSortCriterion(),
        request.getSortOrder());

    String query = "FROM Company as company ORDER BY " + orderPart;
    String countQuery = "SELECT count(*) FROM Company";

    // Total count
    Long count = (Long) session.createQuery(countQuery).iterate().next();
    searchWrapper.setTotalCount(count);

    // Current Page
    long currentPage = (long) Math.ceil(request.getOffset() * 1.0 / request.getNbRequested()) + 1;
    searchWrapper.setCurrentPage(currentPage);

    // Total page
    long totalPage = (long) Math.ceil(searchWrapper.getTotalCount() * 1.0
        / request.getNbRequested());
    searchWrapper.setTotalPage(totalPage);

    @SuppressWarnings("unchecked")
    List<Company> companies = session.createQuery(query)
        .setFirstResult(request.getOffset().intValue())
        .setMaxResults(request.getNbRequested().intValue())
        .list();

    searchWrapper.setResults(companies);

    return searchWrapper;
  }

  @Override
  public void create(Company company) {
    Session session = sessionFactory.getCurrentSession();

    session.save(company);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean remove(long id) {
    Session session = sessionFactory.getCurrentSession();

    Company company = (Company) session.get(Company.class, id);

    if (company != null) {
      session.delete(company);
      return true;
    } else {
      return false;
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
  public SearchWrapper<Company> findByName(CompanyPageRequest request) throws DaoException {

    if (request == null) {
      throw new DaoException("CompanyPageRequest parameter cannot be null");
    }

    SearchWrapper<Company> searchWrapper = new SearchWrapper<Company>();

    searchWrapper.setSearchQuery(request.getSearchQuery());
    searchWrapper.setSortOrder(request.getSortOrder().getName());
    searchWrapper.setSortCriterion(request.getSortCriterion().getName());

    if (request.getSearchQuery() == null || request.getSearchQuery().isEmpty()
        || request.getOffset() < 0 || request.getNbRequested() <= 0) {
      searchWrapper.setResults(new ArrayList<Company>());
      searchWrapper.setCurrentPage(0);
      searchWrapper.setTotalPage(0);
      searchWrapper.setTotalCount(0);

      return searchWrapper;
    }

    Session session = sessionFactory.getCurrentSession();

    String orderPart = generateOrderPart("company", request.getSortCriterion(),
        request.getSortOrder());

    String query = "FROM Company as company WHERE company.name LIKE :keyword ORDER BY " + orderPart;
    String countQuery = "SELECT count(*) FROM Company AS company WHERE company.name LIKE :keyword";

    // Setting up search keyword
    String searchKeyword = request.getSearchQuery().replace("%", "\\%");
    searchKeyword = String.format("%%%s%%", searchKeyword);
    LOGGER.debug(String.format("findByName(): Keyword={%s}", searchKeyword));

    Long count = (Long) session.createQuery(countQuery)
        .setString("keyword", searchKeyword)
        .iterate()
        .next();
    searchWrapper.setTotalCount(count);

    // Current Page
    long currentPage = (long) Math.ceil(request.getOffset() * 1.0 / request.getNbRequested()) + 1;
    searchWrapper.setCurrentPage(currentPage);

    // Total Page
    long totalPage = (long) Math.ceil(searchWrapper.getTotalCount() * 1.0
        / request.getNbRequested());
    searchWrapper.setTotalPage(totalPage);

    @SuppressWarnings("unchecked")
    List<Company> companies = session.createQuery(query)
        .setString("keyword", searchKeyword)
        .setFirstResult(request.getOffset().intValue())
        .setMaxResults(request.getNbRequested().intValue())
        .list();

    searchWrapper.setResults(companies);

    return searchWrapper;
  }

}
