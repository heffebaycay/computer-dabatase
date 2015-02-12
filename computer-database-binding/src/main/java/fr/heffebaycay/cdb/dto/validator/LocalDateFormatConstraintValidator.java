package fr.heffebaycay.cdb.dto.validator;

import java.util.Locale;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.GenericValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.i18n.LocaleContextHolder;

public class LocalDateFormatConstraintValidator implements
    ConstraintValidator<LocalDateFormat, String> {

  private static final Logger LOGGER = LoggerFactory
                                         .getLogger(LocalDateFormatConstraintValidator.class);

  @Override
  public void initialize(LocalDateFormat ldf) {
  }

  @Override
  public boolean isValid(String str, ConstraintValidatorContext arg1) {

    if (!StringUtils.isBlank(str)) {
      Locale userLocale = LocaleContextHolder.getLocale();
      
      if(GenericValidator.isDate(str, userLocale)) {
        return true;
      } else {
        LOGGER.debug("isValid(): Invalid date supplied: '{}'.", str);
        return false;
      }
    } else {
      // Blank strings are deemed valid
      return true;
    }
  }

}
