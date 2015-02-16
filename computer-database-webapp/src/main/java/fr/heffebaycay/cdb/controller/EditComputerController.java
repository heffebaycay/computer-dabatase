package fr.heffebaycay.cdb.controller;

import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import fr.heffebaycay.cdb.dto.CompanyDTO;
import fr.heffebaycay.cdb.dto.ComputerDTO;
import fr.heffebaycay.cdb.dto.mapper.CompanyMapper;
import fr.heffebaycay.cdb.dto.mapper.ComputerMapper;
import fr.heffebaycay.cdb.model.Company;
import fr.heffebaycay.cdb.model.Computer;
import fr.heffebaycay.cdb.service.ICompanyService;
import fr.heffebaycay.cdb.service.IComputerService;
import fr.heffebaycay.cdb.util.Constants;
import fr.heffebaycay.cdb.web.exception.ComputerNotFoundException;
import fr.heffebaycay.cdb.web.exception.ServiceInitializationException;

@Controller
@RequestMapping("/computers/edit")
public class EditComputerController {
  private static final Logger LOGGER = LoggerFactory.getLogger(EditComputerController.class
                                         .getSimpleName());

  private static final String ATTR_COMPANIES = "companies";
  private static final String ATTR_COMPUTER_DTO = "computerDTO";
  
  @Autowired
  private IComputerService    mComputerService;
  @Autowired
  private ICompanyService     mCompanyService;

  @Autowired
  private ComputerMapper      computerMapper;
  @Autowired
  private CompanyMapper       companyMapper;

  public EditComputerController() {
    super();
  }

  @RequestMapping(method = RequestMethod.GET)
  protected String doGet(Long id, @RequestParam(required = false) String msg, ModelMap map) {

    LOGGER.debug("Received call to EditComputerController::doGet()");

    if (mComputerService == null) {
      LOGGER.error("doGet() : IComputerService instance is null");
      throw new ServiceInitializationException();
    }

    if (mCompanyService == null) {
      LOGGER.error("doGet() : ICompanyService instance is null");
      throw new ServiceInitializationException();
    }

    if ("addSuccess".equals(msg)) {
      map.addAttribute("bAddSuccess", true);
    } else if ("editSuccess".equals(msg)) {
      map.addAttribute("msgSuccess", true);
    }

    ComputerDTO computerDTO = computerMapper.toDTO(mComputerService.findById(id));
    if (computerDTO == null) {
      // Unable to find any computer based on the given Id
      LOGGER.warn("doGet() : Inexisting computer requested by user");
      throw new ComputerNotFoundException();
    }

    map.addAttribute(ATTR_COMPUTER_DTO, computerDTO);

    List<CompanyDTO> companies = companyMapper.toDTO(mCompanyService.findAll());

    map.addAttribute(ATTR_COMPANIES, companies);

    return Constants.JSP_EDIT_COMPUTER;
  }

  @RequestMapping(method = RequestMethod.POST)
  protected String doPost(@Valid ComputerDTO computerDTO, BindingResult result, ModelMap map,
      RedirectAttributes redirectAttrs) {

    LOGGER.debug("Received call to EditComputerController:doPost()");
    LOGGER.debug("doPost() :: ComputerId: " + computerDTO.getId());

    Computer computer = mComputerService.findById(computerDTO.getId());
    if (computer == null) {
      LOGGER.warn("doPost() :: User supplied an Id for a non-existing computer.");
      // 40X ?
    }

    // Creating the Company object
    Company company = null;

    if (computerDTO.getCompanyId() > 0) {
      company = mCompanyService.findById(computerDTO.getCompanyId());
      if (company == null) {
        LOGGER.warn("doPost() :: Company does not exist");
        result.addError(new ObjectError("companyId", "Selected company does not exist"));
      }
    }

    if (result.hasErrors()) {
      // Validation failed
      LOGGER.debug("doPost(): ComputerDTO has errors [JSR 303 Validation]");
      
      map.addAttribute("msgValidationFailed", true);
      
      // The edit page needs the list of companies
      List<CompanyDTO> companies = companyMapper.toDTO(mCompanyService.findAll());
      map.addAttribute(ATTR_COMPANIES, companies);

      map.addAttribute(ATTR_COMPUTER_DTO, computerDTO);
      // Returning the editComputer view
      return Constants.JSP_EDIT_COMPUTER;

    } else {
      // Validation succeeded
      computerMapper.updateDAO(computer, computerDTO);

      computer.setCompany(company);
      mComputerService.update(computer);

      redirectAttrs.addAttribute("id", computerDTO.getId());
      return "redirect:/computers/edit?id={id}&msg=editSuccess";
    }
  }

}
