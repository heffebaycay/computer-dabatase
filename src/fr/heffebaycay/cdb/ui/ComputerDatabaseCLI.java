package fr.heffebaycay.cdb.ui;

import java.util.Scanner;

public class ComputerDatabaseCLI {

	public static final String CLI_VERSION = "1.0";

	
	public class MenuOption {
		public static final int COMPANY_LIST = 1;
		public static final int COMPUTER_LIST = 2;
		public static final int COMPUTER_SHOWDETAILS = 3;
	}
	
	protected static CompanyCLIUI companyUI;
	protected static ComputerCLIUI computerUI;
	
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
		computerUI = new ComputerCLIUI();
	}
	
	protected static void menuLogic() {
		
		boolean bMenuLoop = true;
		
		Scanner sc = new Scanner(System.in);
		
		while(bMenuLoop) {
			
			printMenu();
			
			if(handleMenuChoice(sc) == true) {
				bMenuLoop = false;
			}
			
		}
		
		sc.close();
		
		System.out.println("\n\n*** \tThanks for using our CLI application \\o/ ***");
		
		
	}
	
	protected static void printMenu() {
		System.out.println("The following actions are available to choose:");
		
		System.out.printf("\t#%d - List Companies\n", MenuOption.COMPANY_LIST);
		System.out.printf("\t#%d - List Computers\n", MenuOption.COMPUTER_LIST);
		System.out.printf("\t#%d - Show details for a specific computer\n", MenuOption.COMPUTER_SHOWDETAILS);
		
		System.out.println("\nPlease type the identifier of the action you want to perform: ");
		
	}
	
	protected static boolean handleMenuChoice(Scanner sc) {
		
		int iChoice = sc.nextInt();
		
		switch(iChoice) {
		case MenuOption.COMPANY_LIST:
			companyUI.printCompanies();
			return true;
		case MenuOption.COMPUTER_LIST:
			computerUI.printComputers();
			return true;
		case MenuOption.COMPUTER_SHOWDETAILS:
			System.out.println("Please type the identifier of the computer:");
			long computerId = sc.nextLong();
			computerUI.printComputerDetails(computerId);
			return true;
			
			default:
				System.out.println("The option you picked isn't available.");
				return false;
		}
		
	}	
	
	
	
}
