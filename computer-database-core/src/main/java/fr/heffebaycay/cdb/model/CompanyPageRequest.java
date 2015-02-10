package fr.heffebaycay.cdb.model;

import fr.heffebaycay.cdb.model.ComputerPageRequest.Builder;
import fr.heffebaycay.cdb.util.CompanySortCriteria;
import fr.heffebaycay.cdb.util.SortOrder;

public class CompanyPageRequest extends PageRequest {

  protected CompanySortCriteria sortCriterion;

  public CompanyPageRequest() {

  }

  public CompanySortCriteria getSortCriterion() {
    return sortCriterion;
  }

  public void setSortCriterion(CompanySortCriteria sortCriterion) {
    this.sortCriterion = sortCriterion;
  }

  public static class Builder {

    private CompanyPageRequest request;

    public Builder() {
      request = new CompanyPageRequest();
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

    public Builder sortCriterion(CompanySortCriteria sortCriterion) {
      request.setSortCriterion(sortCriterion);
      return this;
    }

    public Builder sortCriterion(String strSortCriterion) {
      CompanySortCriteria sortCriterion;

      if ("id".equals(strSortCriterion)) {
        sortCriterion = CompanySortCriteria.ID;
      } else if ("name".equals(strSortCriterion)) {
        sortCriterion = CompanySortCriteria.NAME;
      } else {
        sortCriterion = CompanySortCriteria.ID;
      }

      request.setSortCriterion(sortCriterion);
      return this;
    }

    public CompanyPageRequest build() {
      return request;
    }

  }

}
