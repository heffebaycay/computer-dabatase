package fr.heffebaycay.cdb.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.heffebaycay.cdb.dao.exception.ConfigFileException;
import fr.heffebaycay.cdb.model.Route;

public class AppSettings {

  private static final String CONFIG_FILE     = "config.properties";

  public static List<Route>   APP_ROUTES;

  public static final long    NB_RESULTS_PAGE = 10;

  private static final Logger LOGGER          = LoggerFactory.getLogger(AppSettings.class
                                                  .getSimpleName());

  static {

    readProperties();
    populateRoutes();
  }

  // AppSettings Should never be instanciated
  private AppSettings() {

  }

  /**
   * Read the application configuration file and set the application 
   * parameters according to its content.
   */
  private static void readProperties() {

    Properties prop = new Properties();

    InputStream inputStream = AppSettings.class.getClassLoader().getResourceAsStream(CONFIG_FILE);
    if (inputStream != null) {
      try {
        prop.load(inputStream);
      } catch (IOException e) {
        LOGGER.error("Failed to read config file: {}", e);
        throw new ConfigFileException("Failed to read config file.", e);
      }
    } else {
      LOGGER.error("Failed to load config file.");
      throw new ConfigFileException();
    }

  }

  /**
   * Populate the list of routes in the application, so URLs can
   * be dynamically generated.
   */
  private static void populateRoutes() {

    APP_ROUTES = new ArrayList<>();

    Set<String> params = new HashSet<>();
    params.add("p");
    params.add("search");
    params.add("order");
    params.add("sortBy");

    Route dashboardRoute = new Route.Builder().name("dashboard").path("/computers/list").parameters(params)
        .build();
    
    Route companyRoute = new Route.Builder().name("company-list").path("/companies/list").parameters(params).build();

    APP_ROUTES.add(dashboardRoute);
    APP_ROUTES.add(companyRoute);

  }

}
