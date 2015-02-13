package fr.heffebaycay.cdb.dto.mapper;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

@Component
public class MappingSettings {
  
  private static final Logger LOGGER = LoggerFactory.getLogger(MappingSettings.class);
  
  @Autowired
  private MessageSource messageSource;
  
  public String getDatePattern() {
    Locale userLocale = LocaleContextHolder.getLocale();
    
    return messageSource.getMessage("date.format", null, userLocale);

  }
  
}
