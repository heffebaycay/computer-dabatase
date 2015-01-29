package fr.heffebaycay.cdb.dto.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.heffebaycay.cdb.dto.ComputerDTO;
import fr.heffebaycay.cdb.model.Company;
import fr.heffebaycay.cdb.model.Computer;

public class ComputerMapper {

  public static final Logger LOGGER = LoggerFactory.getLogger(ComputerMapper.class.getSimpleName());

  private ComputerMapper() {
    super();
  }

  /**
   * Converts a Computer DAO object to is DTO version 
   * 
   * @param computerDAO     The DAO object to be converted
   * @return                An instance of <i>ComputerDTO</i>, or <strong>null</strong> if <strong>computerDAO</strong> is null.
   */
  public static ComputerDTO toDTO(Computer computerDAO) {

    if (computerDAO == null) {
      return null;
    }

    ComputerDTO.Builder builder = new ComputerDTO.Builder();

    Long companyId = 0L;
    if (computerDAO.getCompany() != null) {
      companyId = computerDAO.getCompany().getId();
    }

    builder
        .id(computerDAO.getId())
        .name(computerDAO.getName())
        .introduced(LocalDateTimeMapper.toDTO(computerDAO.getIntroduced()))
        .discontinued(LocalDateTimeMapper.toDTO(computerDAO.getDiscontinued()))
        .companyId(companyId);

    return builder.build();

  }

  /**
   * Converts a List of Computer to a List of ComputerDTO objects.
   * 
   * @param computers       The list of Computer DAO objects to be converted
   * @return                A List of ComputerDTO objects, or <strong>null</strong> if <strong>computers</strong> is null.
   */
  public static List<ComputerDTO> toDTO(List<Computer> computers) {

    if (computers == null) {
      return null;
    }

    return computers.stream().map(c -> toDTO(c)).collect(Collectors.toList());

  }

  /**
   * Converts a ComputerDTO object to its DAO version
   * 
   * @param computerDTO     The DTO object to be converted
   * @return                An instance of <i>Computer</i>, or <strong>null</strong> if <strong>computerDTO</strong> is null.
   */
  public static Computer fromDTO(ComputerDTO computerDTO) {

    if (computerDTO == null) {
      return null;
    }
    
    Computer computer = new Computer.Builder()
        .id(computerDTO.getId())
        .name(computerDTO.getName())
        .introduced(
            LocalDateTimeMapper.fromLocalDate(LocalDateMapper.fromDTO(computerDTO.getIntroduced())))
        .discontinued(
            LocalDateTimeMapper.fromLocalDate(LocalDateMapper.fromDTO(computerDTO.getDiscontinued())))
        .company(new Company.Builder().id(computerDTO.getCompanyId()).build())
        .build();

    return computer;
  }

  /**
   * Maps a computerDTO object to an existing computer object 
   * 
   * @param computer
   * @param computerDTO
   */
  public static void updateDAO(Computer computer, ComputerDTO computerDTO) {

    if (computer == null || computerDTO == null) {
      return;
    }

    Computer localComputer = fromDTO(computerDTO);

    computer.setName(localComputer.getName());
    computer.setIntroduced(localComputer.getIntroduced());
    computer.setDiscontinued(localComputer.getDiscontinued());
    computer.setCompany(localComputer.getCompany());

  }

}
