package fr.heffebaycay.cdb.ui;

import fr.heffebaycay.cdb.model.Computer;
import fr.heffebaycay.cdb.service.IComputerService;
import fr.heffebaycay.cdb.service.manager.ServiceManager;

import java.util.List;

public class ComputerCLIUI {

	IComputerService computerService;
	
	public ComputerCLIUI() {
		computerService = ServiceManager.INSTANCE.getComputerService();
	}
	
	public void printComputers() {
		List<Computer> computers = computerService.getComputers();
		
		for(Computer c : computers) {
			System.out.println(c);
		}
	}
	
	
	
	
}
