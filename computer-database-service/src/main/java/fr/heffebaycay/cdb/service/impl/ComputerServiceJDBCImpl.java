package fr.heffebaycay.cdb.service.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.heffebaycay.cdb.dao.IComputerDao;
import fr.heffebaycay.cdb.model.Computer;
import fr.heffebaycay.cdb.model.ComputerPageRequest;
import fr.heffebaycay.cdb.service.IComputerService;
import fr.heffebaycay.cdb.wrapper.SearchWrapper;

/**
 * 
 * Base implementation of the {@link fr.heffebaycay.cdb.service.IComputerService} inteface.
 *
 */
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

    List<Computer> computers = computerDao.findAll();

    return computers;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Transactional
  public Computer findById(long id) {
    LOGGER.debug("Call to findById()");

    Computer computer = computerDao.findById(id);

    return computer;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Transactional
  public boolean remove(long id) {
    LOGGER.debug("Call to remove()");

    boolean result = computerDao.remove(id);

    return result;

  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Transactional
  public long create(Computer computer) {
    LOGGER.debug("Call to create()");

    long computerId = computerDao.create(computer);

    return computerId;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Transactional
  public void update(Computer computer) {
    LOGGER.debug("Call to update()");

    computerDao.update(computer);

  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Transactional
  public SearchWrapper<Computer> findAll(ComputerPageRequest request) {
    LOGGER.debug("Call to findAll(long, long)");

    SearchWrapper<Computer> wrapper = computerDao.findAll(request);

    return wrapper;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Transactional
  public SearchWrapper<Computer> findByName(ComputerPageRequest request) {
    LOGGER.debug("Call to findByName()");

    SearchWrapper<Computer> wrapper = computerDao.findByName(request);

    return wrapper;
  }

  /**
   * {@inheritDoc}}
   */
  @Override
  @Transactional
  public void remove(List<Long> ids) {
    LOGGER.debug("Call to remove(List<long>)");

    computerDao.remove(ids);

  }

}
