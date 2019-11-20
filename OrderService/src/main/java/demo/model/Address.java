package demo.model;

import java.io.*;

import javax.persistence.*;

import org.springframework.beans.factory.annotation.*;
import org.springframework.data.domain.*;

@Entity
@Table(name = "demo_address")
public class Address implements Serializable{
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAddressLine1() {
		return addressLine1;
	}

	public void setAddressLine1(String addressLine1) {
		this.addressLine1 = addressLine1;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private String addressLine1;
    private String zip;

    protected Address() {}

    public Address(String addressLine1, String zip) {
        this.addressLine1 = addressLine1;
        this.zip = zip;
    }

    @Override
    public String toString() {
        return String.format(
                "Address[id=%d, addressLine1='%s', zip='%s']",
                id, addressLine1, zip);
    }
}
