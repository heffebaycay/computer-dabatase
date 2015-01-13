package fr.heffebaycay.cdb.service.manager;

import fr.heffebaycay.cdb.service.ICompanyService;
import fr.heffebaycay.cdb.service.IComputerService;
import fr.heffebaycay.cdb.service.impl.CompanyServiceMockImpl;
import fr.heffebaycay.cdb.service.impl.ComputerServiceMockImpl;

public enum ServiceManager {

	INSTANCE;
	
	private ICompanyService companyService;
	private IComputerService computerService;
	
	private ServiceManager() {
		companyService = new CompanyServiceMockImpl();
		computerService = new ComputerServiceMockImpl();
	}
	
	/**
	 * Returns an instance of CompanyService
	 * 
	 * @return
	 */
	public ICompanyService getCompanyService() {
		return companyService;
	}
	
	/**
	 * Returns an instance of ComputerService
	 * 
	 * @return
	 */
	public IComputerService getComputerService() {
		return computerService;
	}
	
}
