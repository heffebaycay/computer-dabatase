package fr.heffebaycay.cdb.controller;

import java.util.ArrayList;
import java.util.List;
import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.heffebaycay.cdb.model.Company;
import fr.heffebaycay.cdb.model.Computer;
import fr.heffebaycay.cdb.service.ICompanyService;
import fr.heffebaycay.cdb.service.IComputerService;
import fr.heffebaycay.cdb.service.manager.ServiceManager;

/**
 * Servlet implementation class EditComputerController
 */
@WebServlet("/computers/edit")
public class EditComputerController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	private static final Logger logger = LoggerFactory.getLogger(EditComputerController.class.getSimpleName());
	
	protected IComputerService mComputerService;
	protected ICompanyService mCompanyService;
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EditComputerController() {
        super();
        mComputerService = ServiceManager.INSTANCE.getComputerService();
        mCompanyService = ServiceManager.INSTANCE.getCompanyService();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	  
	  logger.debug("Received call to EditComputerController::doGet()");
	  
	  if(mComputerService == null) {
	    logger.error("doGet() : IComputerService instance is null");
	    return;
	  }
	  
	  if(mCompanyService == null) {
	    logger.error("doGet() : ICompanyService instance is null");
	    return;
	  }
	  
	  String strComputerId = request.getParameter("id");
	  if(strComputerId == null) {
	    strComputerId = (String) request.getAttribute("id");
	  }
	  
	  long computerId;
	  try {
	    computerId = Long.parseLong(strComputerId);
	  } catch(NumberFormatException e) {
	    // Id parameter provided isn't of type long
	    logger.warn("doGet() : Invalid id provided by user");
	    return;
	  }
	  
	  Computer computer = mComputerService.findById(computerId);
	  if(computer == null) {
	    // Unable to find any computer based on the given Id
	    logger.warn("doGet() : Inexisting computer requested by user");
	    return;
	  }
	  
	  List<Company> companies = mCompanyService.findAll();
	  request.setAttribute("companies", companies);
	  
	  request.setAttribute("computer", computer);
	  
	  RequestDispatcher rd = getServletContext().getRequestDispatcher(response.encodeURL("/WEB-INF/views/editComputer.jsp"));
	  rd.forward(request, response);
	  
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	  
	  logger.debug("Received call to EditComputerController:doPost()");
	  
	  List<String> errors = new ArrayList<>();
	  
	  String uComputerId = request.getParameter("computerId");
	  long computerId;
	  try {
	    computerId = Long.parseLong(uComputerId);
	  } catch(NumberFormatException e) {
	    logger.warn("doPost() :: User supplied an invalid computer id.");
	    return;
	  }
	  
	  logger.debug("doPost() :: ComputerId: " + computerId);
	  
	  Computer computer = mComputerService.findById(computerId);
	  if(computer == null) {
	    logger.warn("doPost() :: User supplied an Id for a non-existing computer.");
	    return;
	  }
	   
	  
	  // Computer name
	  String uName = request.getParameter("computerName");
	  // Storing given value in case something goes wrong and we need to display it back to the user
	  request.setAttribute("computerNameValue", uName);
	  
	  logger.debug("doPost() :: computerName: " + uName);
	  
	  if( uName == null || uName.isEmpty()) {
	    // Error: invalid computer name
	    errors.add("Computer name cannot be empty");
	  }
	  
	  String uIntroduced = request.getParameter("introduced");
	  logger.debug("doPost() :: dateIntroduced: " + uIntroduced);
	  
	  // Storing given value in case something goes wrong and we need to display it back to the user
	  request.setAttribute("dateIntroducedValue", uIntroduced);
	  try {
	    computer.setIntroduced(uIntroduced);
	  } catch(Exception e) {
	    errors.add("Invalid value for date introduced. Valid format is: YYYY-MM-DD.");
	  }
	  
	  
	  String uDiscontinued = request.getParameter("discontinued");
	  logger.debug("doPost() :: dateDiscontinued: " + uDiscontinued);
	  
	  request.setAttribute("dateDiscontinuedValue", uDiscontinued);
	  try {
	    computer.setDiscontinued(uDiscontinued);
	  } catch(Exception e) {
	    errors.add("Invalid value for date discontinued. Valid format is: YYYY-MM-DD.");
	  }
	  
	  String uCompanyId = request.getParameter("companyId");
	  
	  Company company = null;
	  long companyId;
	  try {
	    companyId = Long.parseLong(uCompanyId);
	    company = mCompanyService.findById(companyId);
	    if(company == null) {
	      logger.warn("doPost() :: Company does not exist");
	      errors.add("Company does not exist");
	    }
	  } catch(NumberFormatException e) {
	    logger.warn("doPost() :: User supplied an invalid company id");
	    errors.add("Invalid company Id");
	  }
	  
	  
	  
	  if(errors.size() == 0) {
	    computer.setName(uName);
	    computer.setCompany(company);
	    
	    computer.setIntroduced(uIntroduced);
	    computer.setDiscontinued(uDiscontinued);
	    
	    mComputerService.update(computer);
	    
	  }
	  
	  
	  request.setAttribute("errors", errors);
	  request.setAttribute("id", uComputerId);
	  
	  doGet(request, response);
	  
	  
	}

}
