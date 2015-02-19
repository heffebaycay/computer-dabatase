package fr.heffebaycay.cdb.dto;

import java.io.Serializable;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

import fr.heffebaycay.cdb.dto.validator.LocalDateFormat;

public class ComputerDTO implements Serializable {

  private static final long serialVersionUID = 163956444219807087L;

  public static final String ATTR_NAME = "name";
  public static final String ATTR_INTRODUCED = "introduced";
  public static final String ATTR_DISCONTINUED = "discontinued";
  
  private long              id;

  @NotBlank(message = "{computer_dto.name_not_blank}")
  @Size(min = 1, max = 255, message = "{computer_dto.name_length}")
  private String            name;

  @LocalDateFormat(message = "{computer_dto.date_introduced_format}")
  private String            introduced;

  @LocalDateFormat(message = "{computer_dto.date_discontinued_format}")
  private String            discontinued;

  private Long              companyId;

  private CompanyDTO        company;

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

  public CompanyDTO getCompany() {
    return company;
  }

  public void setCompany(CompanyDTO company) {
    this.company = company;
  }
  
  

  @Override
  public String toString() {
    return String.format(
        "Computer: id[%1$d], name[%2$s], introduced[%4$s], discontinued[%5$s], companyId[%6$s], company[%3$s]",
        getId(), getName(), getCompany(), getIntroduced(), getDiscontinued(), getCompanyId());
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
