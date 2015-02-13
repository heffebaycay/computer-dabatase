package fr.heffebaycay.cdb.dto.mapper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LocalDateTimeMapper {

  private static final Logger LOGGER = LoggerFactory.getLogger(LocalDateTimeMapper.class
                                         .getSimpleName());

  @Autowired
  private MappingSettings mappingSettings;
  
  private LocalDateTimeMapper() {
    super();
  }

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

  public LocalDateTime fromLocalDate(LocalDate ld) {

    if (ld == null) {
      return null;
    }

    return ld.atStartOfDay();

  }

}
