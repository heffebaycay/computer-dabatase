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

import fr.heffebaycay.cdb.dto.ComputerDTO;
import fr.heffebaycay.cdb.dto.mapper.CompanyMapper;
import fr.heffebaycay.cdb.dto.mapper.ComputerMapper;
import fr.heffebaycay.cdb.dto.validator.ComputerDTOValidator;
import fr.heffebaycay.cdb.model.Company;
import fr.heffebaycay.cdb.model.Computer;
import fr.heffebaycay.cdb.service.ICompanyService;
import fr.heffebaycay.cdb.service.IComputerService;

/**
 * Servlet implementation class AddComputerController
 */
@WebServlet("/computers/add")
public class AddComputerController extends AbstractSpringHttpServlet {
  private static final long   serialVersionUID = 1L;

  private static final Logger LOGGER           = LoggerFactory
                                                   .getLogger(AddComputerController.class
                                                       .getSimpleName());
  
  @Autowired
  private IComputerService  mComputerService;
  @Autowired
  private ICompanyService   mCompanyService;

  /**
   * @see HttpServlet#HttpServlet()
   */
  public AddComputerController() {
    super();
  }

  /**
   * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
   */
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    LOGGER.debug("Call to AddComputerController::doGet()");

    // The view requires the list of all companies
    request.setAttribute("companies", CompanyMapper.toDTO( mCompanyService.findAll() ));

    RequestDispatcher rd = getServletContext().getRequestDispatcher(
        response.encodeURL("/WEB-INF/views/addComputer.jsp"));
    rd.forward(request, response);

  }

  /**
   * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
   */
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    LOGGER.debug("Call to AddComputerController::doPost()");

    List<String> errors = new ArrayList<>();

    Computer computer = new Computer();
    ComputerDTO.Builder computerBuilder = new ComputerDTO.Builder();

    String uName = request.getParameter("computerName");
    String uIntroduced = request.getParameter("introduced");
    String uDiscontinued = request.getParameter("discontinued");

    // Storing given value in case something goes wrong and we need to display it back to the user
    request.setAttribute("computerNameValue", uName);
    request.setAttribute("dateIntroducedValue", uIntroduced);
    request.setAttribute("dateDiscontinuedValue", uDiscontinued);
    
    ComputerDTO computerDTO = computerBuilder.name(uName).introduced(uIntroduced).discontinued(uDiscontinued).build();
    
    ComputerDTOValidator computerValidator = new ComputerDTOValidator();
    computerValidator.validate(computerDTO, errors);
    
    // Validating company is special as we need to check that the company selected
    // by the user does exist in the data source.
    String uCompanyId = request.getParameter("companyId");
    Company company = null;
    long companyId;
    try {
      companyId = Long.parseLong(uCompanyId);
      if (companyId > -1) {
        company = mCompanyService.findById(companyId);
      }
    } catch (NumberFormatException e) {
      LOGGER.warn("doPost() :: User supplied an invalid company id");
      errors.add("Invalid company Id");
    }

    if (errors.isEmpty()) {
      computer = ComputerMapper.fromDTO(computerDTO);
      
      computer.setCompany(company);
      long computerId = mComputerService.create(computer);

      // All done, let's go to the edit page of this computer
      response.sendRedirect(request.getContextPath() + "/computers/edit?id=" + computerId
          + "&msg=addSuccess");

    } else {
      // Tough luck, the user didn't fill the form with valid input
      // Let's just redirect him back to the form and show him what he did wrong
      request.setAttribute("errors", errors);
      
      doGet(request, response);
    }

  }

}
