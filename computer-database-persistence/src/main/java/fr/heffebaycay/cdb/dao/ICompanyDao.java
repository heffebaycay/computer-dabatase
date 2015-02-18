package fr.heffebaycay.cdb.dao;

import java.util.List;

import fr.heffebaycay.cdb.dao.exception.DaoException;
import fr.heffebaycay.cdb.model.Company;
import fr.heffebaycay.cdb.model.CompanyPageRequest;
import fr.heffebaycay.cdb.wrapper.SearchWrapper;

public interface ICompanyDao {

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
   * Removes a Company from the data source along with all the Computers tied to it
   * 
   * @param id      Identifier of the company that should be removed.
   */
  boolean remove(long id);


  /**
   * Queries the data source for a given number of elements starting at the offset defined by the <strong>request</strong> parameter
   *
   * @param request         Object containing the details of the findAll() request (offset, number of elements)
   * @param conn            
   * @return                A SearchWrapper element containing both the results as a List and additional information about pagination
   * @throws DaoException
   */
  SearchWrapper<Company> findAll(CompanyPageRequest request) throws DaoException;
  

  SearchWrapper<Company> findByName(CompanyPageRequest request) throws DaoException;
  
}
