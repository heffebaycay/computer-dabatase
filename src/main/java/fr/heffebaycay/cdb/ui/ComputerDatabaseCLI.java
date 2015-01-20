package fr.heffebaycay.cdb.ui;

import java.util.Scanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ComputerDatabaseCLI {

  public static final String CLI_VERSION = "1.0";
  
  private static final Logger LOGGER = LoggerFactory.getLogger(ComputerDatabaseCLI.class);
  
  public class MenuOption {
    /**
     * Here are the list of options available in the application
     * Each option value must be unique!
     */
    public static final int COMPANY_LIST         = 1;
    public static final int COMPUTER_LIST        = 2;
    public static final int COMPUTER_SHOWDETAILS = 3;
    public static final int COMPUTER_REMOVE      = 4;
    public static final int COMPUTER_CREATE      = 5;
    public static final int COMPUTER_UPDATE      = 6;
    public static final int COMPANY_LIST_PAGE    = 7;
    public static final int COMPUTER_LIST_PAGE   = 8;
    public static final int EXIT                 = 9;
  }

  protected static CompanyCLIUI  companyUI;
  protected static ComputerCLIUI computerUI;

  public static void main(String[] args) {

    printWelcome();
    initServices();
    menuLogic();

  }

  /**
   * Prints the "Welcome" header in the console
   */
  protected static void printWelcome() {
    LOGGER.debug("Starting application v{}", CLI_VERSION);
    System.out.println("-------------------------------------------");
    System.out.printf("       Computer Database CLI - v%s         %n", CLI_VERSION);
    System.out.println("-------------------------------------------");
  }

  /**
   * Initializes the basic UI services the CLI application requires
   */
  protected static void initServices() {
    companyUI = new CompanyCLIUI();
    computerUI = new ComputerCLIUI();
  }

  /**
   * Handles the logic of the application menu.
   */
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

    System.out.println("%n%n*** \tThanks for using our CLI application \\o/ ***");

  }

  /**
   * Prints the list of options offered by the CLI application
   */
  protected static void printMenu() {
    System.out.println("%nThe following actions are available to choose:");

    System.out.printf("\t#%d - List Companies%n", MenuOption.COMPANY_LIST);
    System.out.printf("\t#%d - List Computers%n", MenuOption.COMPUTER_LIST);
    System.out.printf("\t#%d - Show details for a specific computer%n",
        MenuOption.COMPUTER_SHOWDETAILS);
    System.out.printf("\t#%d - Remove a specific computer%n", MenuOption.COMPUTER_REMOVE);
    System.out.printf("\t#%d - Create a new computer%n", MenuOption.COMPUTER_CREATE);
    System.out.printf("\t#%d - Update an existing computer%n", MenuOption.COMPUTER_UPDATE);
    System.out.printf("\t#%d - List Companies (w/ page)%n", MenuOption.COMPANY_LIST_PAGE);
    System.out.printf("\t#%d - List Computers (w/ page)%n", MenuOption.COMPUTER_LIST_PAGE);
    System.out.printf("\t#%d - Exit this application%n", MenuOption.EXIT);

    System.out.println("%nPlease type the identifier of the action you want to perform: ");

  }

  /**
   * Prompts the user for a choice from the menu options and dispatches the request to the right UI handlers.
   * 
   * @param sc A <i>Scanner</i> object, used to prompt the user. 
   * 
   * @return A boolean indicating whether the main application should exit the menu loop
   * (<strong>true</strong>: the loop should be exited ; <strong>false</strong>: the loop should not be exited)
   */
  protected static boolean handleMenuChoice(Scanner sc) {

    int iChoice;
    if(sc.hasNextInt()) {
      iChoice = sc.nextInt();
      sc.nextLine();
    } else {
      iChoice = -1;
    }
    
    long computerId, pageNumber;

    switch (iChoice) {
      case MenuOption.COMPANY_LIST:
        companyUI.printCompanies();
        return false;
      case MenuOption.COMPUTER_LIST:
        computerUI.printComputers();
        return false;
      case MenuOption.COMPUTER_SHOWDETAILS:
        System.out.println("Please type the identifier of the computer:");
        if(sc.hasNextLong()) {
          computerId = sc.nextLong();
          sc.nextLine(); // clearing Scanner buffer
          computerUI.printComputerDetails(computerId);
        } else {
          System.out.println("Invalid identifier. Exiting routine.");
        }
        return false;
      case MenuOption.COMPUTER_REMOVE:
        System.out.println("Please type the identifier of the computer to be removed:");
        if(sc.hasNextLong()) {
          computerId = sc.nextLong();
          sc.nextLine(); // clearing Scanner buffer
          computerUI.printRemoveComputer(computerId);
        } else {
          System.out.println("Invalid identifier. Exiting routine.");
        }
        return false;
      case MenuOption.COMPUTER_CREATE:
        computerUI.createComputer(sc);
        return false;
      case MenuOption.COMPUTER_UPDATE:
        System.out.println("Please type the identifier of the computer:");
        if(sc.hasNextLong()) {
          computerId = sc.nextLong();
          sc.nextLine(); // clearing Scanner buffer
          computerUI.updateComputer(sc, computerId);
        } else {
          System.out.println("Invalid identifier. Exiting routine.");
        }
        return false;
      case MenuOption.COMPANY_LIST_PAGE:
        System.out.println("Please type the number of the page you wish to displayed");
        if(sc.hasNextLong()) {
          pageNumber = sc.nextLong();
          sc.nextLine(); // clearing Scanner buffer
          companyUI.printCompaniesWithPage(pageNumber);
        } else {
          System.out.println("Invalid page number. Exiting routine.");
        }
        return false;
      case MenuOption.COMPUTER_LIST_PAGE:
        System.out.println("Please type the number of the page you wish to be displayed");
        if(sc.hasNextLong()) {
          pageNumber = sc.nextLong();
          sc.nextLine(); // clearing Scanner buffer
          computerUI.printComputersWithPage(pageNumber);
        } else {
          System.out.println("Invalid page number. Exiting routine.");
        }
        return false;
      case MenuOption.EXIT:
        return true;
      default:
        System.out.println("The option you picked isn't available.");
        return false;
    }

  }

}
