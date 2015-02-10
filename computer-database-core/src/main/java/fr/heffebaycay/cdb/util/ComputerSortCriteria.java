package fr.heffebaycay.cdb.util;

public enum ComputerSortCriteria {
  
  ID("id"),
  NAME("name"),
  DATE_INTRODUCED("introduced"),
  DATE_DISCONTINUED("discontinued"),
  COMPANY_NAME("company");
  
  private String name;
  
  private ComputerSortCriteria(String name) {
    this.name = name;
  }
  
  public String getName() {
    return this.name;
  }
  
}
