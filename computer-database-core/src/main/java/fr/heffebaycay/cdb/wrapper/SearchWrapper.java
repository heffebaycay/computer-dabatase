package fr.heffebaycay.cdb.wrapper;

import java.util.List;

/**
 * Generic SearchWrapper class. An object of this class is returned for data queried with offsets.
 *
 * @param <T> The Entity type queried
 */
public class SearchWrapper<T> {

  /**
   * The actual data result
   */
  private List<T> results;
  
  /**
   * Total number of elements matching the query
   * This is always greater of equal than the number of elements in the <strong>results</strong> object.
   */
  private long totalCount;
  
  /**
   * Number of the current page. Page numbers are 1-indexed.
   */
  private long currentPage;
  
  /**
   * Total number of pages of elements matching the query
   */
  private long totalPage;
  
  /**
   * Attribute indicating in what order the elements should be sorted
   */
  private String sortOrder;
  
  /**
   * Attribute indicating the criterion that should be used for sorting elements
   */
  private String sortCriterion;
  
  /**
   * An optional search keyword to filter elements
   */
  private String searchQuery;
  
  
  public List<T> getResults() {
    return this.results;
  }
  
  public SearchWrapper<T> setResults(List<T> results) {
    this.results = results;
    return this;
  }
  
  public long getTotalCount() {
    return totalCount;
  }
  
  public SearchWrapper<T> setTotalCount(long totalCount) {
    this.totalCount = totalCount;
    return this;    
  }

  public long getCurrentPage() {
    return currentPage;
  }

  public void setCurrentPage(long currentPage) {
    this.currentPage = currentPage;
  }

  public long getTotalPage() {
    return totalPage;
  }

  public void setTotalPage(long totalPage) {
    this.totalPage = totalPage;
  }

  public String getSortOrder() {
    return sortOrder;
  }

  public void setSortOrder(String sortOrder) {
    this.sortOrder = sortOrder;
  }

  public String getSortCriterion() {
    return sortCriterion;
  }

  public void setSortCriterion(String sortCriterion) {
    this.sortCriterion = sortCriterion;
  }

  public String getSearchQuery() {
    return searchQuery;
  }

  public void setSearchQuery(String searchQuery) {
    this.searchQuery = searchQuery;
  }
  
  
}
