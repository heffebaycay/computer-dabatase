package fr.heffebaycay.cdb.ui;

import java.util.Scanner;

public class ComputerDatabaseCLI {

  public static final String CLI_VERSION = "1.0";

  public class MenuOption {
    public static final int COMPANY_LIST         = 1;
    public static final int COMPUTER_LIST        = 2;
    public static final int COMPUTER_SHOWDETAILS = 3;
    public static final int COMPUTER_REMOVE      = 4;
    public static final int COMPUTER_CREATE      = 5;
    public static final int COMPUTER_UPDATE      = 6;
    public static final int COMPANY_LIST_PAGE    = 7;
    public static final int EXIT                 = 8;
  }

  protected static CompanyCLIUI  companyUI;
  protected static ComputerCLIUI computerUI;

  public static void main(String[] args) {

    printWelcome();
    initServices();
    menuLogic();

  }

  protected static void printWelcome() {
    System.out.println("-------------------------------------------");
    System.out.printf("       Computer Database CLI - v%s         \n", CLI_VERSION);
    System.out.println("-------------------------------------------");
  }

  protected static void initServices() {
    companyUI = new CompanyCLIUI();
    computerUI = new ComputerCLIUI();
  }

  protected static void menuLogic() {

    boolean bMenuLoop = true;

    Scanner sc = new Scanner(System.in);

    while (bMenuLoop) {

      printMenu();

      if (handleMenuChoice(sc) == true) {
        bMenuLoop = false;
      } else {
        System.out.println("Press 'Enter' to continue ");
        sc.nextLine();
      }

    }

    sc.close();

    System.out.println("\n\n*** \tThanks for using our CLI application \\o/ ***");

  }

  protected static void printMenu() {
    System.out.println("\nThe following actions are available to choose:");

    System.out.printf("\t#%d - List Companies\n", MenuOption.COMPANY_LIST);
    System.out.printf("\t#%d - List Computers\n", MenuOption.COMPUTER_LIST);
    System.out.printf("\t#%d - Show details for a specific computer\n",
        MenuOption.COMPUTER_SHOWDETAILS);
    System.out.printf("\t#%d - Remove a specific computer\n", MenuOption.COMPUTER_REMOVE);
    System.out.printf("\t#%d - Create a new computer\n", MenuOption.COMPUTER_CREATE);
    System.out.printf("\t#%d - Update an existing computer\n", MenuOption.COMPUTER_UPDATE);
    System.out.printf("\t#%d - List Companies (w/ page)\n", MenuOption.COMPANY_LIST_PAGE);
    System.out.printf("\t#%d - Exit this application\n", MenuOption.EXIT);

    System.out.println("\nPlease type the identifier of the action you want to perform: ");

  }

  protected static boolean handleMenuChoice(Scanner sc) {

    int iChoice = sc.nextInt();
    sc.nextLine(); // Clearing Scanner buffer
    long computerId;

    switch (iChoice) {
      case MenuOption.COMPANY_LIST:
        companyUI.printCompanies();
        return false;
      case MenuOption.COMPUTER_LIST:
        computerUI.printComputers();
        return false;
      case MenuOption.COMPUTER_SHOWDETAILS:
        System.out.println("Please type the identifier of the computer:");
        computerId = sc.nextLong();
        computerUI.printComputerDetails(computerId);
        return false;
      case MenuOption.COMPUTER_REMOVE:
        System.out.println("Please type the identifier of the computer to be removed:");
        computerId = sc.nextLong();
        computerUI.printRemoveComputer(computerId);
        return false;
      case MenuOption.COMPUTER_CREATE:
        computerUI.createComputer(sc);
        return false;
      case MenuOption.COMPUTER_UPDATE:
        System.out.println("Please type the identifier of the computer:");
        computerId = sc.nextLong();
        sc.nextLine(); // clearing Scanner buffer
        computerUI.updateComputer(sc, computerId);
        return false;
      case MenuOption.COMPANY_LIST_PAGE:
        System.out.println("Please type the number of the page you wish to displayed");
        long pageNumber = sc.nextLong();
        sc.nextLine(); // clearing Scanner buffer
        companyUI.printCompaniesWithPage(pageNumber);
        return false;
      case MenuOption.EXIT:
        return true;
      default:
        System.out.println("The option you picked isn't available.");
        return false;
    }

  }

}
