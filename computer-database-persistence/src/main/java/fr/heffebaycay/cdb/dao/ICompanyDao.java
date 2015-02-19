package fr.heffebaycay.cdb.dao;

import java.util.List;

import fr.heffebaycay.cdb.dao.exception.DaoException;
import fr.heffebaycay.cdb.model.Company;
import fr.heffebaycay.cdb.model.CompanyPageRequest;
import fr.heffebaycay.cdb.wrapper.SearchWrapper;

/**
 * Company DAO contract
 *
 */
public interface ICompanyDao {

  /**
   * This method returns the list of all companies stored in the data source.
   * 
   * @return The list of all Companies
   */
  List<Company> findAll();

  /**
   * Find a Company from the data source
   * @param id      The unique identifier of the Company to fetch
   * @return        The company matching the id or <strong>null</strong> if no company was found.
   */
  Company findById(long id);

  /**
   * Create a Company in the data source based on an instance of Company
   * 
   * @param company The company object that should be created
   * @return        Identifier of the newly created Company
   */
  long create(Company company);

  /**
   * Removes a Company from the data source along with all the Computers tied to it
   * 
   * @param id      Identifier of the company that should be removed.
   * @return        <strong>true</strong> on success, <strong>false</strong> on failure.
   */
  boolean remove(long id);

  /**
   * Queries the data source for a given number of elements starting at the offset defined within the <strong>request</strong> parameter.
   * 
   * @param request         Object containing the details of the findAll() request (offset, number of elements)
   * @return                A {@link fr.heffebaycay.cdb.wrapper.SearchWrapper} object containing the collection of Company object, as well as information about the page
   * @throws DaoException   If the <strong>request</strong> parameter is null
   */
  SearchWrapper<Company> findAll(CompanyPageRequest request) throws DaoException;

  /**
   * Queries the data source for a given number of elements starting at the offset defined within the <strong>request</strong> parameter.
   * 
   * The method will attempt to find Computer objects whose name or company name (if applicable) matches the searchQuery attribute of <strong>request</strong>.
   * 
   * @param request         Object containing the details of the findByName() request, including the search keyword
   * @return                A {@link fr.heffebaycay.cdb.wrapper.SearchWrapper} object containing the collection of Company object, as well as information about the page
   * @throws DaoException   If the <strong>request</strong> parameter is null
   */
  SearchWrapper<Company> findByName(CompanyPageRequest request) throws DaoException;

}
