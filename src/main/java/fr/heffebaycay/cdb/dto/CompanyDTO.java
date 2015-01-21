package fr.heffebaycay.cdb.dto;

import java.io.Serializable;

public class CompanyDTO implements IObjectDTO {
  
  protected long id;
  
  protected String name;
  
  public CompanyDTO() {
    
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
  
  
  public static class Builder {
    
    protected CompanyDTO companyDTO;
    
    public Builder() {
      companyDTO = new CompanyDTO();
    }
    
    public Builder id(long id) {
      companyDTO.setId(id);
      return this;
    }
    
    public Builder name(String name) {
      companyDTO.setName(name);
      return this;
    }
    
    public CompanyDTO build() {
      return companyDTO;
    }
    
  }
  

}
