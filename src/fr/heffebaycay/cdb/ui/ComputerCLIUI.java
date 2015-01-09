package fr.heffebaycay.cdb.ui;

import fr.heffebaycay.cdb.model.Company;
import fr.heffebaycay.cdb.model.Computer;
import fr.heffebaycay.cdb.service.ICompanyService;
import fr.heffebaycay.cdb.service.IComputerService;
import fr.heffebaycay.cdb.service.manager.ServiceManager;

import java.util.List;
import java.util.Scanner;

public class ComputerCLIUI {

	IComputerService computerService;
	ICompanyService companyService;
	
	public ComputerCLIUI() {
		computerService = ServiceManager.INSTANCE.getComputerService();
		companyService = ServiceManager.INSTANCE.getCompanyService();
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

	public void createComputer(Scanner sc) {
		/** In order to create a valid computer, we need the following info:
		 *  - name
		 *  - Date Introduced
		 *  - Date Discontinued
		 *  - Company
		 *  
		 *  If the company doesn't exist, we need to let the user create it.
		 */
		
		System.out.println("\n\t** Welcome to the Computer creation wizard ** ");
		System.out.println("Please answer the following questions in order to create a new computer:");
		
		Computer computer = new Computer();
		
		System.out.println("What is the name of the computer ?");
		String name = sc.nextLine();
		
		try {
			computer.setName(name);
		} catch(IllegalArgumentException iae) {
			System.out.printf("[Error] %s - Canceling creation\n", iae.getMessage());
			return;
		}
		
		System.out.println("On what date was the computer introduced (format: year-month-day)?");
		String introduced = sc.nextLine();
		
		try {
			computer.setIntroduced(introduced);
		} catch(IllegalArgumentException iae) {
			System.out.printf("[Error] %s - Canceling creation", iae.getMessage());
			return;
		}
		
		System.out.println("On what date was the computer discontinued (format: year-month-day)?");
		String discontinued = sc.nextLine();
		
		try {
			computer.setDiscontinued(discontinued);
		} catch(IllegalArgumentException iae) {
			System.out.printf("[Error] %s - Canceling creation\n", iae.getMessage());
			return;
		}
		
		
		System.out.println("Last step: we need to tie this computer to a Company. Please enter the id of the Company (-1 to create a new Company).");
		long companyId = sc.nextLong();
		sc.nextLine(); // clearing Scanner buffer
		
		if(companyId == -1) {
			// Creating new company
			Company company = new Company();
			
			System.out.println("What is the name of the Company that created this computer?");
			String companyName = sc.nextLine();
			
			try {
				company.setName(companyName);
			} catch(IllegalArgumentException iae) {
				System.out.printf("[Error] %s - Canceling creation\n", iae.getMessage());
				return;
			}
			
			companyService.create(company);
			
			computer.setCompany(company);
			
			
		} else {
			// Need to fetch company with requested Id
			Company company = companyService.findById(companyId);
			if(company == null) {
				System.out.printf("[Error] No company matches the id '%d'. Canceling creation\n", companyId);
				return;
			} else {
				computer.setCompany(company);
			}
		}
		
		computerService.create(computer);
		
	}

}
