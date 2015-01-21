package fr.heffebaycay.cdb.dto;

import java.io.Serializable;

public class ComputerDTO implements IObjectDTO {
  
  
  protected long id;
  
  protected String name;
  
  protected String introduced;
  
  protected String discontinued;
  
  protected CompanyDTO company;
  
  public ComputerDTO() {
    
    
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

  public String getIntroduced() {
    return introduced;
  }

  public void setIntroduced(String introduced) {
    this.introduced = introduced;
  }

  public String getDiscontinued() {
    return discontinued;
  }

  public void setDiscontinued(String discontinued) {
    this.discontinued = discontinued;
  }

  public CompanyDTO getCompany() {
    return company;
  }

  public void setCompany(CompanyDTO company) {
    this.company = company;
  }
  
  
  public static class Builder {
    
    protected ComputerDTO computerDTO;
    
    public Builder() {
      computerDTO = new ComputerDTO();
    }
    
    public Builder id(long id) {
      computerDTO.setId(id);
      return this;
    }
    
    public Builder name(String name) {
      computerDTO.setName(name);
      return this;
    }
    
    public Builder introduced(String introduced) {
      computerDTO.setIntroduced(introduced);
      return this;
    }
    
    public Builder discontinued(String discontinued) {
      computerDTO.setDiscontinued(discontinued);
      return this;
    }
    
    public Builder company(CompanyDTO company) {
      computerDTO.setCompany(company);
      return this;
    }
    
    public ComputerDTO build() {
      return computerDTO;
    }

  }

}
