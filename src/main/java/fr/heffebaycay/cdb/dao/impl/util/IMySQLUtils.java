package fr.heffebaycay.cdb.dao.impl.util;

import java.sql.Connection;

public interface IMySQLUtils {

  /**
   * Return an instance of <i>Connection</i>, to connect to the database
   * 
   * @return An instance of Connection
   */
  Connection getConnection();
  
  /**
   * Closes a <i>Connection</i> object, with error handling.
   * 
   * @param conn The <i>Connection</i> object that should be closed.
   */
  void closeConnection(Connection conn);
  
  
  String getMySQLConnectionURL();
  
  
}
