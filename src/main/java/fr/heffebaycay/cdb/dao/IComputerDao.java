package fr.heffebaycay.cdb.dao;

import java.sql.Connection;
import java.util.List;

import fr.heffebaycay.cdb.model.Computer;
import fr.heffebaycay.cdb.wrapper.SearchWrapper;

public interface IComputerDao {
	
	/**
	 * This method returns the list of all computers stored in the data source
	 * 
	 * @return
	 */
	List<Computer> findAll();
	
	
	/**
	 * Find a given Computer from the data source
	 * 
	 * @param id The Id of the Computer object that should be returned
	 * @return An instance of Computer or null if there's no match
	 */
	Computer findById(long id);
	
	
	/**
	 * This method removes a Computer from the data source based on its Id
	 * 
	 * @param id Id of the computer object to be removed
	 * @return boolean indicating success (true) or failure (false) of the removal operation
	 */
	boolean remove(long id);
		
	/**
	 * Create a Computer in the data source based on an instance of Computer
	 * 
	 * @param computer The computer object that should be persisted
	 * 
	 * @return id of the created computer
	 */
	long create(Computer computer);
	
	
	/**
	 * Update an already existing Computer in the data source
	 * 
	 * @param computer The computer object that should be updated
	 */
	void update(Computer computer);
	
	/**
	 * Queries the data source for <strong>nbRequested</strong> elements starting at the offset defined by the <strong>offset</strong> parameter
	 * 
	 * @param offset           The offset of the first Computer element that should be returned
	 * @param nbRequested      The number of elements requested
	 * @return                 A SearchWrapper element containing the results as well as page information
	 */
	SearchWrapper<Computer> findAll(long offset, long nbRequested);
	
	/**
	 * Removes all computers matching a given company from the data source
	 * 
	 * @param companyId        The id of the Company for which associated Computers should be removed
	 * @param conn             
	 * @return                 The number of rows affected by the operation, or <strong>-1</strong> on error.
	 */
	int removeForCompany(long companyId, Connection conn);
	

}
