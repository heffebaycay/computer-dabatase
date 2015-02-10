package fr.heffebaycay.cdb.web.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "Failed to initialize an internal service")
public class ServiceInitializationException extends RuntimeException {

  /**
   * 
   */
  private static final long serialVersionUID = -845237483428104288L;

  public ServiceInitializationException() {
    super();
  }

  public ServiceInitializationException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

  public ServiceInitializationException(String message, Throwable cause) {
    super(message, cause);
  }

  public ServiceInitializationException(String message) {
    super(message);
  }

  public ServiceInitializationException(Throwable cause) {
    super(cause);
  }

}
