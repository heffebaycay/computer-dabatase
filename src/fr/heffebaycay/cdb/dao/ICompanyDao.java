package fr.heffebaycay.cdb.dao;

import fr.heffebaycay.cdb.model.Company;

import java.util.List;

public interface ICompanyDao {

	
	/**
	 * This method returns the list of all companies stored in the DataSource.
	 * 
	 * @return The list of all Companies
	 */
	List<Company> getCompanies();
	
	/**
	 * Find a Company from the DataSource
	 * @param id
	 * @return
	 */
	Company findById(long id);
	
	/**
	 * Create a Company in the DataSource based on an instance of Company
	 * 
	 * @param company The company object that should be created
	 */
	void create(Company company);
}
