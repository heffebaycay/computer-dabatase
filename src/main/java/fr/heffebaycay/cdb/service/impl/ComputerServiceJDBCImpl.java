package fr.heffebaycay.cdb.service.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.heffebaycay.cdb.dao.IComputerDao;
import fr.heffebaycay.cdb.dao.exception.DaoException;
import fr.heffebaycay.cdb.model.Computer;
import fr.heffebaycay.cdb.model.ComputerPageRequest;
import fr.heffebaycay.cdb.service.IComputerService;
import fr.heffebaycay.cdb.wrapper.SearchWrapper;

@Service
public class ComputerServiceJDBCImpl implements IComputerService {

  private static final Logger LOGGER = LoggerFactory.getLogger(ComputerServiceJDBCImpl.class);

  IComputerDao                computerDao;

  @Autowired
  public ComputerServiceJDBCImpl(IComputerDao computerDao) {
    this.computerDao = computerDao;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Transactional
  public List<Computer> findAll() {
    LOGGER.debug("Call to findAll()");

    List<Computer> computers = null;
    try {
      computers = computerDao.findAll();
    } catch (DaoException e) {
      LOGGER.warn("findAll(): DaoException", e);
    }

    return computers;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Transactional
  public Computer findById(long id) {
    LOGGER.debug("Call to findById()");

    Computer computer = null;
    try {
      computer = computerDao.findById(id);
    } catch (DaoException e) {
      LOGGER.warn("findById(): DaoException: ", e);
    }

    return computer;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Transactional
  public boolean remove(long id) {
    LOGGER.debug("Call to remove()");

    boolean result = false;
    try {
      result = computerDao.remove(id);
    } catch (DaoException e) {
      LOGGER.warn("remove(): DaoException: ", e);
    }

    return result;

  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Transactional
  public long create(Computer computer) {
    LOGGER.debug("Call to create()");

    long computerId = -1;
    try {
      computerId = computerDao.create(computer);
    } catch (DaoException e) {
      LOGGER.warn("create(): DaoException: ", e);
    }

    return computerId;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Transactional
  public void update(Computer computer) {
    LOGGER.debug("Call to update()");

    try {
      computerDao.update(computer);
    } catch (DaoException e) {
      LOGGER.warn("update(): ", e);
    }

  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Transactional
  public SearchWrapper<Computer> findAll(ComputerPageRequest request) {
    LOGGER.debug("Call to findAll(long, long)");

    SearchWrapper<Computer> wrapper = null;

    try {
      wrapper = computerDao.findAll(request);
    } catch (DaoException e) {
      LOGGER.warn("findAll(): DaoException: ", e);
    }
    
    return wrapper;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Transactional
  public SearchWrapper<Computer> findByName(ComputerPageRequest request) {
    LOGGER.debug("Call to findByName()");

    SearchWrapper<Computer> wrapper = null;

    try {
      wrapper = computerDao.findByName(request);
    } catch (DaoException e) {
      LOGGER.warn("findByName(): DaoException: ", e);
    }

    return wrapper;
  }

}
