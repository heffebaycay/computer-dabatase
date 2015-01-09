package fr.heffebaycay.cdb.dao.manager;

import fr.heffebaycay.cdb.dao.ICompanyDao;
import fr.heffebaycay.cdb.dao.impl.CompanyDaoMockImpl;

public enum DaoManager {
	
	INSTANCE;
	
	private ICompanyDao companyDao;
	
	private DaoManager() {
		// companyDao
		companyDao = new CompanyDaoMockImpl();
	}
	
	public ICompanyDao getCompanyDao() {
		return companyDao;
	}
	

}
