package fr.heffebaycay.cdb.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import fr.heffebaycay.cdb.dao.IComputerDao;
import fr.heffebaycay.cdb.dao.exception.DaoException;
import fr.heffebaycay.cdb.model.Computer;
import fr.heffebaycay.cdb.model.ComputerPageRequest;
import fr.heffebaycay.cdb.util.ComputerSortCriteria;
import fr.heffebaycay.cdb.util.SortOrder;
import fr.heffebaycay.cdb.wrapper.SearchWrapper;

@Repository
public class SQLComputerDao implements IComputerDao {

  private static final Logger LOGGER = LoggerFactory.getLogger(SQLComputerDao.class);

  @Autowired
  private SessionFactory      sessionFactory;

  public SQLComputerDao() {

  }

  public void setSessionFactory(SessionFactory sessionFactory) {
    this.sessionFactory = sessionFactory;
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  @Override
  public List<Computer> findAll() throws DaoException {

    Session session = sessionFactory.getCurrentSession();

    List<Computer> computers = session.createQuery("from Computer as computer").list();

    return computers;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Computer findById(long id) throws DaoException {
    Session session = sessionFactory.getCurrentSession();

    Query query = session.createQuery("from Computer as comp where comp.id = :id");
    query.setLong("id", id);

    Computer computer = (Computer) query.uniqueResult();

    return computer;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean remove(long id) throws DaoException {

    Session session = sessionFactory.getCurrentSession();

    Computer computer = (Computer) session.get(Computer.class, id);

    if (computer != null) {
      session.delete(computer);
      return true;
    } else {
      return false;
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
    Session session = sessionFactory.getCurrentSession();

    long computerId = (long) session.save(computer);

    return computerId;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void update(Computer computer) throws DaoException {
    if (computer == null) {
      throw new IllegalArgumentException("'computer' argument cannot be null");
    }

    Session session = sessionFactory.getCurrentSession();

    session.update(computer);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int removeForCompany(long companyId) throws DaoException {
    Session session = sessionFactory.getCurrentSession();

    int computersAffected;

    Query q = session
        .createQuery("DELETE FROM Computer as comp WHERE comp.company.id = :companyId");
    q.setLong("companyId", companyId);
    computersAffected = q.executeUpdate();

    return computersAffected;

  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
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

    Session session = sessionFactory.getCurrentSession();
    String orderPart;

    if (ComputerSortCriteria.COMPANY_NAME.equals(request.getSortCriterion())) {
      orderPart = generateOrderPart("company", request.getSortCriterion(), request.getSortOrder());
    } else {
      orderPart = generateOrderPart("computer", request.getSortCriterion(), request.getSortOrder());
    }

    String query = "FROM Computer as computer LEFT JOIN computer.company as company WHERE computer.name LIKE :keyWord OR company.name LIKE :keyWord ORDER BY "
        + orderPart;

    String countQuery = "SELECT COUNT(*) FROM Computer as computer LEFT JOIN computer.company as company WHERE computer.name LIKE :keyWord OR company.name LIKE :keyword";

    // Setting up Search keyword
    // Escaping '%' character
    String searchKeyword = request.getSearchQuery().replace("%", "\\%");
    searchKeyword = String.format("%%%s%%", searchKeyword);
    LOGGER.debug("Search keyword: " + searchKeyword);

    Long count = (Long) session.createQuery(countQuery).setString("keyWord", searchKeyword)
        .iterate().next();
    searchWrapper.setTotalCount(count);

    // Current page
    long currentPage = (long) Math.ceil(request.getOffset() * 1.0 / request.getNbRequested()) + 1;
    searchWrapper.setCurrentPage(currentPage);

    // Total page
    long totalPage = (long) Math.ceil(searchWrapper.getTotalCount() * 1.0
        / request.getNbRequested());
    searchWrapper.setTotalPage(totalPage);

    Query q = session.createQuery(query);
    q.setFirstResult(request.getOffset().intValue()).setMaxResults(
        request.getNbRequested().intValue());
    q.setString("keyWord", searchKeyword);

    computers = q.list();
    searchWrapper.setResults(computers);

    return searchWrapper;
  }

  @SuppressWarnings("unchecked")
  @Override
  public SearchWrapper<Computer> findAll(ComputerPageRequest request) throws DaoException {

    if (request == null) {
      throw new DaoException("PageRequest object cannot be null");
    }

    SearchWrapper<Computer> searchWrapper = new SearchWrapper<Computer>();
    List<Computer> computers;

    searchWrapper.setSortOrder(request.getSortOrder().getName());
    searchWrapper.setSortCriterion(request.getSortCriterion().getName());

    if (request.getOffset() < 0 || request.getNbRequested() <= 0) {
      searchWrapper.setResults(new ArrayList<Computer>());
      searchWrapper.setCurrentPage(0);
      searchWrapper.setTotalPage(0);
      searchWrapper.setTotalCount(0);

      return searchWrapper;
    }

    Session session = sessionFactory.getCurrentSession();

    String orderPart;

    if (ComputerSortCriteria.COMPANY_NAME.equals(request.getSortCriterion())) {
      orderPart = generateOrderPart("company", request.getSortCriterion(), request.getSortOrder());
    } else {
      orderPart = generateOrderPart("computer", request.getSortCriterion(), request.getSortOrder());
    }

    String query = "SELECT computer FROM Computer as computer LEFT JOIN computer.company as company ORDER BY "
        + orderPart;
    String countQuery = "SELECT count(*) FROM Computer as computer LEFT JOIN computer.company as company";

    Long count = ((Long) session.createQuery(countQuery).iterate().next());
    searchWrapper.setTotalCount(count);

    // Current page
    long currentPage = (long) Math.ceil(request.getOffset() * 1.0 / request.getNbRequested()) + 1;
    searchWrapper.setCurrentPage(currentPage);

    // Total page
    long totalPage = (long) Math.ceil(searchWrapper.getTotalCount() * 1.0
        / request.getNbRequested());
    searchWrapper.setTotalPage(totalPage);

    Query q = session.createQuery(query);
    q.setFirstResult(request.getOffset().intValue())
        .setMaxResults(request.getNbRequested().intValue());
    computers = (List<Computer>) q.list();
    searchWrapper.setResults(computers);

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
