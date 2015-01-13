package fr.heffebaycay.cdb.wrapper;

import java.util.List;

/**
 * Generic SearchWrapper class. An object of this class is returned for data queried with offsets.
 *
 * @param <T> The Entity type queried
 */
public class SearchWrapper<T> {

  private List<T> results;
  
  private long totalQueryCount;
  
  private long currentPage;
  
  private long totalPage;
  
  
  public List<T> getResults() {
    return this.results;
  }
  
  public SearchWrapper<T> setResults(List<T> results) {
    this.results = results;
    return this;
  }
  
  public long getTotalQueryCount() {
    return totalQueryCount;
  }
  
  public SearchWrapper<T> setTotalQueryCount(long totalQueryCount) {
    this.totalQueryCount = totalQueryCount;
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
  
  
  
  
  
}
