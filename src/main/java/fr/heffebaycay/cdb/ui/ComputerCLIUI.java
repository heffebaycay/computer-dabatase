package fr.heffebaycay.cdb.ui;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

import fr.heffebaycay.cdb.model.Company;
import fr.heffebaycay.cdb.model.Computer;
import fr.heffebaycay.cdb.service.ICompanyService;
import fr.heffebaycay.cdb.service.IComputerService;
import fr.heffebaycay.cdb.service.manager.ServiceManager;
import fr.heffebaycay.cdb.util.AppSettings;
import fr.heffebaycay.cdb.wrapper.SearchWrapper;

public class ComputerCLIUI {

  IComputerService computerService;
  ICompanyService  companyService;

  public ComputerCLIUI() {
    computerService = ServiceManager.INSTANCE.getComputerService();
    companyService = ServiceManager.INSTANCE.getCompanyService();
  }

  /**
   * Prints the list of all computers
   */
  public void printComputers() {
    List<Computer> computers = computerService.findAll();

    for (Computer c : computers) {
      System.out.println(c);
    }
  }

  /**
   * Prints details about a specific computer, identified by its identifier (you don't say :O)
   * 
   * @param id  The identifier of the computer object
   */
  public void printComputerDetails(long id) {

    Computer computer = computerService.findById(id);

    if (computer == null) {
      System.out.printf("[Error] Failed to find a computer for id '%d'\n", id);
    } else {
      System.out.printf("Here is the details of computer with id '%d'\n:\t%s\n", id, computer);
    }

  }

  /**
   * Removes a computer from the data source and prints the result of the operation
   * 
   * @param id  The identifier of the Computer
   */
  public void printRemoveComputer(long id) {

    boolean result = computerService.remove(id);

    if (result) {
      System.out.printf("Computer with id '%d' was successfully removed\n", id);
    } else {
      System.out.printf("[Error] Failed to remove computer with id '%d'\n", id);
    }

  }

  /**
   * Prints the interactive computer creation text interface
   * 
   * @param sc  A <i>Scanner</i> object, used to prompt the user about the details of the computer
   */
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
    } catch (IllegalArgumentException iae) {
      System.out.printf("[Error] %s - Canceling creation\n", iae.getMessage());
      return;
    }
    
    System.out.println("On what date was the computer introduced (format: year-month-day)?");
    String introduced = sc.nextLine();

    try {
      computer.setIntroduced(introduced);
    } catch (IllegalArgumentException iae) {
      System.out.printf("[Error] %s - Canceling creation", iae.getMessage());
      return;
    }

    System.out.println("On what date was the computer discontinued (format: year-month-day)?");
    String discontinued = sc.nextLine();

    try {
      computer.setDiscontinued(discontinued);
    } catch (IllegalArgumentException iae) {
      System.out.printf("[Error] %s - Canceling creation\n", iae.getMessage());
      return;
    }

    System.out
        .println("Last step: we need to tie this computer to a Company. Please enter the id of the Company (-1 to create a new Company, 0 for no Company).");
    long companyId = sc.nextLong();
    sc.nextLine(); // clearing Scanner buffer

    if (companyId == -1) {
      // Creating new company
      Company.Builder companyBuilder = new Company.Builder();
      Company company;
      

      System.out.println("What is the name of the Company that created this computer?");
      String companyName = sc.nextLine();

      companyBuilder.name(companyName);
      if(companyBuilder.checkName() == false) {
        // Validating "name" attribute
        System.out.println("[Error] Invalid name provided for Company. Canceling creation.");
        return;
      }
      
      // Validation succeeded, Company object can be built
      company = companyBuilder.build();

      companyService.create(company);

      computer.setCompany(company);

    } else if(companyId > 0) {
      // Need to fetch company with requested Id
      Company company = companyService.findById(companyId);
      if (company == null) {
        System.out
            .printf("[Error] No company matches the id '%d'. Canceling creation\n", companyId);
        return;
      } else {
        computer.setCompany(company);
      }
    }

    computerService.create(computer);

  }

  /**
   * Prints the interactive Computer update text interface
   * 
   * @param sc  A <i>Scanner</i> object, used to prompt the user for details about the computer
   * @param id  The identifier of the computer that should be updated
   */
  public void updateComputer(Scanner sc, long id) {

    System.out.println("\n\t** Welcome to the Computer update wizard ** ");
    System.out.println("Please answer the following questions in order to update an existing:");

    Computer computer = computerService.findById(id);
    if (computer == null) {
      System.out.printf("[Error] No computer matches the id '%d'\n. Canceling update\n", id);
      return;
    }

    System.out.printf("What's the name of the computer? ('%s')\n", computer.getName());
    String name = sc.nextLine();

    if (name != null && name.length() > 0) {
      // User typed something
      try {
        computer.setName(name);
      } catch (IllegalArgumentException iae) {
        System.out.printf("[Error] %s - Canceling creation\n", iae.getMessage());
        return;
      }
    }

    /**
     * 'Introduced' attribute
     */

    System.out.printf(
        "On what date was the computer introduced (type null for a null value)? ('%s')\n",
        computer.getIntroduced());
    String introduced = sc.nextLine();

    if (introduced != null && introduced.length() > 0) {
      // User typed something
      if (introduced.equals("null")) {
        computer.setIntroduced((LocalDateTime) null);
      } else {
        try {
          computer.setIntroduced(introduced);
        } catch (IllegalArgumentException iae) {
          System.out.printf("[Error] %s - Canceling creation\n", iae.getMessage());
          return;
        }
      }
    }

    /**
     * 'Discontinued' attribute
     */

    System.out.printf(
        "On what date was the computer discontinued (type null for a null value)? ('%s')\n",
        computer.getDiscontinued());
    String discontinued = sc.nextLine();

    if (discontinued != null && discontinued.length() > 0) {
      // User typed something
      if (discontinued.equals("null")) {
        computer.setDiscontinued((LocalDateTime) null);
      } else {
        try {
          computer.setDiscontinued(discontinued);
        } catch (IllegalArgumentException iae) {
          System.out.printf("[Error] %s - Canceling creation\n", iae.getMessage());
          return;
        }
      }
    }

    System.out
        .printf(
            "Last step: we need to tie this computer to a Company. Please enter the id of the Company (-1 to keep current value, 0 for no company). (%d)\n",
            computer.getCompany().getId());
    long companyId = sc.nextLong();
    sc.nextLine(); // clearing Scanner buffer
    if (companyId > 0) {
      Company company = companyService.findById(companyId);
      if (company == null) {
        System.out
            .printf("[Error] No company matches the id '%d'. Canceling creation\n", companyId);
        return;
      } else {
        computer.setCompany(company);
      }
    } else if(companyId == 0) {
      computer.setCompany(null);
    }

    computerService.update(computer);

  }
  
  /**
   * Prints the list of Computers located on a given page
   * 
   * @param pageNumber
   */
  public void printComputersWithPage(long pageNumber) {
    
    long offset = (pageNumber - 1) * AppSettings.NB_RESULTS_PAGE;
    
    SearchWrapper<Computer> sw = computerService.findAll(offset, AppSettings.NB_RESULTS_PAGE);
    
    System.out.printf("Displaying page %d of %d:\n", sw.getCurrentPage(), sw.getTotalPage());
    
    for(Computer c : sw.getResults()) {
      System.out.println(c);
    }
    
  }

}
