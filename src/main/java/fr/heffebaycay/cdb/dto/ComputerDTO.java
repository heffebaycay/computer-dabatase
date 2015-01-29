package fr.heffebaycay.cdb.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

import fr.heffebaycay.cdb.dto.validator.LocalDateFormat;
import fr.heffebaycay.cdb.util.AppSettings;


public class ComputerDTO implements IObjectDTO {
  
  
  private long id;
  
  @NotBlank(message = "Computer name cannot neither null nor empty")
  @Size(min = 1, max = 255, message = "Computer name length must be between {min} and {max} characters")
  private String name;
  
  @LocalDateFormat(message = "Invalid Date Introduced. Correct pattern is {pattern}")
  private String introduced;
  
  @LocalDateFormat(message = "Invalid Date Discontinued. Correct pattern is {pattern}")
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
