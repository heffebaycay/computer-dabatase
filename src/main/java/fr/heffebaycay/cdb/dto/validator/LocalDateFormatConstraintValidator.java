package fr.heffebaycay.cdb.dto.validator;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LocalDateFormatConstraintValidator implements ConstraintValidator<LocalDateFormat, String> {

  private static final Logger LOGGER = LoggerFactory.getLogger(LocalDateFormatConstraintValidator.class);

  private String datePattern; 
  
  @Override
  public void initialize(LocalDateFormat ldf) {
    this.datePattern = ldf.pattern();
  }

  @Override
  public boolean isValid(String str, ConstraintValidatorContext arg1) {
    
    if(!StringUtils.isBlank(str)) {
      
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern(this.datePattern);
      try {
        LocalDate.parse(str, formatter);
        return true;
      } catch(DateTimeParseException e) {
        LOGGER.debug("isValid(): Invalid date supplied: '{}' for Pattern: '{}'", str, this.datePattern);
        return false;
      }
      
      
    } else {
      // Blank strings are deemed valid
      return true;
    }
  }
  
  

}
