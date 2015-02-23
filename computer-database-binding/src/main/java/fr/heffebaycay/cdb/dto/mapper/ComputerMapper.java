package fr.heffebaycay.cdb.dto.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.heffebaycay.cdb.dto.ComputerDTO;
import fr.heffebaycay.cdb.model.Company;
import fr.heffebaycay.cdb.model.Computer;
import fr.heffebaycay.cdb.wrapper.SearchWrapper;

/**
 * <code>ComputerMapper</code> is a class for converting DO Computer objects from/to DTO Computer objects
 *
 */
@Component
public class ComputerMapper {

  private static final Logger LOGGER = LoggerFactory.getLogger(ComputerMapper.class);

  private ComputerMapper() {
    super();
  }

  @Autowired
  private LocalDateTimeMapper localDateTimeMapper;

  @Autowired
  private LocalDateMapper     localDateMapper;

  @Autowired
  private CompanyMapper       companyMapper;

  /**
   * Converts a Computer DO object to is DTO version 
   * 
   * @param computerDAO     The DAO object to be converted
   * @return                An instance of <code>ComputerDTO</code>, or <strong>null</strong> if <strong>computerDAO</strong> is null.
   */
  public ComputerDTO toDTO(Computer computerDAO) {

    if (computerDAO == null) {
      return null;
    }

    ComputerDTO.Builder builder = new ComputerDTO.Builder();

    Long companyId = 0L;
    if (computerDAO.getCompany() != null) {
      companyId = computerDAO.getCompany().getId();
    }

    ComputerDTO computerDTO = builder
        .id(computerDAO.getId())
        .name(computerDAO.getName())
        .introduced(localDateTimeMapper.toDTO(computerDAO.getIntroduced()))
        .discontinued(localDateTimeMapper.toDTO(computerDAO.getDiscontinued()))
        .companyId(companyId)
        .build();

    if (computerDAO.getCompany() != null) {
      computerDTO.setCompany(companyMapper.toDTO(computerDAO.getCompany()));
    }

    return computerDTO;

  }

  /**
   * Converts a List of <code>Computer</code> to a List of <code>ComputerDTO</code> objects.
   * 
   * @param computers       The list of Computer DAO objects to be converted
   * @return                A List of <code>ComputerDTO</code> objects, or <strong>null</strong> if <strong>computers</strong> is null.
   */
  public List<ComputerDTO> toDTO(List<Computer> computers) {

    if (computers == null) {
      return null;
    }

    return computers.stream().map(c -> toDTO(c)).collect(Collectors.toList());

  }

  /**
   * Converts a ComputerDTO object to its DO version
   * 
   * @param computerDTO     The DTO object to be converted
   * @return                An instance of <i>Computer</i>, or <strong>null</strong> if <strong>computerDTO</strong> is null.
   */
  public Computer fromDTO(ComputerDTO computerDTO) {

    if (computerDTO == null) {
      return null;
    }

    Computer computer = new Computer.Builder()
        .id(computerDTO.getId())
        .name(computerDTO.getName())
        .introduced(
            localDateTimeMapper.fromLocalDate(localDateMapper.fromDTO(computerDTO.getIntroduced())))
        .discontinued(
            localDateTimeMapper.fromLocalDate(localDateMapper.fromDTO(computerDTO.getDiscontinued())))
        .build();

    if (computerDTO.getCompany() != null) {
      computer.setCompany(new Company.Builder().id(computerDTO.getCompany().getId())
          .name(computerDTO.getCompany().getName()).build());
    }

    return computer;
  }

  /**
   * Maps a computerDTO object to an existing computer object 
   * 
   * @param computer        The DO object that should be updated
   * @param computerDTO     The DTO object that contains the updated properties
   */
  public void updateDO(Computer computer, ComputerDTO computerDTO) {

    if (computer == null || computerDTO == null) {
      return;
    }

    Computer localComputer = fromDTO(computerDTO);

    computer.setName(localComputer.getName());
    computer.setIntroduced(localComputer.getIntroduced());
    computer.setDiscontinued(localComputer.getDiscontinued());
    computer.setCompany(localComputer.getCompany());

  }

  /**
   * Maps a SearchWrapper&lt;Computer&gt; object to a SearchWrapper&lt;ComputerDTO&gt; object
   * 
   * @param wrapper     The wrapper that should be mapped
   * @return            A wrapper of <code>ComputerDTO</code>
   */
  public SearchWrapper<ComputerDTO> convertWrappertoDTO(SearchWrapper<Computer> wrapper) {

    SearchWrapper<ComputerDTO> dtoWrapper = new SearchWrapper<>();

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
