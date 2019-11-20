package demo.model;

import java.io.*;
import java.util.*;

import javax.persistence.*;

import org.springframework.data.domain.*;

@Entity
@Table(name = "demo_order")
public class Order implements Serializable{

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private String status;
    private Date createdDate;
    private Date updatedDate;
    private Double totalValue;
    
    public void addItem(Item item){
    	if(this.items == null){
    		this.items = new ArrayList<Item>();
    	}
    	
    	if(item.getValue() == null){
    		item.setValue(item.getBasePrice() * item.getQuantity() * item.getDiscount());
    	}
    	
    	this.items.add(item);
    	
    	if(this.totalValue == null){
    		this.totalValue = new Double(0);
    	}
    	this.totalValue = this.totalValue + item.getValue();
    }
    
    @OneToOne(cascade = CascadeType.PERSIST , fetch=FetchType.EAGER)
    @JoinColumn(name = "customer_id")
    private Customer customer;
    
    @OneToOne(cascade = CascadeType.PERSIST, fetch=FetchType.EAGER)
    @JoinColumn(name = "bill_address_id")
    private Address billAddress;
    
    @OneToOne(cascade = CascadeType.PERSIST, fetch=FetchType.EAGER)
    @JoinColumn(name = "ship_address_id")
    private Address shipAddress;
    
    @OneToMany(cascade = CascadeType.PERSIST, fetch=FetchType.EAGER)
    @JoinColumn(name = "item_id")
    private List<Item> items;

    protected Order() {}

    public Order(Customer customer, Address billAddress, Address shipAddress) {
    	this.createdDate= new Date();
    	this.customer = customer;
    	this.status = "NEW";
    	this.billAddress = billAddress;
    	this.shipAddress = shipAddress;
    	this.totalValue = new Double(0);
    }

    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Double getTotalValue() {
		return totalValue;
	}

	public void setTotalValue(Double totalValue) {
		this.totalValue = totalValue;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Address getBillAddress() {
		return billAddress;
	}

	public void setBillAddress(Address billAddress) {
		this.billAddress = billAddress;
	}

	public Address getShipAddress() {
		return shipAddress;
	}

	public void setShipAddress(Address shipAddress) {
		this.shipAddress = shipAddress;
	}

    @Override
    public String toString() {
        return String.format(
                "Order[id=%d, status='%s', createdDate='%s', customer='%s', billAddress='%s', shipAddress='%s', items='%s' ]",
                id, status, createdDate, customer, billAddress, shipAddress, items);
    }

	public List<Item> getItems() {
		return items;
	}

	public void setItems(List<Item> items) {
		this.items = items;
	}
}