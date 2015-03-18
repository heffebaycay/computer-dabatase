package fr.heffebaycay.cdb.ui;

import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.heffebaycay.cdb.webservice.ICompanyRESTService;
import fr.heffebaycay.cdb.webservice.IComputerRESTService;


@Component
public class ComputerDatabaseCLI {

  public static final String  CLI_VERSION = "0.4.0";

  public static final long NB_RESULTS_PAGE = 10;

  private static final Logger LOGGER      = LoggerFactory.getLogger(ComputerDatabaseCLI.class);

  public interface MenuOption {
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
    public static final int COMPANY_REMOVE       = 9;
    public static final int EXIT                 = 10;

  }

  @Autowired
  protected CompanyCLIUI  companyUI;
  @Autowired
  protected ComputerCLIUI computerUI;

  
  public void start(IComputerRESTService computerWebService, ICompanyRESTService companyWebService) {
    
    computerUI.setComputerWebService(computerWebService);
    computerUI.setCompanyWebService(companyWebService);
    companyUI.setCompanyWebService(companyWebService);
    
    printWelcome();
    menuLogic();

  }

  /**
   * Prints the "Welcome" header in the console
   */
  protected void printWelcome() {
    LOGGER.debug("Starting application v{}", CLI_VERSION);
    System.out.println("-------------------------------------------");
    System.out.printf("       Computer Database CLI - v%s         %n", CLI_VERSION);
    System.out.println("-------------------------------------------");
  }

  /**
   * Handles the logic of the application menu.
   */
  protected void menuLogic() {

    boolean bMenuLoop = true;

    final Scanner sc = new Scanner(System.in);

    while (bMenuLoop) {

      printMenu();

      if (handleMenuChoice(sc)) {
        bMenuLoop = false;
      } else {
        System.out.println("Press 'Enter' to continue ");
        sc.nextLine();
      }

    }

    sc.close();

    System.out.printf("%n%n*** \tThanks for using our CLI application \\o/ ***%n");

  }

  /**
   * Prints the list of options offered by the CLI application
   */
  protected void printMenu() {
    System.out.println("The following actions are available to choose:");

    System.out.printf("\t#%d - List Companies%n", MenuOption.COMPANY_LIST);
    System.out.printf("\t#%d - List Computers%n", MenuOption.COMPUTER_LIST);
    System.out.printf("\t#%d - Show details for a specific computer%n",
        MenuOption.COMPUTER_SHOWDETAILS);
    System.out.printf("\t#%d - Remove a specific computer%n", MenuOption.COMPUTER_REMOVE);
    System.out.printf("\t#%d - Create a new computer%n", MenuOption.COMPUTER_CREATE);
    System.out.printf("\t#%d - Update an existing computer%n", MenuOption.COMPUTER_UPDATE);
    System.out.printf("\t#%d - List Companies (w/ page)%n", MenuOption.COMPANY_LIST_PAGE);
    System.out.printf("\t#%d - List Computers (w/ page)%n", MenuOption.COMPUTER_LIST_PAGE);
    System.out.printf("\t#%d - Remove a specific company%n", MenuOption.COMPANY_REMOVE);
    System.out.printf("\t#%d - Exit this application%n", MenuOption.EXIT);

    System.out.printf("%nPlease type the identifier of the action you want to perform:%n");

  }

  /**
   * Prompts the user for a choice from the menu options and dispatches the request to the right UI handlers.
   * 
   * @param sc A <i>Scanner</i> object, used to prompt the user. 
   * 
   * @return A boolean indicating whether the main application should exit the menu loop
   * (<strong>true</strong>: the loop should be exited ; <strong>false</strong>: the loop should not be exited)
   */
  protected boolean handleMenuChoice(final Scanner sc) {

    int iChoice;
    if (sc.hasNextInt()) {
      iChoice = sc.nextInt();
      sc.nextLine();
    } else {
      iChoice = -1;
    }

    long computerId, pageNumber, companyId;
    boolean result;

    switch (iChoice) {
      case MenuOption.COMPANY_LIST:
        companyUI.printCompanies();
        result = false;
        break;
      case MenuOption.COMPUTER_LIST:
        computerUI.printComputers();
        result = false;
        break;
      case MenuOption.COMPUTER_SHOWDETAILS:
        System.out.println("Please type the identifier of the computer:");
        if (sc.hasNextLong()) {
          computerId = sc.nextLong();
          sc.nextLine(); // clearing Scanner buffer
          computerUI.printComputerDetails(computerId);
        } else {
          System.out.println("Invalid identifier. Exiting routine.");
        }
        result = false;
        break;
      case MenuOption.COMPUTER_REMOVE:
        System.out.println("Please type the identifier of the computer to be removed:");
        if (sc.hasNextLong()) {
          computerId = sc.nextLong();
          sc.nextLine(); // clearing Scanner buffer
          computerUI.printRemoveComputer(computerId);
        } else {
          System.out.println("Invalid identifier. Exiting routine.");
        }
        result = false;
        break;
      case MenuOption.COMPUTER_CREATE:
        computerUI.createComputer(sc);
        return false;
      case MenuOption.COMPUTER_UPDATE:
        System.out.println("Please type the identifier of the computer:");
        if (sc.hasNextLong()) {
          computerId = sc.nextLong();
          sc.nextLine(); // clearing Scanner buffer
          computerUI.updateComputer(sc, computerId);
        } else {
          System.out.println("Invalid identifier. Exiting routine.");
        }
        result = false;
        break;
      case MenuOption.COMPANY_LIST_PAGE:
        System.out.println("Please type the number of the page you wish to displayed");
        if (sc.hasNextLong()) {
          pageNumber = sc.nextLong();
          sc.nextLine(); // clearing Scanner buffer
          companyUI.printCompaniesWithPage(pageNumber);
        } else {
          System.out.println("Invalid page number. Exiting routine.");
        }
        result = false;
        break;
      case MenuOption.COMPUTER_LIST_PAGE:
        System.out.println("Please type the number of the page you wish to be displayed");
        if (sc.hasNextLong()) {
          pageNumber = sc.nextLong();
          sc.nextLine(); // clearing Scanner buffer
          computerUI.printComputersWithPage(pageNumber);
        } else {
          System.out.println("Invalid page number. Exiting routine.");
        }
        result = false;
        break;
      case MenuOption.COMPANY_REMOVE:
        System.out.println("Please type the identifier of the company to be removed:");
        if (sc.hasNextLong()) {
          companyId = sc.nextLong();
          sc.nextLine(); // clearing Scanner buffer
          companyUI.removeCompany(companyId);
        } else {
          System.out.println("Invalid identifier. Exiting routine.");
        }
        result = false;
        break;
      case MenuOption.EXIT:
        result = true;
        break;
      default:
        System.out.println("The option you picked isn't available.");
        result = false;
    }

    return result;

  }

}
