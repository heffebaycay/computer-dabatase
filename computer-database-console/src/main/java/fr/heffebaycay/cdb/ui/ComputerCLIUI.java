package fr.heffebaycay.cdb.ui;

import java.util.List;
import java.util.Scanner;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.heffebaycay.cdb.dto.CompanyDTO;
import fr.heffebaycay.cdb.dto.ComputerDTO;
import fr.heffebaycay.cdb.webservice.ICompanyRESTService;
import fr.heffebaycay.cdb.webservice.IComputerRESTService;
import fr.heffebaycay.cdb.wrapper.SearchWrapper;

@Service
public class ComputerCLIUI {

  IComputerRESTService        computerWebService;

  ICompanyRESTService         companyWebService;

  private static final Logger LOGGER = LoggerFactory.getLogger(ComputerCLIUI.class.getSimpleName());

  @Autowired
  private Validator           validator;

  public ComputerCLIUI() {

  }

  public void setComputerWebService(IComputerRESTService computerWebService) {
    this.computerWebService = computerWebService;
  }

  public void setCompanyWebService(ICompanyRESTService companyWebService) {
    this.companyWebService = companyWebService;
  }

  /**
   * Prints the list of all computers
   */
  public void printComputers() {
    List<ComputerDTO> computers = computerWebService.findAll();

    for (ComputerDTO c : computers) {
      System.out.println(c);
    }
  }

  /**
   * Prints details about a specific computer, identified by its identifier (you don't say :O)
   * 
   * @param id  The identifier of the computer object
   */
  public void printComputerDetails(long id) {

    ComputerDTO computerDTO = computerWebService.findById(id);

    if (computerDTO == null) {
      System.out.printf("[Error] Failed to find a computer for id '%d'%n", id);
    } else {
      System.out.printf("Here is the details of computer with id '%d'%n:\t%s%n", id, computerDTO);
    }

  }

  /**
   * Removes a computer from the data source and prints the result of the operation
   * 
   * @param id  The identifier of the Computer
   */
  public void printRemoveComputer(long id) {

    Response response = computerWebService.remove(id);

    if (response.getStatus() == Status.NO_CONTENT.getStatusCode()) {
      // NO_CONTENT means the removal operation succeeded
      System.out.printf("Computer with id '%d' was successfully removed%n", id);
    } else {
      System.out.printf(
          "[Error] Failed to remove computer with id '%d'. Remote server returned HTTP code %d.%n",
          id, response.getStatus());
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

    System.out.println("%n\t** Welcome to the Computer creation wizard ** ");
    System.out.println("Please answer the following questions in order to create a new computer:");

    Set<ConstraintViolation<ComputerDTO>> validationErrors;

    ComputerDTO computer = new ComputerDTO();

    System.out.println("What is the name of the computer ?");
    String name = sc.nextLine();

    validationErrors = validator.validateValue(ComputerDTO.class, ComputerDTO.ATTR_NAME, name);
    if (!validationErrors.isEmpty()) {
      LOGGER.debug("createComputer(): Invalid name for computer.");
      printValidationErrors(validationErrors);
      return;
    } else {
      computer.setName(name);
    }

    System.out.println("On what date was the computer introduced (format: year-month-day)?");
    String introduced = sc.nextLine();

    validationErrors = validator.validateValue(ComputerDTO.class, ComputerDTO.ATTR_INTRODUCED, introduced);
    if (!validationErrors.isEmpty()) {
      LOGGER.debug("createComputer(): Invalid date introduced for computer.");
      printValidationErrors(validationErrors);
      return;
    } else {
      computer.setIntroduced(introduced);
    }

    System.out.println("On what date was the computer discontinued (format: year-month-day)?");
    String discontinued = sc.nextLine();

    validationErrors = validator.validateValue(ComputerDTO.class, ComputerDTO.ATTR_DISCONTINUED, discontinued);
    if (!validationErrors.isEmpty()) {
      LOGGER.debug("createComputer(): Invalid date discontinued for computer.");
      printValidationErrors(validationErrors);
      return;
    } else {
      computer.setDiscontinued(discontinued);
    }

    System.out
        .println("Last step: we need to tie this computer to a Company. Please enter the id of the Company (-1 to create a new Company, 0 for no Company).");
    long companyId = sc.nextLong();
    sc.nextLine(); // clearing Scanner buffer

    if (companyId == -1) {
      // Creating new company
      CompanyDTO company = new CompanyDTO();

      System.out.println("What is the name of the Company that created this computer?");
      String companyName = sc.nextLine();

      Set<ConstraintViolation<CompanyDTO>> companyErrors = validator.validateValue(
          CompanyDTO.class, CompanyDTO.ATTR_NAME, companyName);
      if (!companyErrors.isEmpty()) {
        LOGGER.debug("createComputer(): Invalid name for company.");
        for (ConstraintViolation<CompanyDTO> error : companyErrors) {
          System.out.printf("[Error] %s - Canceling creation%n", error.getMessage());
        }
        return;
      } else {
        company.setName(companyName);
      }

      company = companyWebService.create(company);

      computer.setCompany(company);

    } else if (companyId > 0) {
      // Need to fetch company with requested Id
      CompanyDTO company = companyWebService.findById(companyId);
      if (company == null) {
        System.out
            .printf("[Error] No company matches the id '%d'. Canceling creation%n", companyId);
        return;
      } else {
        computer.setCompany(company);
      }
    }

    computerWebService.create(computer);

  }

  /**
   * Prints the interactive Computer update text interface
   * 
   * @param sc  A <i>Scanner</i> object, used to prompt the user for details about the computer
   * @param id  The identifier of the computer that should be updated
   */
  public void updateComputer(Scanner sc, long id) {

    System.out.println("%n\t** Welcome to the Computer update wizard ** ");
    System.out.println("Please answer the following questions in order to update an existing:");

    Set<ConstraintViolation<ComputerDTO>> validationErrors;

    ComputerDTO computer = computerWebService.findById(id);
    if (computer == null) {
      System.out.printf("[Error] No computer matches the id '%d'%n. Canceling update%n", id);
      return;
    }

    System.out.printf("What's the name of the computer? ('%s')%n", computer.getName());
    String name = sc.nextLine();

    if (name != null && name.length() > 0) {
      // User typed something

      validationErrors = validator.validateValue(ComputerDTO.class, ComputerDTO.ATTR_NAME, name);
      if (!validationErrors.isEmpty()) {
        LOGGER.debug("updateComputer(): Invalid name for computer.");
        printValidationErrors(validationErrors);
        return;
      } else {
        computer.setName(name);
      }
    }

    /**
     * 'Introduced' attribute
     */

    System.out.printf(
        "On what date was the computer introduced (type null for a null value)? ('%s')%n",
        computer.getIntroduced());
    String introduced = sc.nextLine();

    if (introduced != null && introduced.length() > 0) {
      // User typed something
      if ("null".equals(introduced)) {
        computer.setIntroduced(null);
      } else {

        // Validating 'Introduced' attribute
        validationErrors = validator.validateValue(ComputerDTO.class, ComputerDTO.ATTR_INTRODUCED, introduced);
        if (!validationErrors.isEmpty()) {
          LOGGER.debug("updateComputer(): Invalid date introduced for computer.");
          printValidationErrors(validationErrors);
          return;
        } else {
          computer.setIntroduced(introduced);
        }
      }
    }

    /**
     * 'Discontinued' attribute
     */

    System.out.printf(
        "On what date was the computer discontinued (type null for a null value)? ('%s')%n",
        computer.getDiscontinued());
    String discontinued = sc.nextLine();

    if (discontinued != null && discontinued.length() > 0) {
      // User typed something
      if ("null".equals(discontinued)) {
        computer.setDiscontinued(null);
      } else {
        // Validating 'Discontinued' attribute
        validationErrors = validator.validateValue(ComputerDTO.class, ComputerDTO.ATTR_DISCONTINUED, discontinued);
        if (!validationErrors.isEmpty()) {
          LOGGER.debug("updateComputer(): Invalid date discontinued for computer.");
          printValidationErrors(validationErrors);
          return;
        } else {
          computer.setDiscontinued(discontinued);
        }
      }
    }

    System.out
        .printf(
            "Last step: we need to tie this computer to a Company. Please enter the id of the Company (-1 to keep current value, 0 for no company). (%d)%n",
            computer.getCompany().getId());
    long companyId = sc.nextLong();
    sc.nextLine(); // clearing Scanner buffer
    if (companyId > 0) {
      CompanyDTO company = companyWebService.findById(companyId);
      if (company == null) {
        System.out
            .printf("[Error] No company matches the id '%d'. Canceling creation%n", companyId);
        return;
      } else {
        computer.setCompany(company);
      }
    } else if (companyId == 0) {
      computer.setCompany(null);
    }

    computerWebService.update(computer);

  }

  /**
   * Prints the list of Computers located on a given page
   * 
   * @param pageNumber
   */
  public void printComputersWithPage(long pageNumber) {

    SearchWrapper<ComputerDTO> dtoWrapper = computerWebService.findAllPaged(pageNumber);

    System.out.printf("Displaying page %d of %d:%n", dtoWrapper.getCurrentPage(),
        dtoWrapper.getTotalPage());

    for (ComputerDTO c : dtoWrapper.getResults()) {
      System.out.println(c);
    }

  }

  /**
   * Print a set of ComputerDTO validation errors to the console
   * 
   * @param errors  The list of validation errors to be printed
   */
  private void printValidationErrors(Set<ConstraintViolation<ComputerDTO>> errors) {
    for (ConstraintViolation<ComputerDTO> error : errors) {
      System.out.printf("[Error] %s - Canceling creation%n", error.getMessage());
    }
  }

}
