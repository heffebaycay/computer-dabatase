package fr.heffebaycay.cdb.dao;

import java.sql.Connection;
import java.util.List;

import fr.heffebaycay.cdb.dao.exception.DaoException;
import fr.heffebaycay.cdb.model.Company;
import fr.heffebaycay.cdb.util.CompanySortCriteria;
import fr.heffebaycay.cdb.util.SortOrder;
import fr.heffebaycay.cdb.wrapper.SearchWrapper;

public interface ICompanyDao {

  /**
   * This method returns the list of all companies stored in the data source.
   * 
   * @return The list of all Companies
   */
  List<Company> findAll(Connection conn) throws DaoException;

  /**
   * Find a Company from the data source
   * @param id
   * @return
   */
  Company findById(long id, Connection conn) throws DaoException;

  /**
   * Create a Company in the data source based on an instance of Company
   * 
   * @param company The company object that should be created
   */
  void create(Company company, Connection conn) throws DaoException;
  
  /**
   * Removes a Company from the data source along with all the Computers tied to it
   * 
   * @param id      Identifier of the company that should be removed.
   */
  int remove(long id, Connection conn) throws DaoException;

  /**
   * Queries the data source for nbRequested elements starting at the offset defined by the parameter with the same name
   * 
   * @param offset           The offset of the first Company element that should be returned
   * @param nbRequested      The total number of elements requested
   * @return                 A SearchWrapper element containing both the results as a List and the total number of elements matched by the query
   */
  SearchWrapper<Company> findAll(long offset, long nbRequested, CompanySortCriteria sortCriterion, SortOrder sortOrder, Connection conn) throws DaoException;
  
  
  SearchWrapper<Company> findByName(String name, long offset, long nbRequested, CompanySortCriteria sortCriterion, SortOrder sortOrder, Connection conn ) throws DaoException;
  
}
