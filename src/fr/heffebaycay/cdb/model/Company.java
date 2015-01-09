package fr.heffebaycay.cdb.model;

public class Company {

	protected long id;
	
	protected String name;

	
	public Company() {
		
	}
	
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
	
	
	@Override
	public String toString() {

		return String.format("Company: id[%d] name[%s]", getId(), getName());
	}




	public static class Builder {
		private Company company;
		
		public Builder() {
			company = new Company();
		}
		
		public Builder id(long id) {
			company.setId(id);
			return this;
		}
		
		public Builder name(String name) {
			company.setName(name);
			return this;
		}
		
		public Company build() {
			return company;
		}
		
	}
	
	
	
}
