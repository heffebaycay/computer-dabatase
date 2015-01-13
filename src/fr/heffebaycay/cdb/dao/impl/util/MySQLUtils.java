package fr.heffebaycay.cdb.dao.impl.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.heffebaycay.cdb.util.AppSettings;



public class MySQLUtils {
  
  private final static Logger logger = LoggerFactory.getLogger(MySQLUtils.class);

  /**
   * Return an instance of <i>Connection</i>, to connect to the database
   * 
   * @return An instance of Connection
   */
  public static Connection getConnection() {

    Connection conn = null;

    try {
      conn = DriverManager.getConnection(AppSettings.DB_URL, AppSettings.DB_USER,
          AppSettings.DB_PASSWORD);
    } catch (SQLException e) {
      logger.error("Failed to get SQL connection: {}", e);
    }

    return conn;
  }
  
  /**
   * Closes a <i>Connection</i> object, with error handling.
   * 
   * @param conn The <i>Connection</i> object that should be closed.
   */
  public static void closeConnection(Connection conn) {
    try {
      if (conn != null) {
        conn.close();
      }
    } catch (SQLException e) {
      logger.error("Failed to close DB connection: {}", e);
    }
  }
  
}
