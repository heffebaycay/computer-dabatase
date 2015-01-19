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

import fr.heffebaycay.cdb.model.Computer;
import fr.heffebaycay.cdb.service.IComputerService;
import fr.heffebaycay.cdb.service.impl.ComputerServiceMockImpl;
import fr.heffebaycay.cdb.util.AppSettings;
import fr.heffebaycay.cdb.wrapper.SearchWrapper;

/**
 * Servlet implementation class ComputerController
 */
@WebServlet("")
public class ComputerController extends HttpServlet {

  private static final long  serialVersionUID = 1L;

  private static final Logger logger = LoggerFactory.getLogger(ComputerController.class.getSimpleName());
  
  protected IComputerService mComputerService;

  /**
   * @see HttpServlet#HttpServlet()
   */
  public ComputerController() {
    super();
    mComputerService = new ComputerServiceMockImpl();
  }

  /**
   * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
   */
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    logger.debug("Call to ComputerController::doGet()");
    
    List<Computer> computers = null;
    SearchWrapper<Computer> searchWrapper;

    long nbResultsPerPage = AppSettings.NB_RESULTS_PAGE;

    long currentPage;
    
    String strPage = request.getParameter("p");
    if(strPage == null || strPage.isEmpty()) {
      strPage = "1";
    }
    
    
    try {
      currentPage = Long.parseLong(strPage);
    } catch (NumberFormatException e) {
      logger.error("Invalid page argument");
      currentPage = 1;
    }
    request.setAttribute("currentPage", currentPage);
    
    searchWrapper = mComputerService.findAll((currentPage - 1) * nbResultsPerPage, nbResultsPerPage);
    computers = searchWrapper.getResults();
    
    request.setAttribute("totalPage", searchWrapper.getTotalPage());
    request.setAttribute("totalCount", searchWrapper.getTotalQueryCount());
    request.setAttribute("computers", computers);
    
    
    RequestDispatcher rd = getServletContext().getRequestDispatcher(response.encodeURL("/WEB-INF/views/dashboard.jsp"));
    rd.forward(request, response);

  }

  /**
   * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
   */
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    // TODO Auto-generated method stub
  }

}
