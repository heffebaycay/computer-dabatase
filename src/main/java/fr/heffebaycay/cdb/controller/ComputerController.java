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
import org.springframework.beans.factory.annotation.Autowired;

import fr.heffebaycay.cdb.dto.ComputerDTO;
import fr.heffebaycay.cdb.dto.mapper.ComputerMapper;
import fr.heffebaycay.cdb.model.Computer;
import fr.heffebaycay.cdb.model.ComputerPageRequest;
import fr.heffebaycay.cdb.service.IComputerService;
import fr.heffebaycay.cdb.util.AppSettings;
import fr.heffebaycay.cdb.wrapper.SearchWrapper;

/**
 * Servlet implementation class ComputerController
 */
@WebServlet("/computers/list")
public class ComputerController extends AbstractSpringHttpServlet {

  private static final long  serialVersionUID = 1L;

  private static final Logger LOGGER = LoggerFactory.getLogger(ComputerController.class.getSimpleName());
  
  @Autowired
  private IComputerService mComputerService;

  /**
   * @see HttpServlet#HttpServlet()
   */
  public ComputerController() {
    super();
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
    
    ComputerPageRequest pageRequest = new ComputerPageRequest.Builder()
                                                              .sortOrder(uSortOrder)
                                                              .sortCriterion(uSortBy)
                                                              .searchQuery(searchQuery)
                                                              .offset(offset)
                                                              .nbRequested(nbResultsPerPage)
                                                              .build();
    
    
    if(searchQuery != null && !searchQuery.isEmpty()) {
      searchWrapper = mComputerService.findByName(pageRequest);
    } else {
      searchWrapper = mComputerService.findAll(pageRequest);
    }
    
    
    request.setAttribute("searchWrapper", searchWrapper);
       
    RequestDispatcher rd = getServletContext().getRequestDispatcher(response.encodeURL("/WEB-INF/views/dashboard.jsp"));
    rd.forward(request, response);

  }

}
