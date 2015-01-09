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
	 * @param id The Id of the Computer object that should be returned
	 * @return An instance of Computer or null if there's no match
	 */
	Computer findById(long id);
	
	
	/**
	 * This method removes a Computer from the DataSource based on its Id
	 * 
	 * @param id Id of the computer object to be removed
	 * @return boolean indicating success (true) or failure (false) of the removal operation
	 */
	boolean remove(long id);
	
	/**
	 * Create a Computer in the DataSource based on an instance of Computer
	 * 
	 * @param computer The computer object that should be persisted
	 */
	void create(Computer computer);
	
	
	/**
	 * Update an already existing Computer in the DataSource
	 * 
	 * @param computer The computer object that should be updated
	 */
	void update(Computer computer);
	

}
