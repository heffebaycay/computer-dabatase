package fr.heffebaycay.cdb.model.view;

public class DashboardRequest {

  private Long p;
  
  private String search;
  
  private String sortBy;
  
  private String order;
  
  private String msg;
  
  public DashboardRequest() {
    this.p = 1L;
  }

  public Long getP() {
    return p;
  }

  public Long getCurrentPage() {
    return p;
  }
  
  public void setP(Long p) {
    this.p = p;
  }

  public String getSearch() {
    return search;
  }

  public String getSearchQuery() {
    return search;
  }
  
  public void setSearch(String search) {
    this.search = search;
  }

  public String getSortBy() {
    return sortBy;
  }

  public void setSortBy(String sortBy) {
    this.sortBy = sortBy;
  }

  public String getOrder() {
    return order;
  }

  public void setOrder(String order) {
    this.order = order;
  }
  
  public String getMsg() {
    return msg;
  }
  
  public void setMsg(String msg) {
    this.msg = msg;
  }
  
  
}
