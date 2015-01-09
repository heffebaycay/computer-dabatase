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
	
	public void printComputerDetails(long id) {
		
		Computer computer = computerService.findById(id);
		
		if(computer == null) {
			System.out.printf("[Error] Failed to find a computer for id '%d'\n", id);
		} else {
			System.out.printf("Here is the details of computer with id '%d'\n:\t%s\n", id, computer);
		}
		
	}
	
	public void printRemoveComputer(long id) {
		
		boolean result = computerService.remove(id);
		
		if(result) {
			System.out.printf("Computer with id '%d' was successfully removed\n", id);
		} else {
			System.out.printf("[Error] Failed to remove computer with id '%d'\n", id);
		}
		
	}
}
