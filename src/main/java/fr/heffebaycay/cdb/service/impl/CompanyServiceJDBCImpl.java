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
import fr.heffebaycay.cdb.model.CompanyPageRequest;
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

    List<Company> companies = null;
    try {
      companies = companyDao.findAll();
    } catch (DaoException e) {
      LOGGER.warn("findAll(): DaoException: ", e);
    } finally {
      DaoManager.INSTANCE.closeConnection();
    }

    return companies;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Company findById(long id) {
    LOGGER.debug("Call to findById()");

    Company company = null;
    try {
      company = companyDao.findById(id);
    } catch (DaoException e) {
      LOGGER.warn("findById(): DaoException: ", e);
    } finally {
      DaoManager.INSTANCE.closeConnection();
    }

    return company;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void create(Company company) {
    LOGGER.debug("Call to create()");

    try {
      companyDao.create(company);
    } catch (DaoException e) {
      LOGGER.warn("create(): DaoException: ", e);
    } finally {
      DaoManager.INSTANCE.closeConnection();
    }

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public SearchWrapper<Company> findAll(CompanyPageRequest request) {
    LOGGER.debug("Call to findAll()");

    SearchWrapper<Company> companies = null;
    try {
      companies = companyDao.findAll(request);
    } catch (DaoException e) {
      LOGGER.warn("findAll(): DaoException", e);
    } finally {
      DaoManager.INSTANCE.closeConnection();
    }

    return companies;
  }

  @Override
  public void remove(long id) {
    LOGGER.debug("Call to remove()");

    DaoManager.INSTANCE.startTransaction();

    int nbComputers = -1;
    int nbCompany = -1;

    try {
      // Remove computers linked to company X
      nbComputers = computerDao.removeForCompany(id);

      // Remove company X
      nbCompany = companyDao.remove(id);

      DaoManager.INSTANCE.commitTransaction();

    } catch (DaoException e) {
      DaoManager.INSTANCE.rollbackTransaction();

    } finally {
      DaoManager.INSTANCE.endTransaction();
    }

    LOGGER.debug(String.format("Removed %d computers and %d company", nbComputers, nbCompany));

  }

  @Override
  public SearchWrapper<Company> findByName(CompanyPageRequest request) {
    LOGGER.debug("Call to findByName()");

    SearchWrapper<Company> companies = null;
    try {
      companies = companyDao.findByName(request);
    } catch (DaoException e) {
      LOGGER.warn("findByName(): DaoException: ", e);
    } finally {
      DaoManager.INSTANCE.closeConnection();
    }
    
    return companies;
  }

}
