package fr.heffebaycay.cdb.dto.mapper;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.FormatStyle;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.i18n.LocaleContextHolder;

public class LocalDateMapper {

  private static final Logger LOGGER       = LoggerFactory.getLogger(LocalDateMapper.class
                                               .getSimpleName());

  private LocalDateMapper() {
    super();
  }

  public static String toDTO(LocalDate ld) {

    if (ld == null) {
      return null;
    }

    try {
      Locale userLocale = LocaleContextHolder.getLocale();

      DateTimeFormatter localeFormatter = DateTimeFormatter
          .ofLocalizedDate(FormatStyle.SHORT)
          .withLocale(userLocale);
      String strDate = ld.format(localeFormatter);
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
      Locale userLocale = LocaleContextHolder.getLocale();
      DateTimeFormatter localeFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT).withLocale(userLocale);
      LocalDate ld = LocalDate.parse(strDate, localeFormatter);
      return ld;
    } catch (DateTimeParseException e) {
      LOGGER.warn("toDAO() : Failed to parse String input: ", e);
      return null;
    }

  }

}
