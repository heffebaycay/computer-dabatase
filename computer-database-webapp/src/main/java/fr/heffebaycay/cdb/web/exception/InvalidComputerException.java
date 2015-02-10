package fr.heffebaycay.cdb.web.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Requested Computer is not valid")
public class InvalidComputerException extends RuntimeException {

  public InvalidComputerException() {
    super();
  }

  public InvalidComputerException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

  public InvalidComputerException(String message, Throwable cause) {
    super(message, cause);
  }

  public InvalidComputerException(String message) {
    super(message);
  }

  public InvalidComputerException(Throwable cause) {
    super(cause);
  }
}
