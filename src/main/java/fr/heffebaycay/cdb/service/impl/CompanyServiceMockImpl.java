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
    return companyDao.findAll();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Company findById(long id) {
    return companyDao.findById(id);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void create(Company company) {
    companyDao.create(company);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public SearchWrapper<Company> findAll(long offset, long nbRequested) {
    return companyDao.findAll(offset, nbRequested);
  }

  @Override
  public void remove(long id) {
    
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
