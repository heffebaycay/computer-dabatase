package fr.heffebaycay.cdb.ui;

import java.util.List;

import org.springframework.stereotype.Service;

import fr.heffebaycay.cdb.dto.CompanyDTO;
import fr.heffebaycay.cdb.webservice.ICompanyRESTService;
import fr.heffebaycay.cdb.wrapper.SearchWrapper;

@Service
public class CompanyCLIUI {

  private ICompanyRESTService companyWebService;

  public CompanyCLIUI() {
    super();
  }

  public void setCompanyWebService(ICompanyRESTService companyWebService) {
    this.companyWebService = companyWebService;
  }

  /**
   * Prints the list of all companies
   */
  public void printCompanies() {

    List<CompanyDTO> companies = companyWebService.findAll();

    for (CompanyDTO c : companies) {
      System.out.println(c);
    }

  }

  /**
   * Prints the list of all companies listed on page #<strong>pageNumber</strong>
   * 
   * @param pageNumber   The number of the page for which the companies should be listed
   */
  public void printCompaniesWithPage(long pageNumber) {

    SearchWrapper<CompanyDTO> dtoWrapper = companyWebService.findAllPaged(pageNumber);
    
    System.out.printf("Displaying page %d of %d:%n", dtoWrapper.getCurrentPage(), dtoWrapper.getTotalPage());

    for (CompanyDTO c : dtoWrapper.getResults()) {
      System.out.println(c);
    }

  }

  /**
   * Handles the Company removal operation.
   * 
   * @param companyId    Identifier of the Company that should be removed from the data source
   */
  public void removeCompany(long companyId) {

    companyWebService.remove(companyId);

    System.out.println("Company removal operation completed.");

  }

}
