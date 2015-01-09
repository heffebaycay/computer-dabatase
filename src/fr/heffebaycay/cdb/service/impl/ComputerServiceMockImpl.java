package fr.heffebaycay.cdb.service.impl;

import java.util.List;

import fr.heffebaycay.cdb.dao.IComputerDao;
import fr.heffebaycay.cdb.dao.manager.DaoManager;
import fr.heffebaycay.cdb.model.Computer;
import fr.heffebaycay.cdb.service.IComputerService;

public class ComputerServiceMockImpl implements IComputerService {

	
	IComputerDao computerDao;
	
	public ComputerServiceMockImpl() {
		computerDao = DaoManager.INSTANCE.getComputerDao();
	}
	
	@Override
	public List<Computer> getComputers() {
		return computerDao.getComputers();
	}

	@Override
	public Computer findById(long id) {
		return computerDao.findById(id);
	}
	
	@Override
	public boolean remove(long id) {
		
		return computerDao.remove(id);
		
	}
	
	@Override
	public void create(Computer computer) {
		computerDao.create(computer);
	}
	

}
