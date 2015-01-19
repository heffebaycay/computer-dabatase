package fr.heffebaycay.cdb.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import fr.heffebaycay.cdb.model.Company;
import fr.heffebaycay.cdb.service.ICompanyService;
import fr.heffebaycay.cdb.service.impl.CompanyServiceMockImpl;
import fr.heffebaycay.cdb.service.manager.ServiceManager;
import fr.heffebaycay.cdb.wrapper.SearchWrapper;

/**
 * Servlet implementation class CompanyController
 */
@WebServlet("/company/list")
public class CompanyController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	ICompanyService companyService;
	
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CompanyController() {
        super();
        companyService = ServiceManager.INSTANCE.getCompanyService();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	  if(companyService == null) {
	    return;
	  }
	  
	  List<Company> companies = null;
	  SearchWrapper<Company> searchWrapper;
	  
	  int nbCompaniesPerPage = 10;
	  
	  String strPage = request.getParameter("p");
	  if(strPage == null || strPage.isEmpty()) {
	    strPage = "1";
	  }
	  
	  long iPage;
	  
	  try {
	    iPage = Long.parseLong(strPage);
	  } catch(NumberFormatException e) {
	    iPage = 1;
	  }
	  
	  request.setAttribute("currentPage", iPage);
	  
	  	  
	  searchWrapper = companyService.findAll((iPage - 1) * nbCompaniesPerPage, nbCompaniesPerPage);
	  companies = searchWrapper.getResults();
	  
	  long totalCompanyCount = searchWrapper.getTotalQueryCount();
	  long totalPage = searchWrapper.getTotalPage();
	  
	  request.setAttribute("totalPage", totalPage);
	  request.setAttribute("totalCount", totalCompanyCount);
	  
	  request.setAttribute("companies", companies);
	  
	  RequestDispatcher rd = getServletContext().getRequestDispatcher(response.encodeURL("/WEB-INF/views/dashboard.html"));
	  
	  rd.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}
	
	

}
