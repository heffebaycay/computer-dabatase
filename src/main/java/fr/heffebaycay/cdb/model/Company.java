package fr.heffebaycay.cdb.model;

public class Company {

  protected long   id;

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
    if (name == null || name.length() == 0) {
      throw new IllegalArgumentException("The 'name' property cannot be null nor empty");
    }
    this.name = name;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + (int) (id ^ (id >>> 32));
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
    Company other = (Company) obj;
    if (id != other.id)
      return false;
    if (name == null) {
      if (other.name != null)
        return false;
    } else if (!name.equals(other.name))
      return false;
    return true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {

    return String.format("Company: id[%d] name[%s]", getId(), getName());
  }

  /**
   * Builder class for a <i>Company</i> object
   */
  public static class Builder {
    private Company company;

    public Builder() {
      company = new Company();
    }

    /**
     * Sets the <strong>id</strong> attribute of the underlying <i>Company</i> object
     * 
     * @param id  The id that should be set
     * @return    A reference to the current instance of <i>Builder</i>
     */
    public Builder id(long id) {
      company.setId(id);
      return this;
    }

    /**
     * Sets the <strong>name</strong> attribute of the underlying <i>Company</i> object
     * 
     * @param name    The name that should be set
     * @return        A reference to the current instance of <i>Builder</i>
     */
    public Builder name(String name) {
      company.setName(name);
      return this;
    }

    /**
     * Creates an instance of <i>Company</i>. Each attribute of <i>Company</i> have a matching method
     * in the <i>Builder</i> class
     * 
     * @return An instance of <i>Company</i>
     */
    public Company build() {
      return company;
    }

  }

}
