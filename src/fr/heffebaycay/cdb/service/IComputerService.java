package fr.heffebaycay.cdb.service;

import java.util.List;

import fr.heffebaycay.cdb.model.Computer;
import fr.heffebaycay.cdb.wrapper.SearchWrapper;

public interface IComputerService {
	
	
	List<Computer> getComputers();
	
	Computer findById(long id);
	
	boolean remove(long id);
	
	void create(Computer computer);
	
	void update(Computer computer);
	
	/**
     * Queries the data source for <strong>nbRequested</strong> elements starting at the offset defined by the <strong>offset</strong> parameter
     * 
     * @param offset           The offset of the first Computer element that should be returned
     * @param nbRequested      The number of elements requested
     * @return                 A SearchWrapper element containing the results as well as page information
     */
	SearchWrapper<Computer> getComputers(long offset, long nbRequested);

}
