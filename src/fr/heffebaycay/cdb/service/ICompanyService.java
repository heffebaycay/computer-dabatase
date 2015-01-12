package fr.heffebaycay.cdb.service;

import java.util.List;

import fr.heffebaycay.cdb.model.Company;
import fr.heffebaycay.cdb.wrapper.SearchWrapper;

public interface ICompanyService {

  List<Company> getCompanies();

  Company findById(long id);

  void create(Company company);

  /**
     * Queries the datasource for nbRequested elements starting at the offset defined by the parameter with the same name
     * 
     * @param offset           The offset of the first Company element that should be returned
     * @param nbRequested      The total number of elements requested
     * @return                 A SearchWrapper element containing both the results as a List and the total number of elements matched by the query
     */
  SearchWrapper<Company> getCompanies(long offset, long nbRequested);

}
