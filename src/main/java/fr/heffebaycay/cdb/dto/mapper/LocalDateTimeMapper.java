package fr.heffebaycay.cdb.dto.mapper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LocalDateTimeMapper {

  private static final Logger LOGGER       = LoggerFactory.getLogger(LocalDateTimeMapper.class
                                               .getSimpleName());

  private static final String DATE_PATTERN = "yyyy-MM-dd";

  private LocalDateTimeMapper() {
    super();
  }

  public static String toDTO(LocalDateTime ldt) {

    if (ldt == null) {
      return null;
    }

    try {
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_PATTERN);
      String strDate = ldt.format(formatter);
      return strDate;
    } catch (IllegalArgumentException e) {
      LOGGER.warn("toDTO() : Invalid Object Provided: ", e);
      return null;
    }

  }

  public static LocalDateTime fromDTO(String strDate) {

    try {
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_PATTERN);
      LocalDateTime ldt = LocalDateTime.parse(strDate, formatter);
      return ldt;
    } catch (DateTimeParseException e) {
      LOGGER.warn("toDAO() : Failed to parse String input: ", e);
      return null;
    }
  }

  public static LocalDateTime fromLocalDate(LocalDate ld) {

    if (ld == null) {
      return null;
    }

    return ld.atStartOfDay();

  }

}
