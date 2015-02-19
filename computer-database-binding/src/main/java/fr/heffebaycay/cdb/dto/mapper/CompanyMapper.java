package fr.heffebaycay.cdb.dto.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import fr.heffebaycay.cdb.dto.CompanyDTO;
import fr.heffebaycay.cdb.model.Company;
import fr.heffebaycay.cdb.wrapper.SearchWrapper;

/**
 * <code>CompanyMapper</code> is a class for converting DO Company objects from/to DTO Company objects
 *
 */
@Component
public class CompanyMapper {

  private CompanyMapper() {
    super();
  }

  /**
   * Converts a Company DO object to its DTO version
   * 
   * @param companyDO       The DO object to be converted
   * @return                An instance of <code>CompanyDTO</code>, or <strong>null</strong> if <strong>companyDAO</strong> is null.
   */
  public CompanyDTO toDTO(Company companyDO) {

    if (companyDO == null) {
      return null;
    }

    CompanyDTO companyDTO = new CompanyDTO.Builder().id(companyDO.getId())
        .name(companyDO.getName()).build();

    return companyDTO;
  }

  /**
   * Converts a List of <code>Company</code> to a List of <code>CompanyDTO</code>
   * 
   * @param companies   The List of Company DO objects to be converted
   * @return            A List of <code>CompanyDTO</code> objects, or <strong>null</strong> if <strong>companies</strong> is null.
   */
  public List<CompanyDTO> toDTO(List<Company> companies) {

    if (companies == null) {
      return null;
    }

    return companies.stream().map(c -> toDTO(c)).collect(Collectors.toList());

  }

  /**
   * Converts a <code>CompanyDTO</code> object to its DO version
   * 
   * @param companyDTO      The DTO object to be converted
   * @return                An instance of <code>Company</code>, or <strong>null</strong> if <strong>companyDTO</strong> is null.
   */
  public Company fromDTO(CompanyDTO companyDTO) {

    if (companyDTO == null) {
      return null;
    }

    Company company = new Company.Builder().id(companyDTO.getId()).name(companyDTO.getName())
        .build();

    return company;

  }
  
  /**
   * Maps a CompanyDTO object to an existing Company DO object
   * 
   * @param company The existing DO object that should be updated
   * @param companyDTO The DTO from which the attributes should be taken
   */
  public void updateDO(Company company, CompanyDTO companyDTO) {
    if(company == null || companyDTO == null) {
      return;
    }
    
    Company localCompany = fromDTO(companyDTO);
    
    company.setName(localCompany.getName());
    
  }
  
  /**
   * Maps a SearchWrapper&lt;Company&gt; object to a SearchWrapper&lt;CompanyDTO&gt; object
   * 
   * @param wrapper     The wrapper that should be mapped
   * @return            A wrapper of <code>CompanyDTO</code>
   */
  public SearchWrapper<CompanyDTO> convertWrappertoDTO(SearchWrapper<Company> wrapper) {

    SearchWrapper<CompanyDTO> dtoWrapper = new SearchWrapper<>();

    dtoWrapper.setTotalCount(wrapper.getTotalCount());
    dtoWrapper.setCurrentPage(wrapper.getCurrentPage());
    dtoWrapper.setTotalPage(wrapper.getTotalPage());
    dtoWrapper.setSortOrder(wrapper.getSortOrder());
    dtoWrapper.setSortCriterion(wrapper.getSortCriterion());
    dtoWrapper.setSearchQuery(wrapper.getSearchQuery());

    dtoWrapper.setResults(toDTO(wrapper.getResults()));

    return dtoWrapper;
  }

}
