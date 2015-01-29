package fr.heffebaycay.cdb.web.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Requested Company is not valid")
public class InvalidCompanyException extends RuntimeException {

  public InvalidCompanyException() {
    super();
  }

  public InvalidCompanyException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

  public InvalidCompanyException(String message, Throwable cause) {
    super(message, cause);
  }

  public InvalidCompanyException(String message) {
    super(message);
  }

  public InvalidCompanyException(Throwable cause) {
    super(cause);
  }

  
  
}
