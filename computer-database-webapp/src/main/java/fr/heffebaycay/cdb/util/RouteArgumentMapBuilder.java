package fr.heffebaycay.cdb.util;

import java.util.HashMap;
import java.util.Map;

public class RouteArgumentMapBuilder extends HashMap<String, String> {

  /**
   * 
   */
  private static final long serialVersionUID = 3514327207578076443L;
  protected Map<String, String> args;
  
  public RouteArgumentMapBuilder() {
    
    args = new HashMap<>();
  }
  
  public RouteArgumentMapBuilder addArgument(String paramName, String paramValue) {
    args.put(paramName, paramValue);
    return this;
  }
  
  public Map<String, String> build() {
    return args;
  }
  
}
