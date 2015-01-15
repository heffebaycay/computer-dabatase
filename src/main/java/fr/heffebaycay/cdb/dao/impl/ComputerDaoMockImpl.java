package fr.heffebaycay.cdb.dao.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import fr.heffebaycay.cdb.dao.IComputerDao;
import fr.heffebaycay.cdb.dao.manager.DaoManager;
import fr.heffebaycay.cdb.model.Company;
import fr.heffebaycay.cdb.model.Computer;
import fr.heffebaycay.cdb.wrapper.SearchWrapper;

public class ComputerDaoMockImpl implements IComputerDao {

  protected List<Computer> computers;

  /**
   * Computer Mock constructor
   * 
   * Some sample computers are created in this constructor.
   */
  public ComputerDaoMockImpl() {
    computers = new ArrayList<Computer>();

    Company apple = new Company.Builder().id(1).name("Apple Inc.").build();

    Company thinkingMachines = new Company.Builder().id(2).name("Thinking Machines").build();

    Computer c1 = new Computer.Builder().id(1).company(apple).name("MacBook Pro 15.4 inch").build();
    Computer c2 = new Computer.Builder().id(2).name("CM-2a").company(thinkingMachines).build();
    Computer c3 = new Computer.Builder().id(3).name("CM-200").company(thinkingMachines).build();

    Computer c4 = new Computer.Builder().id(4).name("CM-5e").company(thinkingMachines).build();

    Computer c5 = new Computer.Builder().id(5).name("CM-5").company(thinkingMachines)
        .introduced(LocalDateTime.parse("1991-01-01T00:00:00")).build();

    Computer c6 = new Computer.Builder().id(6).name("MacBook Pro").company(apple)
        .introduced(LocalDateTime.parse("2006-01-10T00:00:00")).build();

    computers.add(c1);
    computers.add(c2);
    computers.add(c3);
    computers.add(c4);
    computers.add(c5);
    computers.add(c6);

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<Computer> findAll() {

    return computers;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Computer findById(long id) {
    for (Computer c : computers) {
      if (c.getId() == id) {
        return c;
      }
    }

    return null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean remove(long id) {

    Computer computer = findById(id);
    if (computer == null) {
      return false;
    } else {
      return computers.remove(computer);
    }

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public long create(Computer computer) {

    if (computer.getCompany() == null) {
      throw new IllegalArgumentException("The 'company' property cannot be null");
    } else if (computer.getCompany().getId() == 0) {
      // Company doesn't exist
      throw new RuntimeException("Not implemented");
    }

    /**
     * Since we're in a mock class, we need to increment Ids on our own :(
     * Look elsewhere, this isn't going to be pretty o/
     */
    long nextAvailableId = 1;

    for (Computer c : computers) {
      if (c.getId() > nextAvailableId) {
        nextAvailableId = c.getId();
      }
    }

    nextAvailableId++;

    computer.setId(nextAvailableId);

    computers.add(computer);
    
    return nextAvailableId;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void update(Computer computer) {

    if (computer == null || computer.getId() == 0) {
      throw new IllegalArgumentException("Attempted to update an invalid computer object");
    }

    for (Computer source : computers) {
      if (source.getId() == computer.getId()) {
        // Found the matching computer in the mock database

        if (source.getCompany() != computer.getCompany() && computer.getCompany() != null) {

          Company company = DaoManager.INSTANCE.getCompanyDao().findById(
              computer.getCompany().getId());
          if (company != null) {
            source.setCompany(company);
          } else {
            throw new IllegalArgumentException(
                "Attempted to update a computer with an unknown company");
          }
        }

        source.setIntroduced(computer.getIntroduced());
        source.setDiscontinued(computer.getDiscontinued());
        source.setName(computer.getName());

        break;
      }
    }

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public SearchWrapper<Computer> findAll(long offset, long nbRequested) {
    // Not implemented
    return null;
  }

}
