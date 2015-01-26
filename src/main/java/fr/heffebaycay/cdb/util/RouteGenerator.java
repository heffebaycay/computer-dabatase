package fr.heffebaycay.cdb.util;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.heffebaycay.cdb.model.Route;

public class RouteGenerator {

  private static final Logger LOGGER = LoggerFactory.getLogger(RouteGenerator.class.getSimpleName());
  
  private RouteGenerator() {
    super();
  }
  
  /**
   * Generates the path to the Dashboard route.
   * 
   * @param pageNumber      Page number
   * @param search          Search Query
   * @return                Path to the Dashboard route
   */
  public static String generateDashboardRoute(String pageNumber, String search, String sortCriterion, String sortOrder) {
    
    return generateRoute("dashboard", new RouteArgumentMapBuilder().addArgument("p", pageNumber).addArgument("search", search).addArgument("sortBy", sortCriterion).addArgument("order", sortOrder).build());
    
  }
  
  public static String generateCompanyRoute(String pageNumber, String search, String sortCriterion, String sortOrder) {
	  return generateRoute("company-list", new RouteArgumentMapBuilder().addArgument("p", pageNumber).addArgument("search", search).addArgument("sortBy", sortCriterion).addArgument("order", sortOrder).build());
  }
  
  /**
   * Generates the path to a given Route identified by its name, and append the
   * parameters to the path.
   * 
   * @param routeName       Unique name of the route, as defined by the application. 
   * @param parameters      Key-value map of the route parameters (e.g.: page => 3, query => "Intel") 
   * @return                The path to the Route.
   */
  protected static String generateRoute(String routeName, Map<String, String> parameters) {

    LOGGER.debug(String.format("generateRoute() : routeName={%s}", routeName));
    
    if (routeName == null) {
      return null;
    }

    StringBuilder routeURLBuilder = new StringBuilder();

    for (Route e : AppSettings.APP_ROUTES) {
      if (routeName.equals(e.getName())) {

        // Build URL
        routeURLBuilder.append(e.getPath());
        if (!e.getParameters().isEmpty()) {
          // Need to append some parameters to the url
          routeURLBuilder.append("?");

          boolean isFirstParam = true;

          for (String pName : e.getParameters()) {

            if (parameters.containsKey(pName)) {

              if (isFirstParam) {
                isFirstParam = false;
              } else {
                // Inserting the parameter separator
                routeURLBuilder.append("&amp;");
              }

              routeURLBuilder.append(pName);
              routeURLBuilder.append("=");
              routeURLBuilder.append(parameters.get(pName));
            }

          }

          return routeURLBuilder.toString();

        } else {
          // No parameters to append to the URL
          return routeURLBuilder.toString();
        }

      }
    }

    return null;

  }

}
