package fr.heffebaycay.cdb.dao.impl.util;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.heffebaycay.cdb.dao.manager.DaoManager;
import fr.heffebaycay.cdb.util.AppSettings;

@Component
public class MySQLUtils {

  private static final Logger LOGGER = LoggerFactory.getLogger(MySQLUtils.class);

  
  private DaoManager daoManager;
  
  @Autowired
  public MySQLUtils(DaoManager daoManager) {
    this.daoManager = daoManager;
  }
  
  public static String getMySQLConnectionURL() {
    return String.format("jdbc:mysql://127.0.0.1:3306/%s?zeroDateTimeBehavior=convertToNull",
        AppSettings.DB_NAME);
  }

  public void truncateTables() {
    Connection conn = daoManager.getConnection();
    final String queryComputer = "DELETE FROM computer";
    final String queryCompany = "DELETE FROM company";
    try {
      Statement stmt = conn.createStatement();
      stmt.executeUpdate(queryComputer);
      stmt.executeUpdate(queryCompany);
    } catch (SQLException e) {
      LOGGER.error("SQL Exception in trucateTables(): {}", e);
    }
    daoManager.closeConnection();
  }

}
