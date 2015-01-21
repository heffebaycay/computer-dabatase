package fr.heffebaycay.cdb.dto.mapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.heffebaycay.cdb.dto.ComputerDTO;
import fr.heffebaycay.cdb.dto.IObjectDTO;
import fr.heffebaycay.cdb.model.Computer;

public class ComputerMapper {

  public static final Logger LOGGER = LoggerFactory.getLogger(ComputerMapper.class.getSimpleName());

  
  public static ComputerDTO toDTO(Computer computerDAO) {

    if (computerDAO == null) {
      return null;
    }

    ComputerDTO.Builder builder = new ComputerDTO.Builder();
    
    builder
      .id(computerDAO.getId())
      .name(computerDAO.getName())
      .introduced( LocalDateTimeMapper.toDTO(computerDAO.getIntroduced()) )
      .discontinued(LocalDateTimeMapper.toDTO(computerDAO.getDiscontinued()))
      .company(CompanyMapper.toDTO(computerDAO.getCompany()))
      ;
    
    return builder.build();

  }


  
  public static Computer toDAO(ComputerDTO computerDTO) {
   
    if(computerDTO == null) {
      return null;
    }
    
    Computer computer = new Computer.Builder()
                                        .id(computerDTO.getId())
                                        .name(computerDTO.getName())
                                        .introduced(LocalDateTimeMapper.fromDTO(computerDTO.getIntroduced()))
                                        .discontinued(LocalDateTimeMapper.fromDTO(computerDTO.getDiscontinued()))
                                        .company(CompanyMapper.fromDTO(computerDTO.getCompany()))
                                        .build();
    
    return computer;
  }

}
