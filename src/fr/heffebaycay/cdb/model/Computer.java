package fr.heffebaycay.cdb.model;

import java.time.LocalDateTime;

public class Computer {
	
	protected long id;
	
	protected String name;
	
	protected LocalDateTime introduced;
	
	protected LocalDateTime discontinued;
	
	protected Company company;
	
	public Computer() {
		
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

	public LocalDateTime getIntroduced() {
		return introduced;
	}

	public void setIntroduced(LocalDateTime introduced) {
		this.introduced = introduced;
	}

	public LocalDateTime getDiscontinued() {
		return discontinued;
	}

	public void setDiscontinued(LocalDateTime discontinued) {
		this.discontinued = discontinued;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}
	
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((company == null) ? 0 : company.hashCode());
		result = prime * result
				+ ((discontinued == null) ? 0 : discontinued.hashCode());
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result
				+ ((introduced == null) ? 0 : introduced.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Computer other = (Computer) obj;
		if (company == null) {
			if (other.company != null)
				return false;
		} else if (!company.equals(other.company))
			return false;
		if (discontinued == null) {
			if (other.discontinued != null)
				return false;
		} else if (!discontinued.equals(other.discontinued))
			return false;
		if (id != other.id)
			return false;
		if (introduced == null) {
			if (other.introduced != null)
				return false;
		} else if (!introduced.equals(other.introduced))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public String toString() {
		
		return String.format("Computer: id[%1$d], name[%2$s], introduced[%4$s], discontinued[%5$s], company[%3$s]", getId(), getName(), getCompany(), getIntroduced(), getDiscontinued());
		
	}
	
	
	public static class Builder {
		
		private Computer computer;
		
		public Builder() {
			computer = new Computer();
		}
		
		public Builder id(long id) {
			computer.setId(id);
			return this;
		}
		
		public Builder name(String name) {
			computer.setName(name);
			return this;
		}
		
		public Builder introduced(LocalDateTime introduced) {
			computer.setIntroduced(introduced);
			return this;
		}
		
		public Builder discontinued(LocalDateTime discontinued) {
			computer.setDiscontinued(discontinued);
			return this;
		}
		
		public Builder company(Company company) {
			computer.setCompany(company);
			return this;
		}
		
		public Computer build() {
			return computer;
		}
		
	}
	
	
	

}
