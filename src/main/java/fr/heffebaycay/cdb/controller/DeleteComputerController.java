package fr.heffebaycay.cdb.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import fr.heffebaycay.cdb.service.IComputerService;

@Controller
@RequestMapping("/computers/delete")
public class DeleteComputerController {

  private static final Logger LOGGER = LoggerFactory
                                         .getLogger(DeleteComputerController.class
                                             .getSimpleName());

  @Autowired
  private IComputerService    mComputerService;

  public DeleteComputerController() {

  }

  @RequestMapping(method = RequestMethod.POST)
  protected String doPost(String selection) {

    // The selection of computers to delete is passed as a CSV of ComputerIds
    // e.g.: 123,42,13

    String[] strComputerIds = selection.split(",");
    List<Long> computerIds = new ArrayList<>();

    for (int i = 0; i < strComputerIds.length; i++) {

      try {
        long computerId = Long.parseLong(strComputerIds[i]);
        computerIds.add(computerId);
      } catch (NumberFormatException e) {
        LOGGER.warn("doPost(): Invalid number sent by user. {}", e);
        //response.sendError(response.SC_BAD_REQUEST, "");
        // 400
      }

    }

    for (Long id : computerIds) {
      mComputerService.remove(id);
    }

    return "redirect:/computers/list?msg=removeSuccess";

  }

}
