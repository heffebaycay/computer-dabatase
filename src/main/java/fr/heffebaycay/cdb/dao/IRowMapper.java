package fr.heffebaycay.cdb.dao;

import java.sql.ResultSet;

public interface IRowMapper<T> {

  /**
   * Maps the element located where the <i>Resultset</i> cursor is to an instance of <i>T</i>
   * 
   * @param resultSet The element to be mapped
   * 
   * @return An instance of <T>, mappped with the data contained in the current <i>ResultSet</i> object
   */
  T mapRow(ResultSet resultSet);
  
  
}
