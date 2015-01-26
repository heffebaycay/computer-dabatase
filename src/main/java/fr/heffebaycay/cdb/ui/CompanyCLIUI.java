package fr.heffebaycay.cdb.ui;

import fr.heffebaycay.cdb.model.Company;
import fr.heffebaycay.cdb.model.CompanyPageRequest;
import fr.heffebaycay.cdb.service.ICompanyService;
import fr.heffebaycay.cdb.service.manager.ServiceManager;
import fr.heffebaycay.cdb.util.AppSettings;
import fr.heffebaycay.cdb.util.CompanySortCriteria;
import fr.heffebaycay.cdb.util.SortOrder;
import fr.heffebaycay.cdb.wrapper.SearchWrapper;

import java.util.List;

public class CompanyCLIUI {
	
	ICompanyService companyService;
	
	
	public CompanyCLIUI() {
		companyService = ServiceManager.INSTANCE.getCompanyService();
	}
	
	/**
	 * Prints the list of all companies
	 */
	public void printCompanies() {
		
		List<Company> companies = companyService.findAll();
		
		for(Company c : companies) {
			System.out.println(c);
		}
		
	}
	
	/**
	 * Prints the list of all companies listed on page #<strong>pageNumber</strong>
	 * 
	 * @param pageNumber   The number of the page for which the companies should be listed
	 */
	public void printCompaniesWithPage(long pageNumber) {
	  
	  long offset = (pageNumber - 1) * AppSettings.NB_RESULTS_PAGE;
	  
	  CompanyPageRequest pageRequest = new CompanyPageRequest.Builder()
	                                                            .offset(offset)
	                                                            .nbRequested(AppSettings.NB_RESULTS_PAGE)
	                                                            .sortCriterion(CompanySortCriteria.ID)
	                                                            .sortOrder(SortOrder.ASC)
	                                                            .build();
	  
	  SearchWrapper<Company> sw = companyService.findAll(pageRequest);
	  
	  System.out.printf("Displaying page %d of %d:%n", sw.getCurrentPage(), sw.getTotalPage());
	  
	  for(Company c : sw.getResults()) {
	    System.out.println(c);
	  }
	  
	}
	
	/**
	 * Handles the Company removal operation.
	 * 
	 * @param companyId    Identifier of the Company that should be removed from the data source
	 */
	public void removeCompany(long companyId) {
	  
	  companyService.remove(companyId);
	  
	  System.out.println("Company removal operation completed.");
	  
	}
	

}
