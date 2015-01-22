package fr.heffebaycay.cdb.dto.validator;

import java.util.List;

import fr.heffebaycay.cdb.dto.CompanyDTO;

public class CompanyDTOValidator implements IDTOValidator<CompanyDTO> {

  @Override
  public boolean validate(CompanyDTO companyDTO, List<String> errors) {
    
    // We need a valid List object
    if(errors == null) {
      return false;
    }
    
    if(companyDTO == null) {
      // Company object is null, so it's pointless to analyze anything
      return true;
    }
    
    validateName(companyDTO.getName(), errors);
    
    return errors.isEmpty();
    
  }
  
  protected boolean validateName(String name, List<String> errors) {
    if(name == null || name.trim().isEmpty()) {
      // Company name cannot be empty
      errors.add("Company name cannot be empty");
      return false;
    } else {
      return true;
    }
  }
  
  
  

}
