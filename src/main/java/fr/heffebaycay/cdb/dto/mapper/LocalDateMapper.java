package fr.heffebaycay.cdb.dto.mapper;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LocalDateMapper {

  private static final Logger LOGGER       = LoggerFactory.getLogger(LocalDateMapper.class
                                               .getSimpleName());

  private static final String DATE_PATTERN = "yyyy-MM-dd";

  
  private LocalDateMapper() {
    super();
  }
  
  public static String toDTO(LocalDate ld) {

    if (ld == null) {
      return null;
    }

    try {
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_PATTERN);
      String strDate = ld.format(formatter);
      return strDate;
    } catch (IllegalArgumentException e) {
      LOGGER.warn("toDTO() : Invalid Object Provided: ", e);
      return null;
    }

  }

  public static LocalDate fromDTO(String strDate) {

    if (strDate == null || strDate.trim().isEmpty()) {
      return null;
    }

    try {
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_PATTERN);
      LocalDate ld = LocalDate.parse(strDate, formatter);
      return ld;
    } catch (DateTimeParseException e) {
      LOGGER.warn("toDAO() : Failed to parse String input: ", e);
      return null;
    }

  }

}
