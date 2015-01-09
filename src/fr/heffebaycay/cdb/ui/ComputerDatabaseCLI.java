package fr.heffebaycay.cdb.ui;

import java.util.Scanner;

import fr.heffebaycay.cdb.service.ICompanyService;
import fr.heffebaycay.cdb.service.manager.ServiceManager;

public class ComputerDatabaseCLI {

	public static final String CLI_VERSION = "1.0";
	
	public static final int CLI_MENUOPTION_COMPANY_LIST = 1;
	public static final int CLI_MENUOPTION_COMPUTER_LIST = 2;
	
	protected static CompanyCLIUI companyUI;
	
	public static void main(String[] args){
		
		printWelcome();
		initServices();
		menuLogic();
		
	}
	
	protected static void printWelcome() {
		System.out.println("-------------------------------------------");
		System.out.printf( "       Computer Database CLI - v%s         \n", CLI_VERSION);
		System.out.println("-------------------------------------------");
	}
	
	protected static void initServices() {
		companyUI = new CompanyCLIUI();
	}
	
	protected static void menuLogic() {
		
		boolean bMenuLoop = true;
		
		while(bMenuLoop) {
			
			printMenu();
			
			if(handleMenuChoice() == true) {
				bMenuLoop = false;
			}
			
		}
		
		System.out.println("\tThanks for using our CLI application \\o/");
		
		
	}
	
	protected static void printMenu() {
		System.out.println("The following actions are available to choose:");
		
		System.out.printf("\t#%d - List Companies\n", CLI_MENUOPTION_COMPANY_LIST);
		System.out.printf("\t#%d - List Computers\n", CLI_MENUOPTION_COMPUTER_LIST);
		
		System.out.println("\nPlease type the identifier of the action you want to perform: ");
		
	}
	
	protected static boolean handleMenuChoice() {
		
		Scanner sc = new Scanner(System.in);
		int iChoice = sc.nextInt();
		
		switch(iChoice) {
		case CLI_MENUOPTION_COMPANY_LIST:
			companyUI.printCompanies();
			return true;
			default:
				System.out.println("The option you picked isn't available.");
				return false;
		}
		
	}
	
	
	
	
}
