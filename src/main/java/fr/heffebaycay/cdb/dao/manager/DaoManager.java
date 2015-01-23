package fr.heffebaycay.cdb.dao.manager;

import java.sql.Connection;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.heffebaycay.cdb.dao.ICompanyDao;
import fr.heffebaycay.cdb.dao.IComputerDao;
import fr.heffebaycay.cdb.dao.exception.DaoException;
import fr.heffebaycay.cdb.dao.impl.CompanyDaoMySQLImpl;
import fr.heffebaycay.cdb.dao.impl.ComputerDaoMySQLImpl;
import fr.heffebaycay.cdb.dao.impl.util.MySQLUtils;

/**
 * The <i>DaoManager</i> object can be used to access the various objects that
 * allow some CRUD actions on the <i>Computer</i> and <i>Company</i> objects 
 */
public enum DaoManager {

  INSTANCE;

  private ICompanyDao  companyDao;
  private IComputerDao computerDao;
  private MySQLUtils sqlUtils;
  
  private static final Logger LOGGER = LoggerFactory.getLogger(DaoManager.class.getSimpleName());

  static {
    try {
      Class.forName("com.mysql.jdbc.Driver");
    } catch (ClassNotFoundException e) {
      throw new DaoException("Failed to load MySQL JDBC driver.", e);
    }
  }

  private DaoManager() {
    companyDao = new CompanyDaoMySQLImpl();
    computerDao = new ComputerDaoMySQLImpl();
    sqlUtils = new MySQLUtils();
  }

  public ICompanyDao getCompanyDao() {
    return companyDao;
  }

  public IComputerDao getComputerDao() {
    return computerDao;
  }
  
  public Connection getConnection() {
    return sqlUtils.getConnection();
  }
  
  public void startTransaction(Connection conn) {
    try {
      LOGGER.debug("startTransaction(): Begining new transaction");
      conn.setAutoCommit(false);
    } catch(SQLException e) {
      LOGGER.warn("startTransaction(): Failed to start transaction: ", e);
    }
  }
  
  public void commitTransaction(Connection conn) {
    try {
      LOGGER.debug("commitTransaction(): Commiting transaction");
      conn.commit();
    } catch(SQLException e) {
      LOGGER.warn("commitTransaction(): Failed to commit transaction: ", e);
      rollbackTransaction(conn);
    }
  }
  
  public void endTransaction(Connection conn) {
    try {
      LOGGER.debug("endTransaction(): About to end transaction");
      conn.setAutoCommit(true);
      closeConnection(conn);
    } catch(SQLException e) {
      LOGGER.warn("endTransaction(): Failed to end transaction: ", e);
    }
  }
  
  public void rollbackTransaction(Connection conn) {
    try {
      LOGGER.debug("rollbackTransaction(): About to rollback transaction");
      conn.rollback();
    } catch(SQLException e) {
      LOGGER.warn("rollbackTransaction(): Failed to rollback transaction: ", e);
    }
  }
  
  public void closeConnection(Connection conn) {
    sqlUtils.closeConnection(conn);
  }

}
