package demo.rule;

import java.io.*;
import java.util.*;

import org.springframework.web.bind.annotation.*;

import demo.event.*;
import demo.model.*;

public class RuleInput implements Serializable {

	private BaseEvent event;
	private List<BaseEvent> pastEvents;
	private Order order;
	private Customer customer;

	public RuleInput() {
		// TODO Auto-generated constructor stub
	}
	public BaseEvent getEvent() {
		return event;
	}

	public void setEvent(BaseEvent event) {
		this.event = event;
	}

	public RuleInput(BaseEvent event, List<BaseEvent> pastEvents, Order order, Customer customer) {
		super();
		this.event = event;
		this.pastEvents = pastEvents;
		this.order = order;
		this.customer = customer;
	}
	public List<BaseEvent> getPastEvents() {
		return pastEvents;
	}

	public void setPastEvents(List<BaseEvent> pastEvents) {
		this.pastEvents = pastEvents;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	

}
