package fr.heffebaycay.cdb.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.heffebaycay.cdb.model.Company;
import fr.heffebaycay.cdb.model.Computer;
import fr.heffebaycay.cdb.dao.IRowMapper;

public class ComputerMySQLRowMapper implements IRowMapper<Computer> {

  private static final Logger LOGGER = LoggerFactory.getLogger(ComputerMySQLRowMapper.class);
  
  /**
   * Maps the current <i>ResultSet</i> object to an instance of <i>Computer</i>
   * 
   * @param resultSet   The <i>ResultSet</i> object, whose cursor is set so the method is able to directly read the object attributes
   * 
   * @return An instance of <i>Computer</i>, mapped with the data from <strong>resultSet</strong>
   * 
   */
  @Override
  public Computer mapRow(ResultSet resultSet) {
    
    Computer computer = null;
    Company company = null;
    
    if(resultSet == null) {
      return null;
    }
    
    try {
      
      
      long computerId = resultSet.getLong("id");
      String computerName = resultSet.getString("name");
      
      LocalDateTime introduced = null;
      Timestamp introducedStamp = resultSet.getTimestamp("introduced");
      if(introducedStamp != null) {
        introduced = introducedStamp.toLocalDateTime();
      }
      
      LocalDateTime discontinued = null;
      Timestamp discontinuedStamp = resultSet.getTimestamp("discontinued");
      if(discontinuedStamp != null) {
        discontinued = discontinuedStamp.toLocalDateTime();
      }

      long companyId = resultSet.getLong("cpId");
      String companyName = resultSet.getString("cpName");
      
      if(companyId > 0) {
        company = new Company.Builder()
                                .id(companyId)
                                .name(companyName)
                                .build();
      }
      
    
      
      computer = new Computer.Builder()
                                  .id(computerId)
                                  .name(computerName)
                                  .discontinued(discontinued)
                                  .introduced(introduced)
                                  .company(company)
                                  .build();
      
    } catch(SQLException e) {
      
      LOGGER.error("SQLException in mapRow: {}", e);
      
    }
    
    return computer;
  }
  
  
}
