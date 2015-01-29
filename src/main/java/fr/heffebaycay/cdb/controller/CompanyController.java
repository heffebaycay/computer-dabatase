package fr.heffebaycay.cdb.controller;

import javax.servlet.RequestDispatcher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import fr.heffebaycay.cdb.model.Company;
import fr.heffebaycay.cdb.model.CompanyPageRequest;
import fr.heffebaycay.cdb.model.view.DashboardRequest;
import fr.heffebaycay.cdb.service.ICompanyService;
import fr.heffebaycay.cdb.util.AppSettings;
import fr.heffebaycay.cdb.wrapper.SearchWrapper;

@Controller
@RequestMapping("/companies/list")
public class CompanyController {

  private static final Logger LOGGER = LoggerFactory.getLogger(CompanyController.class);

  @Autowired
  private ICompanyService     mCompanyService;

  public CompanyController() {
    super();
  }

  @RequestMapping(method = RequestMethod.GET)
  protected String doGet(DashboardRequest requestModel, ModelMap map) {
    LOGGER.debug("Call to doGet()");

    if ("removeSuccess".equals(requestModel.getMsg())) {
      map.addAttribute("bRemoveSuccess", true);
    }

    SearchWrapper<Company> searchWrapper;

    long nbResultsPerPage = AppSettings.NB_RESULTS_PAGE;

    long currentPage = requestModel.getCurrentPage();
    if (currentPage < 1) {
      currentPage = 1;
    }

    // Page parameters
    long offset = (currentPage - 1) * nbResultsPerPage;

    // Fetching search parameters
    String searchQuery = requestModel.getSearchQuery();
    if ("%".equals(searchQuery)) {
      searchQuery = "";
    }

    CompanyPageRequest pageRequest = new CompanyPageRequest.Builder()
        .sortOrder(requestModel.getOrder())
        .sortCriterion(requestModel.getSortBy())
        .searchQuery(searchQuery)
        .offset(offset)
        .nbRequested(nbResultsPerPage)
        .build();

    if (searchQuery != null && !searchQuery.isEmpty()) {
      searchWrapper = mCompanyService.findByName(pageRequest);
    } else {
      searchWrapper = mCompanyService.findAll(pageRequest);
    }

    map.addAttribute("searchWrapper", searchWrapper);

    return "companies";
  }

}
