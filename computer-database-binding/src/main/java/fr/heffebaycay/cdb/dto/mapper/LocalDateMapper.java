package fr.heffebaycay.cdb.dto.mapper;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * <code>LocalDateMapper</code> is a class for converting LocalDate objects to/from a String object
 *
 */
@Component
public class LocalDateMapper {

  private static final Logger LOGGER = LoggerFactory.getLogger(LocalDateMapper.class);

  @Autowired
  private MappingSettings     mappingSettings;

  private LocalDateMapper() {
    super();
  }

  /**
   * Converts a <code>LocalDate</code> object to a localized String representation
   * 
   * The current locale is determined by the <code>MappingSettings</code> class
   * 
   * @param ld      The <code>LocalDate</code> object that should be converted
   * @return        A <code>String</code> representation of the <code>LocalDate</code>,
   *                formatted depending on the current Locale, or <strong>null</strong>
   *                if the date conversion to String fails.
   */
  public String toDTO(LocalDate ld) {

    if (ld == null) {
      return null;
    }

    try {
      DateTimeFormatter localeFormatter = DateTimeFormatter
          .ofPattern(mappingSettings.getDatePattern());
      String strDate = ld.format(localeFormatter);
      return strDate;
    } catch (IllegalArgumentException e) {
      LOGGER.warn("toDTO() : Invalid Object Provided: ", e);
      return null;
    }

  }

  /**
   * Builds a <code>LocalDate</code> object from a localized <code>String</code> representation.
   * 
   * The current locale is determined by the <code>MappingSettings</code> class
   * 
   * @param strDate     The <code>String</code> representation of a <code>LocalDate</code>, in a localized pattern
   * @return            A <code>LocalDate</code> object corresponding to the given <code>String</code>,
   *                    or <strong>null</strong> if the <code>String</code> parameter is empty or if the conversion fails
   */
  public LocalDate fromDTO(String strDate) {

    if (strDate == null || strDate.trim().isEmpty()) {
      return null;
    }

    try {
      DateTimeFormatter localeFormatter = DateTimeFormatter
          .ofPattern(mappingSettings.getDatePattern());
      LocalDate ld = LocalDate.parse(strDate, localeFormatter);
      return ld;
    } catch (DateTimeParseException e) {
      LOGGER.warn("toDAO() : Failed to parse String input: ", e);
      return null;
    }

  }

}
