package fr.heffebaycay.cdb.dto.mapper;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LocalDateMapper {

  private static final Logger LOGGER = LoggerFactory.getLogger(LocalDateMapper.class
                                         .getSimpleName());

  @Autowired
  private MappingSettings mappingSettings;
  
  private LocalDateMapper() {
    super();
  }

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
