package fr.heffebaycay.cdb.dto.mapper;

import fr.heffebaycay.cdb.dto.CompanyDTO;
import fr.heffebaycay.cdb.model.Company;

public class CompanyMapper {

  public static CompanyDTO toDTO(Company companyDAO) {

    if (companyDAO == null) {
      return null;
    }

    CompanyDTO companyDTO = new CompanyDTO.Builder().id(companyDAO.getId())
        .name(companyDAO.getName()).build();

    return companyDTO;
  }

  public static Company fromDTO(CompanyDTO companyDTO) {

    if (companyDTO == null) {
      return null;
    }

    Company company = new Company.Builder().id(companyDTO.getId()).name(companyDTO.getName())
        .build();
    
    return company;

  }

}
