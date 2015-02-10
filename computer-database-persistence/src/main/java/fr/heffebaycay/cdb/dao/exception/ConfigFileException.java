package fr.heffebaycay.cdb.dao.exception;

public class ConfigFileException extends RuntimeException {

  public ConfigFileException() {
    super();
  }

  public ConfigFileException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

  public ConfigFileException(String message, Throwable cause) {
    super(message, cause);
  }

  public ConfigFileException(String message) {
    super(message);
  }

  public ConfigFileException(Throwable cause) {
    super(cause);
  }
  
  
  

}
