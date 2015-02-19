package fr.heffebaycay.cdb.dto.mapper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * <code>LocalDateTimeMapper</code> is a class for converting LocalDateTime objects to/from a String object
 *
 */
@Component
public class LocalDateTimeMapper {

  private static final Logger LOGGER = LoggerFactory.getLogger(LocalDateTimeMapper.class
                                         .getSimpleName());

  @Autowired
  private MappingSettings mappingSettings;
  
  private LocalDateTimeMapper() {
    super();
  }

  /**
   * Converts a <code>LocalDateTime</code> object to a localized String representation
   * 
   * The current locale is determined by the {@link MappingSettings} class
   * 
   * @param ldt     The <code>LocalDateTime</code> object that should be converted
   * @return        A <code>String</code> representation of the <code>LocalDateTime</code>,
   *                formatted depending on the current Locale, or <strong>null</strong>
   *                if the date conversion to String fails.
   */
  public String toDTO(LocalDateTime ldt) {

    if (ldt == null) {
      return null;
    }
    
    try {
      DateTimeFormatter localeFormatter = DateTimeFormatter
          .ofPattern(mappingSettings.getDatePattern());

      String strDate = ldt.format(localeFormatter);
      return strDate;
    } catch (IllegalArgumentException e) {
      LOGGER.warn("toDTO() : Invalid Object Provided: ", e);
      return null;
    }

  }

  /**
   * Builds a <code>LocalDateTime</code> object from a localized <code>String</code> representation.
   * 
   * The current locale is determined by the {@link MappingSettings} class
   * 
   * @param strDate     The <code>String</code> representation of a <code>LocalDateTime</code>, in a localized pattern
   * @return            A <code>LocalDateTime</code> object corresponding to the given <code>String</code>,
   *                    or <strong>null</strong> if the <code>String</code> parameter is empty or if the conversion fails
   */
  public LocalDateTime fromDTO(String strDate) {

    try {
      DateTimeFormatter localeFormatter = DateTimeFormatter.ofPattern(mappingSettings.getDatePattern());
      LocalDateTime ldt = LocalDateTime.parse(strDate, localeFormatter);
      return ldt;
    } catch (DateTimeParseException e) {
      LOGGER.warn("toDAO() : Failed to parse String input: ", e);
      return null;
    }
  }

  /**
   * Builds a LocalDateTime object from a LocalDate object.
   * 
   * @param ld      The <code>LocalDate</code> object to convert
   * @return        A <code>LocalDateTime</code> representation of the <code>LocalDate</code> object
   */
  public LocalDateTime fromLocalDate(LocalDate ld) {

    if (ld == null) {
      return null;
    }

    return ld.atStartOfDay();

  }

}
