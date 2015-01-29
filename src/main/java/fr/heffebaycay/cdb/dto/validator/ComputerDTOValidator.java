package fr.heffebaycay.cdb.dto.validator;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.heffebaycay.cdb.dto.ComputerDTO;

public class ComputerDTOValidator implements IDTOValidator<ComputerDTO> {

  private static final Logger LOGGER = LoggerFactory.getLogger(ComputerDTOValidator.class.getSimpleName());
  
  private static final String DATE_PATTERN = "yyyy-MM-dd"; 
  
  @Override
  public boolean validate(ComputerDTO computerDTO, List<String> errors) {
    
    // We need a valid list object
    if(errors == null) {
      return false; 
    }
    
    if(computerDTO == null) {
      // object is null, so it's pointless to analyze it further
      return true;
    }
    
    validateName(computerDTO.getName(), errors);
    validateIntroduced(computerDTO.getIntroduced(), errors);
    validateDiscontinued(computerDTO.getDiscontinued(), errors);
    
    CompanyDTOValidator companyValidator = new CompanyDTOValidator();
    
    return errors.isEmpty();
  }
  
  protected boolean validateName(String name, List<String> errors) {
    
    if(name == null || name.isEmpty() || name.trim().isEmpty()) {
      errors.add("Computer name cannot be empty");
      return false;
    } else {
      return true;
    }
    
  }
  
  protected boolean validateIntroduced(String introduced, List<String> errors) {
    
    if(validateDate(introduced) == false) {
      errors.add("Invalid value for date introduced. Correct format is: " + DATE_PATTERN);
      return false;
    } else {
      return true;
    }
    
  }
  
  protected boolean validateDiscontinued(String discontinued, List<String> errors) {
    if(validateDate(discontinued) == false) {
      errors.add("Invalid value for date discontinued. Correct format is: " + DATE_PATTERN);
      return false;
    } else {
      return true;
    }
  }
  
  protected boolean validateDate(String strDate) {
    if(strDate != null && !strDate.trim().isEmpty()) {
      // Date is not empty, thus we must check consistency
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_PATTERN);
      try {
        LocalDate ld = LocalDate.parse(strDate);
        return true;
      } catch(DateTimeParseException e) {
        // Date is not valid
        LOGGER.debug("validateDate(): DateTimeParseException: ", e);
        return false;
      }
      
    } else {
      // Empty date is acceptable
      return true;
    }
  }

}
