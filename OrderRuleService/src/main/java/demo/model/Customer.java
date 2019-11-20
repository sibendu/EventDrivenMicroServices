package demo.model;

import java.io.*;


public class Customer implements Serializable{

    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

    private Long id;
    private String firstName;
    private String lastName;
    private Address address;
    
    protected Customer() {}

    public Customer(String firstName, String lastName, Address address) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address=address;
    }

    @Override
    public String toString() {
        return String.format(
                "Customer[id=%d, firstName='%s', lastName='%s', address='%s']",
                id, firstName, lastName, address);
    }
    
//    @Override
//	public boolean isNew() {
//		if(this.id == null){
//			return true;
//		}
//		return false;
//	}

}