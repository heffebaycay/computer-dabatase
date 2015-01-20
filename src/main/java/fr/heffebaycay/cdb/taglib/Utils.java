package fr.heffebaycay.cdb.taglib;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Utils {
  
  private static final Logger LOGGER = LoggerFactory.getLogger(Utils.class.getSimpleName());
  
  /**
   * Returns a String representation of a <i>LocalDateTime</i> object in the format defined by the <strong>format</strong> parameter.
   * 
   * @param date    The base <i>LocalDateTime</i> object.
   * @param format  The format in which the date should be returned.
   * 
   * @return        A string representation of the <i>LocalDateTime</i> object given in the format defined by the <strong>format</strong> parameter.
   */
  public static String formatDateTime(LocalDateTime date, String format) {
    String strDate = null;
    
    if( date == null) {
      return "";
    }
    
    try {
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
      strDate = date.format(formatter);
    } catch(IllegalArgumentException e) {
      LOGGER.error("formatDateTime() : Invalid format or date passed: {}", e);
      return "";
    }
    
    return strDate;
    
  }
  
  /**
   * 
   * @param date
   * @return
   */
  public static String formatDateTime(LocalDateTime date) {
    
    return formatDateTime(date, "yyyy-MM-dd");
    
  }

}
