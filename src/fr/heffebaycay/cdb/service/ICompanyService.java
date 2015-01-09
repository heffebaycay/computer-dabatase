package fr.heffebaycay.cdb.service;

import java.util.List;

import fr.heffebaycay.cdb.model.Company;

public interface ICompanyService {
	
	
	List<Company> getCompanies();
	
	
	Company findById(long id);
	

}
