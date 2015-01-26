package fr.heffebaycay.cdb.service.impl;

import java.sql.Connection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.heffebaycay.cdb.dao.ICompanyDao;
import fr.heffebaycay.cdb.dao.IComputerDao;
import fr.heffebaycay.cdb.dao.exception.DaoException;
import fr.heffebaycay.cdb.dao.manager.DaoManager;
import fr.heffebaycay.cdb.model.Company;
import fr.heffebaycay.cdb.service.ICompanyService;
import fr.heffebaycay.cdb.util.CompanySortCriteria;
import fr.heffebaycay.cdb.util.SortOrder;
import fr.heffebaycay.cdb.wrapper.SearchWrapper;

public class CompanyServiceJDBCImpl implements ICompanyService {

  private static final Logger LOGGER = LoggerFactory.getLogger(CompanyServiceJDBCImpl.class);

  ICompanyDao                 companyDao;
  IComputerDao                computerDao;

  public CompanyServiceJDBCImpl() {
    companyDao = DaoManager.INSTANCE.getCompanyDao();
    computerDao = DaoManager.INSTANCE.getComputerDao();
  }

  public CompanyServiceJDBCImpl(ICompanyDao companyDao) {
    this.companyDao = companyDao;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<Company> findAll() {
    LOGGER.debug("Call to findAll()");

    Connection conn = DaoManager.INSTANCE.getConnection();
    List<Company> companies = null;
    try {
      companies = companyDao.findAll(conn);
    } catch (DaoException e) {
      LOGGER.warn("findAll(): DaoException: ", e);
    } finally {
      DaoManager.INSTANCE.closeConnection(conn);
    }

    return companies;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Company findById(long id) {
    LOGGER.debug("Call to findById()");

    Connection conn = DaoManager.INSTANCE.getConnection();
    Company company = null;
    try {
      company = companyDao.findById(id, conn);
    } catch (DaoException e) {
      LOGGER.warn("findById(): DaoException: ", e);
    } finally {
      DaoManager.INSTANCE.closeConnection(conn);
    }

    return company;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void create(Company company) {
    LOGGER.debug("Call to create()");

    Connection conn = DaoManager.INSTANCE.getConnection();
    try {
      companyDao.create(company, conn);
    } catch (DaoException e) {
      LOGGER.warn("create(): DaoException: ", e);
    } finally {
      DaoManager.INSTANCE.closeConnection(conn);
    }

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public SearchWrapper<Company> findAll(long offset, long nbRequested,
      CompanySortCriteria sortCriterion, SortOrder sortOrder) {
    LOGGER.debug("Call to findAll()");

    Connection conn = DaoManager.INSTANCE.getConnection();
    SearchWrapper<Company> companies = null;
    try {
      companies = companyDao.findAll(offset, nbRequested, sortCriterion, sortOrder, conn);
    } catch (DaoException e) {
      LOGGER.warn("findAll(): DaoException", e);
    } finally {
      DaoManager.INSTANCE.closeConnection(conn);
    }

    return companies;
  }

  @Override
  public void remove(long id) {
    LOGGER.debug("Call to remove()");

    Connection conn = DaoManager.INSTANCE.getConnection();

    DaoManager.INSTANCE.startTransaction(conn);

    int nbComputers = -1;
    int nbCompany = -1;

    try {
      // Remove computers linked to company X
      nbComputers = computerDao.removeForCompany(id, conn);

      // Remove company X
      nbCompany = companyDao.remove(id, conn);

      DaoManager.INSTANCE.commitTransaction(conn);

    } catch (DaoException e) {
      DaoManager.INSTANCE.rollbackTransaction(conn);

    } finally {
      DaoManager.INSTANCE.endTransaction(conn);
    }

    LOGGER.debug(String.format("Removed %d computers and %d company", nbComputers, nbCompany));

  }

  @Override
  public SearchWrapper<Company> findByName(String name, long offset, long nbRequested,
      CompanySortCriteria sortCriterion, SortOrder sortOrder) {
    LOGGER.debug("Call to findByName()");

    Connection conn = DaoManager.INSTANCE.getConnection();
    SearchWrapper<Company> companies = null;
    try {
      companies = companyDao.findByName(name, offset, nbRequested, sortCriterion, sortOrder, conn);
    } catch (DaoException e) {
      LOGGER.warn("findByName(): DaoException: ", e);
    } finally {
      DaoManager.INSTANCE.closeConnection(conn);
    }
    
    return companies;
  }

}
