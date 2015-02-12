package fr.heffebaycay.cdb.dao.impl.test;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Component;

@Component
public class MySQLUtils {

  private static final Logger     LOGGER = LoggerFactory.getLogger(MySQLUtils.class);

  @Autowired
  private DriverManagerDataSource dataSource;

  public MySQLUtils() {}

  public Connection getConnection() {

    Connection connection;

    try {
      connection = dataSource.getConnection();
    } catch (SQLException e) {
      LOGGER.error("getConnection(): Failed to get connection: {}", e);
      connection = null;
    }

    return connection;

  }

  public void closeConnection(Connection conn) {
    if (conn != null) {
      try {
        conn.close();
      } catch (SQLException e) {
        LOGGER.warn("closeConnection(): Failed to close connection: {}", e);
      }
    }
  }

  public void closeStatement(Statement stmt) {
    if (stmt != null) {
      try {
        stmt.close();
      } catch (SQLException e) {
        LOGGER.warn("closeStatement(): Failed to close statement: {}", e);
      }
    }
  }

  public void truncateTables() {
    Connection connection = getConnection();

    final String queryComputer = "DELETE FROM computer";
    final String queryCompany = "DELETE FROM company";
    try {
      Statement stmt = connection.createStatement();
      stmt.executeUpdate(queryComputer);
      stmt.executeUpdate(queryCompany);
    } catch (SQLException e) {
      LOGGER.warn("truncateTables(): Failed to clear DB of data: {}", e);
    } finally {
      closeConnection(connection);
    }

  }

  public void setDataSource(DriverManagerDataSource dataSource) {
    this.dataSource = dataSource;
  }

}
