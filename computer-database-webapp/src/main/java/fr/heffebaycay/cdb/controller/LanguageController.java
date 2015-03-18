package fr.heffebaycay.cdb.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/SetLanguage")
public class LanguageController {

  private static final Logger LOGGER = LoggerFactory.getLogger(LanguageController.class);

  /**
   * This method is only there to provide a URL clients can call to trigger
   * a language change without getting a full page of content.
   */
  @RequestMapping(method = RequestMethod.GET)
  private ResponseEntity<String> setLanguage() {
    LOGGER.debug("Call to LanguageController::setLanguage()");

    return new ResponseEntity<String>("done", HttpStatus.OK);
  }

}
