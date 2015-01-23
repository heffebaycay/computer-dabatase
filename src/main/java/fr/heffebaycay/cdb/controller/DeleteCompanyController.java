package fr.heffebaycay.cdb.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.heffebaycay.cdb.service.ICompanyService;
import fr.heffebaycay.cdb.service.manager.ServiceManager;

/**
 * Servlet implementation class DeleteCompanyController
 */
@WebServlet("/companies/delete")
public class DeleteCompanyController extends HttpServlet {
  private static final long serialVersionUID = 1L;

  private static final Logger LOGGER = LoggerFactory.getLogger(DeleteCompanyController.class);
  
  protected ICompanyService mCompanyService;
  
  /**
   * @see HttpServlet#HttpServlet()
   */
  public DeleteCompanyController() {
    super();
    mCompanyService = ServiceManager.INSTANCE.getCompanyService();
  }

  /**
   * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
   */
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    String selection = request.getParameter("selection");

    String[] strCompanyIds = selection.split(",");
    List<Long> companyIds = new ArrayList<>();

    for (int i = 0; i < strCompanyIds.length; i++) {
      try {
        long companyId = Long.parseLong(strCompanyIds[i]);
        companyIds.add(companyId);
        
      } catch(NumberFormatException e) {
        LOGGER.warn("doPost(): Invalid number sent by user.", e);
        response.sendError(response.SC_BAD_REQUEST, "");
      }
    }
    
    for(Long id : companyIds) {
      mCompanyService.remove(id);
    }
    
    
    response.sendRedirect(request.getContextPath() + "/companies/list?msg=removeSuccess");

  }

}
