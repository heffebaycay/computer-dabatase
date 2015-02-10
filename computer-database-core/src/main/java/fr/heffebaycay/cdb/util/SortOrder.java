package fr.heffebaycay.cdb.util;

public enum SortOrder {

  ASC("asc"),
  DESC("desc");
  
  private String name;
  
  private SortOrder(String name) {
    this.name = name;
  }
  
  public String getName() {
    return this.name;
  }
  
}
