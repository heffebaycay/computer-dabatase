package fr.heffebaycay.cdb.service;

import java.util.List;

import fr.heffebaycay.cdb.model.Company;
import fr.heffebaycay.cdb.model.CompanyPageRequest;
import fr.heffebaycay.cdb.wrapper.SearchWrapper;

public interface ICompanyService {

  /**
   * This method returns the list of all companies stored in the data source.
   * 
   * @return The list of all Companies
   */
  List<Company> findAll();

  /**
   * Find a Company from the data source
   * @param id
   * @return
   */
  Company findById(long id);

  /**
   * Create a Company in the data source based on an instance of Company
   * 
   * @param company The company object that should be created
   */
  long create(Company company);

  /**
     * Queries the data source for nbRequested elements starting at the offset defined by the parameter with the same name
     * 
     * @param offset           The offset of the first Company element that should be returned
     * @param nbRequested      The total number of elements requested
     * @return                 A SearchWrapper element containing both the results as a List and the total number of elements matched by the query
     */
  SearchWrapper<Company> findAll(CompanyPageRequest request);

  /**
   * Removes a Company from the data source
   * 
   * @param id      Identifier of the company that should be removed.
   */
  void remove(long id);

  SearchWrapper<Company> findByName(CompanyPageRequest request);

}
