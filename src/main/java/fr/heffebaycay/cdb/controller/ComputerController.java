package fr.heffebaycay.cdb.controller;

import javax.servlet.http.HttpServlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import fr.heffebaycay.cdb.model.Computer;
import fr.heffebaycay.cdb.model.ComputerPageRequest;
import fr.heffebaycay.cdb.model.view.DashboardRequest;
import fr.heffebaycay.cdb.service.IComputerService;
import fr.heffebaycay.cdb.util.AppSettings;
import fr.heffebaycay.cdb.wrapper.SearchWrapper;

@Controller
@RequestMapping("/computers/list")
public class ComputerController {

  private static final Logger LOGGER = LoggerFactory.getLogger(ComputerController.class
                                         .getSimpleName());

  @Autowired
  private IComputerService    mComputerService;

  public ComputerController() {

  }

  @RequestMapping(method = RequestMethod.GET)
  protected String doGet(DashboardRequest requestModel, ModelMap map) {

    LOGGER.debug("Call to ComputerController::doGet()");

    if ("removeSuccess".equals(requestModel.getMsg())) {
      map.addAttribute("bRemoveSuccess", true);
    }

    SearchWrapper<Computer> searchWrapper;

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

    ComputerPageRequest pageRequest = new ComputerPageRequest.Builder()
        .sortOrder(requestModel.getOrder())
        .sortCriterion(requestModel.getSortBy())
        .searchQuery(searchQuery)
        .offset(offset)
        .nbRequested(nbResultsPerPage)
        .build();

    if (searchQuery != null && !searchQuery.isEmpty()) {
      searchWrapper = mComputerService.findByName(pageRequest);
    } else {
      searchWrapper = mComputerService.findAll(pageRequest);
    }

    map.addAttribute("searchWrapper", searchWrapper);

    return "dashboard";

  }

}
