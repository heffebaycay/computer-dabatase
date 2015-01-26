package fr.heffebaycay.cdb.util;

public enum CompanySortCriteria {
  ID("id"), NAME("name");

  private String name;

  private CompanySortCriteria(String name) {
    this.name = name;
  }

  public String getName() {
    return this.name;
  }

}
