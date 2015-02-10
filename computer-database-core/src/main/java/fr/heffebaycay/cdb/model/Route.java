package fr.heffebaycay.cdb.model;

import java.util.Set;

public class Route {
  
  protected String path;
  
  protected String name;
  
  protected Set<String> parameters;
  
  public Route() {
    
  }

  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Set<String> getParameters() {
    return parameters;
  }

  public void setParameters(Set<String> parameters) {
    this.parameters = parameters;
  }
  
  public static class Builder {
    
    private Route route;
    
    public Builder() {
      route = new Route();
    }
    
    public Builder name(String name) {
      route.setName(name);
      return this;
    }
    
    public Builder path(String path) {
      route.setPath(path);
      return this;
    }
    
    public Builder parameters(Set<String> params) {
      route.setParameters(params);
      return this;
    }
    
    public Route build() {
      return route;
    }
    
    
    
  }
  

}
