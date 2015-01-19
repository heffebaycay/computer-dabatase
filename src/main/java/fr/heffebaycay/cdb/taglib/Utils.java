package fr.heffebaycay.cdb.taglib;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Utils {
  
  
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
