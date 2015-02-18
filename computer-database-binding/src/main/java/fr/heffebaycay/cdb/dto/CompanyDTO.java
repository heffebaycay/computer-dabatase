package fr.heffebaycay.cdb.dto;

import java.io.Serializable;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

public class CompanyDTO implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -3227468289968810021L;

  protected long            id;

  @NotBlank
  @Size(min = 1, max = 255)
  protected String          name;

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

  @Override
  public String toString() {
    return String.format(
        "Company: id[%1$d], name[%2$s]",
        getId(), getName());
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
