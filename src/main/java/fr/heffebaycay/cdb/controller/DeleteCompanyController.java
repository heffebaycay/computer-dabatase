package fr.heffebaycay.cdb.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import fr.heffebaycay.cdb.service.ICompanyService;
import fr.heffebaycay.cdb.web.exception.InvalidCompanyException;


@Controller
@RequestMapping("/companies/delete")
public class DeleteCompanyController {

  private static final Logger LOGGER = LoggerFactory.getLogger(DeleteCompanyController.class);
  
  @Autowired
  private ICompanyService mCompanyService;
  
  public DeleteCompanyController() {
    super();
  }

  @RequestMapping(method = RequestMethod.POST)
  protected String doPost(String selection) {

    String[] strCompanyIds = selection.split(",");
    List<Long> companyIds = new ArrayList<>();

    for (int i = 0; i < strCompanyIds.length; i++) {
      try {
        long companyId = Long.parseLong(strCompanyIds[i]);
        companyIds.add(companyId);
        
      } catch(NumberFormatException e) {
        LOGGER.warn("doPost(): Invalid number sent by user.", e);
        throw new InvalidCompanyException();
      }
    }
    
    for(Long id : companyIds) {
      mCompanyService.remove(id);
    }
    
    return "redirect:/companies/list?msg=removeSuccess";

  }

}
