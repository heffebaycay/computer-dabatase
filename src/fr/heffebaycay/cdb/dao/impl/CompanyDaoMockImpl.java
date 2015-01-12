package fr.heffebaycay.cdb.dao.impl;

import java.util.ArrayList;
import java.util.List;

import fr.heffebaycay.cdb.model.Company;
import fr.heffebaycay.cdb.wrapper.SearchWrapper;
import fr.heffebaycay.cdb.dao.ICompanyDao;

public class CompanyDaoMockImpl implements ICompanyDao  {
	
	protected List<Company> companies;
	
	
	public CompanyDaoMockImpl() {
	
		companies = new ArrayList<Company>();
		
		String[] names = {"Apple Inc.", "Thinking Machines", "RCA", "Netronics", "Tandy Corporation"};
		
		
		for(int i = 0; i < names.length; i++) {
			Company c = new Company.Builder()
										.id(i + 1)
										.name(names[i])
										.build();
			
			companies.add(c);
		}
		
		
		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Company> getCompanies() {
		return companies;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Company findById(long id) {
		
		for(Company c : companies) {
			if(c.getId() == id) {
				return c;
			}
		}
		
		
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void create(Company company) {
		/**
		 * Since we're in a mock class, we need to increment Ids on our own :(
		 * Look elsewhere, this isn't going to be pretty o/
		 */
		
		long nextAvailableId = 1;
		
		for(Company c : companies) {
			if(c.getId() > nextAvailableId) {
				nextAvailableId = c.getId();
			}
		}
		
		nextAvailableId++;
		
		company.setId(nextAvailableId);
		
		companies.add(company);
		
	}

	/**
	 * {@inheritDoc}
	 */
  @Override
  public SearchWrapper<Company> getCompanies(long offset, long nbRequested) {
    return null;
  }
	
	
	
	
	
	
}
