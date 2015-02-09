package fr.heffebaycay.cdb.dto.mapper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.FormatStyle;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.i18n.LocaleContextHolder;

public class LocalDateTimeMapper {

  private static final Logger LOGGER       = LoggerFactory.getLogger(LocalDateTimeMapper.class
                                               .getSimpleName());

  private LocalDateTimeMapper() {
    super();
  }

  public static String toDTO(LocalDateTime ldt) {

    if (ldt == null) {
      return null;
    }

    try {
      Locale userLocale = LocaleContextHolder.getLocale();
      DateTimeFormatter localeFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT).withLocale(userLocale);
      String strDate = ldt.format(localeFormatter);
      return strDate;
    } catch (IllegalArgumentException e) {
      LOGGER.warn("toDTO() : Invalid Object Provided: ", e);
      return null;
    }

  }

  public static LocalDateTime fromDTO(String strDate) {

    try {
      Locale userLocale = LocaleContextHolder.getLocale();
      
      DateTimeFormatter localeFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT).withLocale(userLocale);
      LocalDateTime ldt = LocalDateTime.parse(strDate, localeFormatter);
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
