package fr.heffebaycay.cdb.controller;

import java.io.IOException;
import java.util.ArrayList;
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

import fr.heffebaycay.cdb.dto.CompanyDTO;
import fr.heffebaycay.cdb.dto.ComputerDTO;
import fr.heffebaycay.cdb.dto.mapper.CompanyMapper;
import fr.heffebaycay.cdb.dto.mapper.ComputerMapper;
import fr.heffebaycay.cdb.dto.validator.ComputerDTOValidator;
import fr.heffebaycay.cdb.model.Company;
import fr.heffebaycay.cdb.model.Computer;
import fr.heffebaycay.cdb.service.ICompanyService;
import fr.heffebaycay.cdb.service.IComputerService;

/**
 * Servlet implementation class EditComputerController
 */
@WebServlet("/computers/edit")
public class EditComputerController extends AbstractSpringHttpServlet {
	private static final long serialVersionUID = 1L;
       
	private static final Logger LOGGER = LoggerFactory.getLogger(EditComputerController.class.getSimpleName());
	
	@Autowired
	private IComputerService mComputerService;
	@Autowired
	private ICompanyService mCompanyService;
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EditComputerController() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	  
	  LOGGER.debug("Received call to EditComputerController::doGet()");
	  
	  if(mComputerService == null) {
	    LOGGER.error("doGet() : IComputerService instance is null");
	    return;
	  }
	  
	  if(mCompanyService == null) {
	    LOGGER.error("doGet() : ICompanyService instance is null");
	    return;
	  }
	  
	  String addOk = request.getParameter("msg");
	  if( "addSuccess".equals(addOk) ) {
	    request.setAttribute("bAddSuccess", true);
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
	    LOGGER.warn("doGet() : Invalid id provided by user");
	    return;
	  }
	  
	  ComputerDTO computerDTO = ComputerMapper.toDTO( mComputerService.findById(computerId) );
	  if(computerDTO == null) {
	    // Unable to find any computer based on the given Id
	    LOGGER.warn("doGet() : Inexisting computer requested by user");
	    return;
	  }
	  
	  List<CompanyDTO> companies = CompanyMapper.toDTO( mCompanyService.findAll() );
	  
	  request.setAttribute("companies", companies);
	  request.setAttribute("computer", computerDTO);
	  
	  RequestDispatcher rd = getServletContext().getRequestDispatcher(response.encodeURL("/WEB-INF/views/editComputer.jsp"));
	  rd.forward(request, response);
	  
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	  
	  LOGGER.debug("Received call to EditComputerController:doPost()");
	  
	  List<String> errors = new ArrayList<>();
	  
	  String uComputerId = request.getParameter("computerId");
	  long computerId;
	  try {
	    computerId = Long.parseLong(uComputerId);
	  } catch(NumberFormatException e) {
	    LOGGER.warn("doPost() :: User supplied an invalid computer id.");
	    return;
	  }
	  
	  LOGGER.debug("doPost() :: ComputerId: " + computerId);
	  
	  Computer computer = mComputerService.findById(computerId);
	  if(computer == null) {
	    LOGGER.warn("doPost() :: User supplied an Id for a non-existing computer.");
	    return;
	  }
	  
	  ComputerDTO.Builder computerBuilder = new ComputerDTO.Builder();
	  
	  // Fetching the request parameters
	  String uName = request.getParameter("computerName");
	  String uIntroduced = request.getParameter("introduced");
	  String uDiscontinued = request.getParameter("discontinued");
	  String uCompanyId = request.getParameter("companyId");
	  
	   // Storing given value in case something goes wrong and we need to display it back to the user
      request.setAttribute("computerNameValue", uName);
      request.setAttribute("dateIntroducedValue", uIntroduced);
      request.setAttribute("dateDiscontinuedValue", uDiscontinued);
      request.setAttribute("companyIdValue", uCompanyId);
	  
	  ComputerDTO computerDTO = computerBuilder.name(uName).introduced(uIntroduced).discontinued(uDiscontinued).id(computerId).build();
	  
	  ComputerDTOValidator computerValidator = new ComputerDTOValidator();
	  computerValidator.validate(computerDTO, errors);
	  
	  // Creating the Company object
	  Company company = null;
	  long companyId;
	  try {
	    companyId = Long.parseLong(uCompanyId);
	    company = mCompanyService.findById(companyId);
	    if(company == null) {
	      LOGGER.warn("doPost() :: Company does not exist");
	      errors.add("Company does not exist");
	    }
	  } catch(NumberFormatException e) {
	    LOGGER.warn("doPost() :: User supplied an invalid company id");
	    errors.add("Invalid company Id");
	  }
	  
	  
	  if(errors.isEmpty()) {
	    
	    ComputerMapper.updateDAO(computer, computerDTO);
	    computer.setCompany(company);
	    
	    mComputerService.update(computer);
	  }
	  
	  
	  request.setAttribute("errors", errors);
	  request.setAttribute("id", uComputerId);
	  
	  doGet(request, response);
	  
	  
	}

}
