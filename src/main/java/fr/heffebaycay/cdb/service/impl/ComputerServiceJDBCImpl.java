package fr.heffebaycay.cdb.service.impl;

import java.sql.Connection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.heffebaycay.cdb.dao.IComputerDao;
import fr.heffebaycay.cdb.dao.manager.DaoManager;
import fr.heffebaycay.cdb.model.Computer;
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
    
    List<Computer> computers = computerDao.findAll(conn);
    
    DaoManager.INSTANCE.closeConnection(conn);
    
    return computers;
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public Computer findById(long id) {
    LOGGER.debug("Call to findById()");
    Connection conn = DaoManager.INSTANCE.getConnection();
    
    Computer computer = computerDao.findById(id, conn);
    
    DaoManager.INSTANCE.closeConnection(conn);
    
    return computer;
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public boolean remove(long id) {
    LOGGER.debug("Call to remove()");
    Connection conn = DaoManager.INSTANCE.getConnection();
    
    boolean result = computerDao.remove(id, conn);
    
    DaoManager.INSTANCE.closeConnection(conn);
    
    return result;

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public long create(Computer computer) {
    LOGGER.debug("Call to create()");
    Connection conn = DaoManager.INSTANCE.getConnection();
    
    long computerId = computerDao.create(computer, conn);
    
    DaoManager.INSTANCE.closeConnection(conn);
    
    return computerId;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void update(Computer computer) {
    LOGGER.debug("Call to update()");
    Connection conn = DaoManager.INSTANCE.getConnection();
    
    computerDao.update(computer, conn);
    
    DaoManager.INSTANCE.closeConnection(conn);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public SearchWrapper<Computer> findAll(long offset, long nbRequested, ComputerSortCriteria sortCriterion, SortOrder sortOrder) {
    LOGGER.debug("Call to findAll(long, long)");
    Connection conn = DaoManager.INSTANCE.getConnection();
    
    SearchWrapper<Computer> wrapper = computerDao.findAll(offset, nbRequested, sortCriterion, sortOrder, conn);
    
    DaoManager.INSTANCE.closeConnection(conn);
    
    return wrapper;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public SearchWrapper<Computer> findByName(String name, long offset, long nbRequested, ComputerSortCriteria sortCriterion, SortOrder sortOrder) {
    LOGGER.debug("Call to findByName()");
    Connection conn = DaoManager.INSTANCE.getConnection();
    
    SearchWrapper<Computer> wrapper = computerDao.findByName(name, offset, nbRequested, sortCriterion, sortOrder, conn);
    
    DaoManager.INSTANCE.closeConnection(conn);
    
    return wrapper;
  }
  
  

}
