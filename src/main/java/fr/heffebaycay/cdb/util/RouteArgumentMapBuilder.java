package fr.heffebaycay.cdb.util;

import java.util.HashMap;
import java.util.Map;

public class RouteArgumentMapBuilder extends HashMap<String, String> {

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
