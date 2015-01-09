package fr.heffebaycay.cdb.dao;

import java.util.List;

import fr.heffebaycay.cdb.model.Computer;

public interface IComputerDao {
	
	/**
	 * This method returns the list of all computers stored in the DataSource
	 * 
	 * @return
	 */
	List<Computer> getComputers();
	
	
	/**
	 * Find a given Computer from the DataSource
	 * 
	 * @param id
	 * @return An instance of Computer or null if there's no match
	 */
	Computer findById(long id);
	
	

}
