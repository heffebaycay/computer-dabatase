package fr.heffebaycay.cdb.service.impl;

import java.sql.Connection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.heffebaycay.cdb.dao.ICompanyDao;
import fr.heffebaycay.cdb.dao.IComputerDao;
import fr.heffebaycay.cdb.dao.manager.DaoManager;
import fr.heffebaycay.cdb.model.Company;
import fr.heffebaycay.cdb.service.ICompanyService;
import fr.heffebaycay.cdb.wrapper.SearchWrapper;

public class CompanyServiceMockImpl implements ICompanyService {

  private static final Logger LOGGER = LoggerFactory.getLogger(CompanyServiceMockImpl.class);
  
  ICompanyDao companyDao;
  IComputerDao computerDao;
  
  public CompanyServiceMockImpl() {
    companyDao = DaoManager.INSTANCE.getCompanyDao();
    computerDao = DaoManager.INSTANCE.getComputerDao();
  }
  
  public CompanyServiceMockImpl(ICompanyDao companyDao) {
    this.companyDao = companyDao;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<Company> findAll() {
    LOGGER.debug("Call to findAll()");
    
    Connection conn = DaoManager.INSTANCE.getConnection();
    List<Company> companies = companyDao.findAll(conn);
    DaoManager.INSTANCE.closeConnection(conn);
    
    return companies;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Company findById(long id) {
    LOGGER.debug("Call to findById()");
    
    Connection conn = DaoManager.INSTANCE.getConnection();
    Company company = companyDao.findById(id, conn);
    DaoManager.INSTANCE.closeConnection(conn);
    
    return company;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void create(Company company) {
    LOGGER.debug("Call to create()");
    
    Connection conn = DaoManager.INSTANCE.getConnection();
    companyDao.create(company, conn);
    DaoManager.INSTANCE.closeConnection(conn);
    
    
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public SearchWrapper<Company> findAll(long offset, long nbRequested) {
    LOGGER.debug("Call to findAll()");
    
    Connection conn = DaoManager.INSTANCE.getConnection();
    SearchWrapper<Company> companies = companyDao.findAll(offset, nbRequested, conn);
    DaoManager.INSTANCE.closeConnection(conn);
    
    return companies;
  }

  @Override
  public void remove(long id) {
    LOGGER.debug("Call to remove()");
    
    Connection conn = DaoManager.INSTANCE.getConnection();
    
    DaoManager.INSTANCE.startTransaction(conn);
    
    // Remove computers linked to company X
    int nbComputers = computerDao.removeForCompany(id, conn);
    
    // Remove company X
    int nbCompany = companyDao.remove(id, conn);
    
    LOGGER.debug(String.format("Removed %d computers and %d company", nbComputers, nbCompany));
    
    DaoManager.INSTANCE.endTransaction(conn);
    
  }
  
  

}
