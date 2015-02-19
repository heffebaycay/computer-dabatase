package fr.heffebaycay.cdb.webservice.filter;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.i18n.LocaleContextHolder;


public class LocaleDetectorFilter implements ContainerRequestFilter {

  private static final Logger LOGGER = LoggerFactory.getLogger(LocaleDetectorFilter.class);
  
  @Override
  public void filter(ContainerRequestContext requestContext) throws IOException {
   
    
    // Fetch the list of acceptable languages
    List<Locale> locales = requestContext.getAcceptableLanguages();
    if(!locales. 0) {
      // Since locales are ordered by weight, the first one is
      // definitely the one we want to use
      Locale locale = locales.get(0);
      LOGGER.debug("filter(): Autodetected locale {}", locale);
      LocaleContextHolder.setLocale(locale, true);
    } else {
      LOGGER.debug("filter(): Failed to detect any locale");
    }
    
  }
  
  
  

}
