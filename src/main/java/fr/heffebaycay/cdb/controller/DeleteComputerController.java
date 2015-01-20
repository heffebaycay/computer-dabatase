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

import fr.heffebaycay.cdb.service.IComputerService;
import fr.heffebaycay.cdb.service.impl.ComputerServiceMockImpl;

/**
 * Servlet implementation class DeleteComputerController
 */
@WebServlet("/computers/delete")
public class DeleteComputerController extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	private static final Logger LOGGER = LoggerFactory.getLogger(DeleteComputerController.class.getSimpleName());
	
	protected final IComputerService mComputerService;
	
	public DeleteComputerController() {
	  this.mComputerService = new ComputerServiceMockImpl();
	}
	
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	  
	  // The selection of computers to delete is passed as a CSV of ComputerIds
	  // e.g.: 123,42,13
	  String selection = request.getParameter("selection");
	  
	  String[] strComputerIds = selection.split(",");
	  List<Long> computerIds = new ArrayList<>();
	  
	  for(int i = 0; i < strComputerIds.length; i++) {
	    
	    try {
	      long computerId = Long.parseLong(strComputerIds[i]);
	      computerIds.add(computerId);
	    } catch(NumberFormatException e) {
	      LOGGER.warn("doPost(): Invalid number sent by user. {}", e);
	      response.sendError(response.SC_BAD_REQUEST, "");
	    }
	    
	  }
	  
	  for(Long id : computerIds) {
	    mComputerService.remove(id);
	  }
	  
	  response.sendRedirect(request.getContextPath() + "/?msg=removeSuccess");
	  
	}

}
