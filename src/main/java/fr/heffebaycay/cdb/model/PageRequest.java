package fr.heffebaycay.cdb.model;

import fr.heffebaycay.cdb.util.SortOrder;

public class PageRequest {

  protected String searchQuery;
  
  protected Long offset;
  
  protected Long nbRequested;
  
  protected SortOrder sortOrder;

  public PageRequest() {
    
  }
  
  public String getSearchQuery() {
    return searchQuery;
  }

  public void setSearchQuery(String searchQuery) {
    this.searchQuery = searchQuery;
  }

  public Long getOffset() {
    return offset;
  }

  public void setOffset(Long offset) {
    this.offset = offset;
  }

  public Long getNbRequested() {
    return nbRequested;
  }

  public void setNbRequested(Long nbRequested) {
    this.nbRequested = nbRequested;
  }

  public SortOrder getSortOrder() {
    return sortOrder;
  }

  public void setSortOrder(SortOrder sortOrder) {
    this.sortOrder = sortOrder;
  }
  
  
  
  
}
