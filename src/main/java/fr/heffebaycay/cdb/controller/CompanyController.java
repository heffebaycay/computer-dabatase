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

import fr.heffebaycay.cdb.dto.CompanyDTO;
import fr.heffebaycay.cdb.dto.mapper.CompanyMapper;
import fr.heffebaycay.cdb.model.Company;
import fr.heffebaycay.cdb.service.ICompanyService;
import fr.heffebaycay.cdb.service.manager.ServiceManager;
import fr.heffebaycay.cdb.util.AppSettings;
import fr.heffebaycay.cdb.util.CompanySortCriteria;
import fr.heffebaycay.cdb.util.SortOrder;
import fr.heffebaycay.cdb.wrapper.SearchWrapper;

/**
 * Servlet implementation class CompanyController
 */
@WebServlet("/companies/list")
public class CompanyController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	private static final Logger LOGGER = LoggerFactory.getLogger(CompanyController.class);
	
	protected ICompanyService mCompanyService;
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CompanyController() {
        super();
        mCompanyService = ServiceManager.INSTANCE.getCompanyService();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		LOGGER.debug("Call to doGet()");
		
		String msgResult = request.getParameter("msg");
	    if("removeSuccess".equals(msgResult)) {
	      request.setAttribute("bRemoveSuccess", true);
	    }
		
		List<CompanyDTO> companies = null;
		SearchWrapper<Company> searchWrapper;
		
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
		} catch(NumberFormatException e) {
			LOGGER.warn("doGet(): Invalid page argument", e);
			currentPage = 1;
		}
		request.setAttribute("currentPage", currentPage);
		
		// Page parameters
		long offset = (currentPage - 1) * nbResultsPerPage;
		
		// Fetching search parameters
		String searchQuery = request.getParameter("search");
		if ("%".equals(searchQuery)) {
			searchQuery = "";
		}
		
		// Fetching sorting parameters
		String uSortBy = request.getParameter("sortBy");
		String uSortOrder = request.getParameter("order");
		SortOrder sortOrder;
		CompanySortCriteria sortCriterion;
		
		// Setting up sort order
		if ( "desc".equals(uSortOrder) ) {
			sortOrder = SortOrder.DESC;
			request.setAttribute("sortOrder", "desc");
		} else {
			sortOrder = SortOrder.ASC;
			request.setAttribute("sortOrder", "asc");
		}
		
		// Setting up sort criterion
		if ( "id".equals(uSortBy) ) {
			sortCriterion = CompanySortCriteria.ID;
			request.setAttribute("sortCriterion", "id");
		} else if ( "name".equals(uSortBy) ) {
			sortCriterion = CompanySortCriteria.NAME;
			request.setAttribute("sortCriterion", "name");
		} else {
			sortCriterion = CompanySortCriteria.ID;
			request.setAttribute("sortCriterion", "id");
		}
		
		if( searchQuery != null && !searchQuery.isEmpty() ) {
			searchWrapper = mCompanyService.findByName(searchQuery, offset, nbResultsPerPage, sortCriterion, sortOrder);
			request.setAttribute("searchQuery", searchQuery);
		} else {
			searchWrapper = mCompanyService.findAll(offset, nbResultsPerPage, sortCriterion, sortOrder);
		}
		
		companies = CompanyMapper.toDTO(searchWrapper.getResults());
		
		request.setAttribute("totalPage", searchWrapper.getTotalPage());
		request.setAttribute("totalCount", searchWrapper.getTotalCount());
		request.setAttribute("companies", companies);
		
		
		RequestDispatcher rd = getServletContext().getRequestDispatcher(response.encodeURL("/WEB-INF/views/companies.jsp"));
		rd.forward(request, response);
		
		
	}

}
