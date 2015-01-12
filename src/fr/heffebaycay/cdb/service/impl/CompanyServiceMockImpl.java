package fr.heffebaycay.cdb.service.impl;

import java.util.List;

import fr.heffebaycay.cdb.dao.ICompanyDao;
import fr.heffebaycay.cdb.dao.manager.DaoManager;
import fr.heffebaycay.cdb.model.Company;
import fr.heffebaycay.cdb.service.ICompanyService;
import fr.heffebaycay.cdb.wrapper.SearchWrapper;

public class CompanyServiceMockImpl implements ICompanyService {

  ICompanyDao companyDao;

  public CompanyServiceMockImpl() {
    companyDao = DaoManager.INSTANCE.getCompanyDao();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<Company> getCompanies() {
    return companyDao.getCompanies();
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
  public SearchWrapper<Company> getCompanies(long offset, long nbRequested) {
    return companyDao.getCompanies(offset, nbRequested);
  }

}
