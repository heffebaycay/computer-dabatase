package fr.heffebaycay.cdb.dao.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import fr.heffebaycay.cdb.dao.IComputerDao;
import fr.heffebaycay.cdb.model.Company;
import fr.heffebaycay.cdb.model.Computer;

public class ComputerDaoMockImpl implements IComputerDao {

	protected List<Computer> computers;
	
	public ComputerDaoMockImpl() {
		computers = new ArrayList<Computer>();
		
		Company apple = new Company.Builder()
										.id(1)
										.name("Apple Inc.")
										.build();
		
		Company thinkingMachines = new Company.Builder()
												.id(2)
												.name("Thinking Machines")
												.build();
		
		Computer c1 = new Computer.Builder()
										.id(1)
										.company(apple)
										.name("MacBook Pro 15.4 inch")
										.build();
		Computer c2 = new Computer.Builder()
										.id(2)
										.name("CM-2a")
										.company(thinkingMachines)
										.build();
		Computer c3 = new Computer.Builder()
										.id(3)
										.name("CM-200")
										.company(thinkingMachines)
										.build();
		
		Computer c4 = new Computer.Builder()
										.id(4)
										.name("CM-5e")
										.company(thinkingMachines)
										.build();
		
		Computer c5 = new Computer.Builder()
										.id(5)
										.name("CM-5")
										.company(thinkingMachines)
										.introduced(LocalDateTime.parse("1991-01-01T00:00:00"))
										.build();
		
		Computer c6 = new Computer.Builder()
										.id(6)
										.name("MacBook Pro")
										.company(apple)
										.introduced(LocalDateTime.parse("2006-01-10T00:00:00"))
										.build();
		
		computers.add(c1);
		computers.add(c2);
		computers.add(c3);
		computers.add(c4);
		computers.add(c5);
		computers.add(c6);
		
	}
	
	
	@Override
	public List<Computer> getComputers() {

		return computers;
	}

	@Override
	public Computer findById(long id) {
		for(Computer c : computers) {
			if(c.getId() == id) {
				return c;
			}
		}
		
		return null;
	}
	
	
	

}
