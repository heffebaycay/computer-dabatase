package fr.heffebaycay.cdb.service.manager;

import fr.heffebaycay.cdb.service.ICompanyService;
import fr.heffebaycay.cdb.service.impl.CompanyServiceMockImpl;

public enum ServiceManager {

	INSTANCE;
	
	private ICompanyService companyService;
	
	private ServiceManager() {
		companyService = new CompanyServiceMockImpl();
	}
	
	/**
	 * Returns an instance of CompanyService
	 * 
	 * @return
	 */
	public ICompanyService getCompanyService() {
		return companyService;
	}
	
}
