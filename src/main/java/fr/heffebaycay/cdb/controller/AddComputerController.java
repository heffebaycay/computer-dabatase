package fr.heffebaycay.cdb.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import fr.heffebaycay.cdb.dto.ComputerDTO;
import fr.heffebaycay.cdb.dto.mapper.CompanyMapper;
import fr.heffebaycay.cdb.dto.mapper.ComputerMapper;
import fr.heffebaycay.cdb.dto.validator.ComputerDTOValidator;
import fr.heffebaycay.cdb.model.Company;
import fr.heffebaycay.cdb.model.Computer;
import fr.heffebaycay.cdb.service.ICompanyService;
import fr.heffebaycay.cdb.service.IComputerService;

@Controller
@RequestMapping("/computers/add")
public class AddComputerController {

  private static final Logger LOGGER = LoggerFactory
                                         .getLogger(AddComputerController.class
                                             .getSimpleName());

  @Autowired
  private IComputerService    mComputerService;
  @Autowired
  private ICompanyService     mCompanyService;

  public AddComputerController() {

  }


  @RequestMapping(method = RequestMethod.GET)
  protected String doGet(ModelMap map) {
    LOGGER.debug("Call to AddComputerController::doGet()");
    
    // The view requires the list of all companies
    map.addAttribute("companies", CompanyMapper.toDTO(mCompanyService.findAll()));
    
    return "addComputer";
  }

  @RequestMapping(method = RequestMethod.POST)
  protected String doPost(ComputerDTO computerDTO, ModelMap map, RedirectAttributes redirectAttrs) {

    LOGGER.debug("Call to AddComputerController::doPost()");

    List<String> errors = new ArrayList<>();

    Computer computer = new Computer();

    // Storing given value in case something goes wrong and we need to display it back to the user
    map.addAttribute("computerNameValue", computerDTO.getName());
    map.addAttribute("dateIntroducedValue", computerDTO.getIntroduced());
    map.addAttribute("dateDiscontinuedValue", computerDTO.getDiscontinued());

    ComputerDTOValidator computerValidator = new ComputerDTOValidator();
    computerValidator.validate(computerDTO, errors);

    // Validating company is special as we need to check that the company selected
    // by the user does exist in the data source.
    
    Company company = null;
    Long companyId = computerDTO.getCompanyId();
    if(companyId > 0) {
      company = mCompanyService.findById(companyId);
      if(company == null) {
        errors.add("Selected company does not exist.");
      }
    }

    if (errors.isEmpty()) {
      computer = ComputerMapper.fromDTO(computerDTO);

      computer.setCompany(company);
      long computerId = mComputerService.create(computer);

      
      
      // All done, let's go to the edit page of this computer
      redirectAttrs.addAttribute("id", computerId);
      return "redirect:/computers/edit?id={id}&msg=addSuccess";
      

    } else {
      // Tough luck, the user didn't fill the form with valid input
      // Let's just redirect him back to the form and show him what he did wrong
      map.addAttribute("errors", errors);
      return doGet(map);
    }

  }

}
