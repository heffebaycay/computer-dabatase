package fr.heffebaycay.cdb.dao.impl.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.heffebaycay.cdb.util.AppSettings;

public class MySQLUtils {

  private static final Logger LOGGER = LoggerFactory.getLogger(MySQLUtils.class);

  // Class should never be instanciated
  private MySQLUtils() {
    super();
  }
  
  /**
   * Return an instance of <i>Connection</i>, to connect to the database
   * 
   * @return An instance of Connection
   */
  public static Connection getConnection() {

    Connection conn = null;

    try {
      conn = DriverManager.getConnection(getMySQLConnectionURL(), AppSettings.DB_USER,
          AppSettings.DB_PASSWORD);
    } catch (SQLException e) {
      LOGGER.error("Failed to get SQL connection: {}", e);
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
      LOGGER.error("Failed to close DB connection: {}", e);
    }
  }

  /**
   * Closes a <i>Statement</i> object.
   * 
   * @param stmt    The <i>Statement</i> object that should be closed.
   */
  public static void closeStatement(Statement stmt) {
    try {
      if (stmt != null) {
        stmt.close();
      }
    } catch (SQLException e) {
      LOGGER.error("closeStatement(): SQL Exception : ", e);
    }
  }

  /**
   * Closes both a <i>Connection</i> and a <i>Statement</i> object.
   * 
   * @param conn    The <i>Connection</i> object that should be closed.
   * @param stmt    The <i>Statement</i> object that should be closed.
   */
  public static void closeConnectionAndStatement(Connection conn, Statement stmt) {

    try {
      if (stmt != null) {
        stmt.close();
      }
    } catch (SQLException e) {
      LOGGER.error("closeConnectionAndStatement(): SQL Exception: ", e);
    }

    closeConnection(conn);

  }

  public static String getMySQLConnectionURL() {
    return String.format("jdbc:mysql://127.0.0.1:3306/%s?zeroDateTimeBehavior=convertToNull",
        AppSettings.DB_NAME);
  }

  public static void truncateTables() {
    Connection conn = getConnection();
    final String queryComputer = "DELETE FROM computer";
    final String queryCompany = "DELETE FROM company";
    try {
      Statement stmt = conn.createStatement();
      stmt.executeUpdate(queryComputer);
      stmt.executeUpdate(queryCompany);
    } catch (SQLException e) {
      LOGGER.error("SQL Exception in trucateTables(): {}", e);
    }
    closeConnection(conn);
  }

}
