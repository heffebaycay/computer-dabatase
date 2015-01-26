package fr.heffebaycay.cdb.model;

import fr.heffebaycay.cdb.util.ComputerSortCriteria;
import fr.heffebaycay.cdb.util.SortOrder;

public class ComputerPageRequest extends PageRequest {

  protected ComputerSortCriteria sortCriterion;

  public ComputerPageRequest() {

  }

  public ComputerSortCriteria getSortCriterion() {
    return sortCriterion;
  }

  public void setSortCriterion(ComputerSortCriteria sortCriterion) {
    this.sortCriterion = sortCriterion;
  }

  public static class Builder {

    private ComputerPageRequest request;

    public Builder() {
      request = new ComputerPageRequest();
    }

    public Builder searchQuery(String searchQuery) {
      request.setSearchQuery(searchQuery);
      return this;
    }

    public Builder offset(Long offset) {
      request.setOffset(offset);
      return this;
    }

    public Builder nbRequested(Long nbRequested) {
      request.setNbRequested(nbRequested);
      return this;
    }

    public Builder sortOrder(SortOrder sortOrder) {
      request.setSortOrder(sortOrder);
      return this;
    }

    public Builder sortOrder(String strSortOrder) {
      SortOrder sortOrder;

      if ("desc".equals(strSortOrder)) {
        sortOrder = SortOrder.DESC;
      } else {
        sortOrder = SortOrder.ASC;
      }

      request.setSortOrder(sortOrder);

      return this;
    }

    public Builder sortCriterion(ComputerSortCriteria sortCriterion) {
      request.setSortCriterion(sortCriterion);
      return this;
    }

    public Builder sortCriterion(String strSortCriterion) {

      ComputerSortCriteria sortCriterion;

      if ("id".equals(strSortCriterion)) {
        sortCriterion = ComputerSortCriteria.ID;
      } else if ("name".equals(strSortCriterion)) {
        sortCriterion = ComputerSortCriteria.NAME;
      } else if ("introduced".equals(strSortCriterion)) {
        sortCriterion = ComputerSortCriteria.DATE_INTRODUCED;
      } else if ("discontinued".equals(strSortCriterion)) {
        sortCriterion = ComputerSortCriteria.DATE_DISCONTINUED;
      } else if ("company".equals(strSortCriterion)) {
        sortCriterion = ComputerSortCriteria.COMPANY_NAME;
      } else {
        // Default sort option
        sortCriterion = ComputerSortCriteria.ID;
      }

      request.setSortCriterion(sortCriterion);

      return this;
    }

    public ComputerPageRequest build() {
      return request;
    }

  }

}
