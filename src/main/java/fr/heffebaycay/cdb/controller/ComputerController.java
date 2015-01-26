package fr.heffebaycay.cdb.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.heffebaycay.cdb.dto.ComputerDTO;
import fr.heffebaycay.cdb.dto.mapper.ComputerMapper;
import fr.heffebaycay.cdb.model.Computer;
import fr.heffebaycay.cdb.service.IComputerService;
import fr.heffebaycay.cdb.service.manager.ServiceManager;
import fr.heffebaycay.cdb.util.AppSettings;
import fr.heffebaycay.cdb.util.ComputerSortCriteria;
import fr.heffebaycay.cdb.util.SortOrder;
import fr.heffebaycay.cdb.wrapper.SearchWrapper;

/**
 * Servlet implementation class ComputerController
 */
@WebServlet("/computers/list")
public class ComputerController extends HttpServlet {

  private static final long  serialVersionUID = 1L;

  private static final Logger LOGGER = LoggerFactory.getLogger(ComputerController.class.getSimpleName());
  
  protected IComputerService mComputerService;

  /**
   * @see HttpServlet#HttpServlet()
   */
  public ComputerController() {
    super();
    mComputerService = ServiceManager.INSTANCE.getComputerService();
  }

  /**
   * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
   */
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    LOGGER.debug("Call to ComputerController::doGet()");
    
    String msgResult = request.getParameter("msg");
    if("removeSuccess".equals(msgResult)) {
      request.setAttribute("bRemoveSuccess", true);
    }
    
    List<ComputerDTO> computers = null;
    SearchWrapper<Computer> searchWrapper;

    long nbResultsPerPage = AppSettings.NB_RESULTS_PAGE;

    long currentPage;
    
    String strPage = request.getParameter("p");
    if(strPage == null || strPage.isEmpty()) {
      strPage = "1";
    }
    
    
    try {
      currentPage = Long.parseLong(strPage);
      if(currentPage < 0) {
        currentPage = 1;
      }
    } catch (NumberFormatException e) {
      LOGGER.error("Invalid page argument");
      currentPage = 1;
    }
    request.setAttribute("currentPage", currentPage);
    
    // Page parameters
    long offset = (currentPage - 1) * nbResultsPerPage;
    
    // Fetching search parameters
    String searchQuery = request.getParameter("search");
    if("%".equals(searchQuery)) {
      searchQuery = "";
    }
    
    // Fetching sorting parameters
    String uSortBy = request.getParameter("sortBy");
    String uSortOrder = request.getParameter("order");
    SortOrder sortOrder;
    ComputerSortCriteria sortCriterion;
    
    // Setting up sort order
    if ("desc".equals(uSortOrder)) {
      sortOrder = SortOrder.DESC;
      request.setAttribute("sortOrder", "desc");
    } else {
      sortOrder = SortOrder.ASC;
      request.setAttribute("sortOrder", "asc");
    }
    
    // Setting up sort criterion
    if( "id".equals(uSortBy) ) {
      sortCriterion = ComputerSortCriteria.ID;
      request.setAttribute("sortCriterion", "id");
    } else if( "name".equals(uSortBy) ) {
      sortCriterion = ComputerSortCriteria.NAME;
      request.setAttribute("sortCriterion", "name");
    } else if( "introduced".equals(uSortBy) ) {
      sortCriterion = ComputerSortCriteria.DATE_INTRODUCED;
      request.setAttribute("sortCriterion", "introduced");
    } else if( "discontinued".equals(uSortBy) ) {
      sortCriterion = ComputerSortCriteria.DATE_DISCONTINUED;
      request.setAttribute("sortCriterion", "discontinued");
    } else if( "company".equals(uSortBy) ) {
      sortCriterion = ComputerSortCriteria.COMPANY_NAME;
      request.setAttribute("sortCriterion", "discontinued");
    } else {
      sortCriterion = ComputerSortCriteria.ID;
      request.setAttribute("sortCriterion", "id");
    }
    
    if(searchQuery != null && !searchQuery.isEmpty()) {
      searchWrapper = mComputerService.findByName(searchQuery, offset, nbResultsPerPage, sortCriterion, sortOrder);
      request.setAttribute("searchQuery", searchQuery);
    } else {
      searchWrapper = mComputerService.findAll(offset, nbResultsPerPage, sortCriterion, sortOrder);
    }
    
    computers = ComputerMapper.toDTO( searchWrapper.getResults() );
    
    request.setAttribute("totalPage", searchWrapper.getTotalPage());
    request.setAttribute("totalCount", searchWrapper.getTotalQueryCount());
    request.setAttribute("computers", computers);
       
    RequestDispatcher rd = getServletContext().getRequestDispatcher(response.encodeURL("/WEB-INF/views/dashboard.jsp"));
    rd.forward(request, response);

  }

}
