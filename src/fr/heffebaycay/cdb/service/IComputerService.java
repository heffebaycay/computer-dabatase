package fr.heffebaycay.cdb.service;

import java.util.List;

import fr.heffebaycay.cdb.model.Computer;

public interface IComputerService {
	
	
	List<Computer> getComputers();
	
	Computer findById(long id);
	
	boolean remove(long id);
	
	void create(Computer computer);
	
	void update(Computer computer);

}
