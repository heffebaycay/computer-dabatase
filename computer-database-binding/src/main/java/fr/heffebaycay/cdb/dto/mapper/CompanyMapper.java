package fr.heffebaycay.cdb.dto.mapper;

import java.util.List;
import java.util.stream.Collectors;

import fr.heffebaycay.cdb.dto.CompanyDTO;
import fr.heffebaycay.cdb.model.Company;

public class CompanyMapper {

  private CompanyMapper() {
    super();
  }

  /**
   * Converts a Company DAO object to its DTO version
   * 
   * @param companyDAO      The DAO object to be converted
   * @return                An instance of <i>CompanyDTO</i>, or <strong>null</strong> if <strong>companyDAO</strong> is null.
   */
  public static CompanyDTO toDTO(Company companyDAO) {

    if (companyDAO == null) {
      return null;
    }

    CompanyDTO companyDTO = new CompanyDTO.Builder().id(companyDAO.getId())
        .name(companyDAO.getName()).build();

    return companyDTO;
  }

  /**
   * Converts a List of Company to a List of CompanyDTO
   * 
   * @param companies   The List of Company DAO objects to be converted
   * @return            A List of CompanyDTO objects, or <strong>null</strong> if <strong>companies</strong> is null.
   */
  public static List<CompanyDTO> toDTO(List<Company> companies) {

    if (companies == null) {
      return null;
    }

    return companies.stream().map(c -> toDTO(c)).collect(Collectors.toList());

  }

  /**
   * Converts a <i>CompanyDTO</i> object to its DAO version
   * 
   * @param companyDTO      The DTO object to be converted
   * @return                An instance of <i>Company</i>, or <strong>null</strong> if <strong>companyDTO</strong> is null.
   */
  public static Company fromDTO(CompanyDTO companyDTO) {

    if (companyDTO == null) {
      return null;
    }

    Company company = new Company.Builder().id(companyDTO.getId()).name(companyDTO.getName())
        .build();

    return company;

  }

}
