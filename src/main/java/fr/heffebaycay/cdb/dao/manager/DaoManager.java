package fr.heffebaycay.cdb.dao.manager;

import java.sql.Connection;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jolbox.bonecp.BoneCP;
import com.jolbox.bonecp.BoneCPConfig;

import fr.heffebaycay.cdb.dao.ICompanyDao;
import fr.heffebaycay.cdb.dao.IComputerDao;
import fr.heffebaycay.cdb.dao.exception.DaoException;
import fr.heffebaycay.cdb.dao.impl.SQLCompanyDao;
import fr.heffebaycay.cdb.dao.impl.SQLComputerDao;
import fr.heffebaycay.cdb.dao.impl.util.MySQLUtils;
import fr.heffebaycay.cdb.util.AppSettings;

/**
 * The <i>DaoManager</i> object can be used to access the various objects that
 * allow some CRUD actions on the <i>Computer</i> and <i>Company</i> objects 
 */
public enum DaoManager {

  INSTANCE;

  private ICompanyDao  companyDao;
  private IComputerDao computerDao;
  private MySQLUtils sqlUtils;
  private BoneCP connectionPool;
  
  
  
  private static final Logger LOGGER = LoggerFactory.getLogger(DaoManager.class);

  private DaoManager() {  
    
    try {
      Class.forName("com.mysql.jdbc.Driver");
    } catch (ClassNotFoundException e) {
      throw new DaoException("Failed to load MySQL JDBC driver.", e);
    }
    
    companyDao = new SQLCompanyDao();
    computerDao = new SQLComputerDao();
        
    
    sqlUtils = new MySQLUtils();
    
    BoneCPConfig config = new BoneCPConfig();
    config.setJdbcUrl(sqlUtils.getMySQLConnectionURL());
    config.setUsername(AppSettings.DB_USER);
    config.setPassword(AppSettings.DB_PASSWORD);
    config.setMinConnectionsPerPartition(3);
    config.setMaxConnectionsPerPartition(10);
    config.setPartitionCount(2);
    
    try {
      connectionPool = new BoneCP(config);
    } catch (SQLException e) {
      throw new DaoException("Failed to initialize BoneCP.", e);
    }
    
    
  }

  public ICompanyDao getCompanyDao() {
    return companyDao;
  }

  public IComputerDao getComputerDao() {
    return computerDao;
  }
  
  public Connection getConnection() {
    
    Connection conn = null;
    
    try {
      conn = connectionPool.getConnection();
    } catch(SQLException e) {
      LOGGER.warn("Failed to get DB connection.", e);
    }
    
    return conn;
  }
  
  public void startTransaction(Connection conn) {
    try {
      LOGGER.debug("startTransaction(): Begining new transaction");
      conn.setAutoCommit(false);
      conn.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
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
