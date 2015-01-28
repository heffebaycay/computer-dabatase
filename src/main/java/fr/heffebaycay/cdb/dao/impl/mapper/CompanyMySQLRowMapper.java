package fr.heffebaycay.cdb.dao.impl.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import fr.heffebaycay.cdb.model.Company;

public class CompanyMySQLRowMapper implements RowMapper<Company> {

  private static final Logger LOGGER = LoggerFactory.getLogger(CompanyMySQLRowMapper.class);

  /**
   * Maps the current <i>ResultSet</i> object to an instance of <i>Company</i>
   * 
   * @param resultSet   The <i>ResultSet</i> object, whose cursor is set so the method is able to directly read the object attributes
   * 
   * @return An instance of <i>Company</i>, mapped with the data from <strong>resultSet</strong>
   * 
   */
  @Override
  public Company mapRow(ResultSet resultSet, int rowNum) {

    Company company = null;

    try {

      long id = resultSet.getLong("id");
      String name = resultSet.getString("name");

      company = new Company.Builder().id(id).name(name).build();

    } catch (SQLException e) {

      LOGGER.error("SQLException in mapRow(): {}", e);

    }

    return company;

  }

}
