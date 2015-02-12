package fr.heffebaycay.cdb.web.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Requested Computer does not exist")
public class ComputerNotFoundException extends RuntimeException {

  /**
   * 
   */
  private static final long serialVersionUID = 6420059200537476540L;

  public ComputerNotFoundException() {
    super();
  }

  public ComputerNotFoundException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

  public ComputerNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }

  public ComputerNotFoundException(String message) {
    super(message);
  }

  public ComputerNotFoundException(Throwable cause) {
    super(cause);
  }

  
  
  
}
