package fr.heffebaycay.cdb.service.impl;

import java.sql.Connection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.heffebaycay.cdb.dao.IComputerDao;
import fr.heffebaycay.cdb.dao.exception.DaoException;
import fr.heffebaycay.cdb.dao.manager.DaoManager;
import fr.heffebaycay.cdb.model.Computer;
import fr.heffebaycay.cdb.model.ComputerPageRequest;
import fr.heffebaycay.cdb.service.IComputerService;
import fr.heffebaycay.cdb.util.ComputerSortCriteria;
import fr.heffebaycay.cdb.util.SortOrder;
import fr.heffebaycay.cdb.wrapper.SearchWrapper;

public class ComputerServiceJDBCImpl implements IComputerService {

  private static final Logger LOGGER = LoggerFactory.getLogger(ComputerServiceJDBCImpl.class);
  
  IComputerDao computerDao;

  public ComputerServiceJDBCImpl() {
    computerDao = DaoManager.INSTANCE.getComputerDao();
  }

  public ComputerServiceJDBCImpl(IComputerDao computerDao) {
    this.computerDao = computerDao;
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public List<Computer> findAll() {
    LOGGER.debug("Call to findAll()");
    Connection conn = DaoManager.INSTANCE.getConnection();
    
    List<Computer> computers = null;
    try {
      computers = computerDao.findAll(conn);
    } catch (DaoException e) {
      LOGGER.warn("findAll(): DaoException", e);
    } finally {
      DaoManager.INSTANCE.closeConnection(conn);
    }
    
    return computers;
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public Computer findById(long id) {
    LOGGER.debug("Call to findById()");
    Connection conn = DaoManager.INSTANCE.getConnection();
    
    Computer computer = null;
    try {
      computer = computerDao.findById(id, conn);
    } catch (DaoException e) {
      LOGGER.warn("findById(): DaoException: ", e);
    } finally {
      DaoManager.INSTANCE.closeConnection(conn);
    }
    
    return computer;
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public boolean remove(long id) {
    LOGGER.debug("Call to remove()");
    Connection conn = DaoManager.INSTANCE.getConnection();
    
    boolean result = false;
    try {
      result = computerDao.remove(id, conn);
    } catch (DaoException e) {
      LOGGER.warn("remove(): DaoException: ", e);
    } finally {
      DaoManager.INSTANCE.closeConnection(conn);
    }
    
    return result;

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public long create(Computer computer) {
    LOGGER.debug("Call to create()");
    Connection conn = DaoManager.INSTANCE.getConnection();
    
    long computerId = -1;
    try {
      computerId = computerDao.create(computer, conn);
    } catch (DaoException e) {
      LOGGER.warn("create(): DaoException: ", e);
    } finally {
      DaoManager.INSTANCE.closeConnection(conn);
    }
    
    return computerId;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void update(Computer computer) {
    LOGGER.debug("Call to update()");
    Connection conn = DaoManager.INSTANCE.getConnection();
    
    try {
      computerDao.update(computer, conn);
    } catch (DaoException e) {
      LOGGER.warn("update(): ", e);
    } finally {
      DaoManager.INSTANCE.closeConnection(conn);
    }
    
    
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public SearchWrapper<Computer> findAll(ComputerPageRequest request) {
    LOGGER.debug("Call to findAll(long, long)");
    Connection conn = DaoManager.INSTANCE.getConnection();
    
    SearchWrapper<Computer> wrapper = null;
    
    try {
      wrapper = computerDao.findAll(request, conn);
    } catch(DaoException e) {
      LOGGER.warn("findAll(): DaoException: ", e);
    } finally {
      DaoManager.INSTANCE.closeConnection(conn);
    }
    
    return wrapper;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public SearchWrapper<Computer> findByName(ComputerPageRequest request) {
    LOGGER.debug("Call to findByName()");
    Connection conn = DaoManager.INSTANCE.getConnection();
    
    SearchWrapper<Computer> wrapper = null;
    
    try {
      wrapper = computerDao.findByName(request, conn);
    } catch( DaoException e) {
      LOGGER.warn("findByName(): DaoException: ", e);
    } finally {
      DaoManager.INSTANCE.closeConnection(conn);
    }

    
    return wrapper;
  }
  
  

}
