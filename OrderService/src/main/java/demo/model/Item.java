package demo.model;

import java.io.*;
import java.util.*;

import javax.persistence.*;

import org.springframework.data.domain.*;

@Entity
@Table(name = "demo_item")
public class Item implements Serializable{

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

	private String productCode;
    private Double quantity;
    private Double basePrice;
    private Double discount;
    private Double value;
    
    protected Item() {}

    public Item(String productCode, Double quantity, Double basePrice, Double discount, Double value) {
    	this.productCode=productCode;
    	this.quantity=quantity;
    	this.basePrice=basePrice;
    	this.discount=discount;
    	this.value=value;
    }

    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public Double getQuantity() {
		return quantity;
	}

	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}

	public Double getBasePrice() {
		return basePrice;
	}

	public void setBasePrice(Double basePrice) {
		this.basePrice = basePrice;
	}

	public Double getDiscount() {
		return discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
	}

	public Double getValue() {
		return value;
	}

	public void setValue(Double value) {
		this.value = value;
	}
    
    @Override
    public String toString() {
        return String.format(
                "Item[id=%d, productCode='%s', quantity='%s', basePrice='%s', discount='%s', value='%s']",
                id, productCode, quantity, basePrice, discount, value);
    }
}