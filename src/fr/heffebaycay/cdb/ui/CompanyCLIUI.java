package fr.heffebaycay.cdb.ui;

import fr.heffebaycay.cdb.model.Company;
import fr.heffebaycay.cdb.service.ICompanyService;
import fr.heffebaycay.cdb.service.manager.ServiceManager;

import java.util.List;

public class CompanyCLIUI {
	
	ICompanyService companyService;
	
	
	public CompanyCLIUI() {
		companyService = ServiceManager.INSTANCE.getCompanyService();
	}
	
	public void printCompanies() {
		
		List<Company> companies = companyService.getCompanies();
		
		for(Company c : companies) {
			System.out.println(c);
		}
		
	}
	

}
