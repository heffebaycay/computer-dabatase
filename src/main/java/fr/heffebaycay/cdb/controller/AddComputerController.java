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

import fr.heffebaycay.cdb.model.Company;
import fr.heffebaycay.cdb.model.Computer;
import fr.heffebaycay.cdb.service.ICompanyService;
import fr.heffebaycay.cdb.service.IComputerService;
import fr.heffebaycay.cdb.service.manager.ServiceManager;

/**
 * Servlet implementation class AddComputerController
 */
@WebServlet("/computers/add")
public class AddComputerController extends HttpServlet {
  private static final long   serialVersionUID = 1L;

  private static final Logger LOGGER           = LoggerFactory
                                                   .getLogger(AddComputerController.class
                                                       .getSimpleName());

  protected IComputerService  mComputerService;
  protected ICompanyService   mCompanyService;

  /**
   * @see HttpServlet#HttpServlet()
   */
  public AddComputerController() {
    super();
    mComputerService = ServiceManager.INSTANCE.getComputerService();
    mCompanyService = ServiceManager.INSTANCE.getCompanyService();
  }

  /**
   * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
   */
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    LOGGER.debug("Call to AddComputerController::doGet()");

    // The view requires the list of all companies
    request.setAttribute("companies", mCompanyService.findAll());

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

    String uName = request.getParameter("computerName");

    // Storing given value in case something goes wrong and we need to display it back to the user
    request.setAttribute("computerNameValue", uName);
    LOGGER.debug("doPost() :: computerName: " + uName);

    if (uName == null || uName.isEmpty()) {
      // Error: invalid computer name
      errors.add("Computer name cannot be empty");
    } else {
      computer.setName(uName);
    }

    String uIntroduced = request.getParameter("introduced");
    LOGGER.debug("doPost() :: dateIntroduced: " + uIntroduced);

    // Storing given value in case something goes wrong and we need to display it back to the user
    request.setAttribute("dateIntroducedValue", uIntroduced);
    try {
      computer.setIntroduced(uIntroduced);
    } catch (Exception e) {
      LOGGER.warn("Invalid value for date introduced. Exception: {}", e);
      errors.add("Invalid value for date introduced. Valid format is: YYYY-MM-DD.");
    }

    String uDiscontinued = request.getParameter("discontinued");
    LOGGER.debug("doPost() :: dateDiscontinued: " + uDiscontinued);

    request.setAttribute("dateDiscontinuedValue", uDiscontinued);
    try {
      computer.setDiscontinued(uDiscontinued);
    } catch (Exception e) {
      LOGGER.warn("Invalid value for date discontinued. Exception: {}", e);
      errors.add("Invalid value for date discontinued. Valid format is: YYYY-MM-DD.");
    }

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
