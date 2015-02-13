package fr.heffebaycay.cdb.dto.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.GenericValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import fr.heffebaycay.cdb.dto.mapper.MappingSettings;

public class LocalDateFormatConstraintValidator implements
    ConstraintValidator<LocalDateFormat, String> {

  private static final Logger LOGGER = LoggerFactory
                                         .getLogger(LocalDateFormatConstraintValidator.class);

  @Autowired
  private MappingSettings mappingSettings;

  @Override
  public void initialize(LocalDateFormat ldf) {}

  @Override
  public boolean isValid(String str, ConstraintValidatorContext arg1) {

    if (!StringUtils.isBlank(str)) {
      String datePattern = mappingSettings.getDatePattern();

      if (GenericValidator.isDate(str, datePattern, true)) {
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
