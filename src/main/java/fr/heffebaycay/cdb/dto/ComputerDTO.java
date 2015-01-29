package fr.heffebaycay.cdb.dto;


public class ComputerDTO implements IObjectDTO {
  
  
  private long id;
  
  private String name;
  
  private String introduced;
  
  private String discontinued;
  
  private Long companyId;
  
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
  
  public void setComputerName(String name) {
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
  
  public Long getCompanyId() {
    return companyId;
  }

  public void setCompanyId(Long companyId) {
    this.companyId = companyId;
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
    
    public Builder companyId(Long companyId) {
      computerDTO.setCompanyId(companyId);
      return this;
    }
    
    public ComputerDTO build() {
      return computerDTO;
    }

  }

}
