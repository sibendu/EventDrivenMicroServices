package demo.action;

import java.io.*;
import java.util.*;

import org.codehaus.jackson.map.*;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.*;

import demo.*;
import demo.action.*;
import demo.event.*;
import demo.model.*;
import demo.model.Address;
import demo.util.*;

public class PaymentHandler extends AbstractActionHandler {
	public PaymentHandler(RabbitTemplate rabbitTemplate,Action action){
		super(rabbitTemplate, action);
	}
	
/*
 * Handle 
 * DemoUtil.ACTION_REQUEST_PAYMENT_AUTHORIZE:
 * DemoUtil.ACTION_RECEIVE_PAYMENT_AUTH:
 * DemoUtil.ACTION_PAYMT_AUTHRIZED:
 */
	
	@Override
	public void process(BaseEvent event, List<BaseEvent> pastEvents, Order order, Customer customer) throws Exception {
		if(OrderUtil.ACTION_REQUEST_PAYMENT_AUTHORIZE.equals(getAction().getCode())){
			sendRequest(event, pastEvents, order, customer);
		}else if(OrderUtil.ACTION_RECEIVE_PAYMENT_AUTH.equals(getAction().getCode())){
				receiveResponse(event, pastEvents, order, customer);	
		}else{
			System.out.println("PaymentHandler error :: No logic defined");
		}
	}
	
	
	public void sendRequest(BaseEvent event, List<BaseEvent> pastEvents, Order order, Customer customer) throws Exception {
		System.out.println("Payment authorization request sent for :: "+ customer.toString()+ " ::: "+order.toString() );
	}
	
	public void receiveResponse(BaseEvent event, List<BaseEvent> pastEvents, Order order, Customer customer) throws Exception {
		System.out.println("Payment authorization response received for :: "+ customer.toString()+ " ::: "+order.toString() );
		
		//Decide what should happen next
		// If all documents requested have been already received, 
		//generate event proceed with next step i.e. Ship Order 
		if(OrderUtil.checkPendingReports(pastEvents)){
			System.out.println("Order (id="+order.getId()+") has pending reports; will wait ... ");
		}else{
			System.out.println("Order (id="+order.getId()+") has no pending reports; proceeding to Ship Order ... ");
			BaseEvent newEvent = new BaseEvent(OrderUtil.EVENT_SHIP_ORDER, OrderUtil.EVENT_SOURCE_SYSTEM, event.getId(),order.getId());
			postEvent(newEvent,OrderUtil.EXCHANGE_ORDER_EVENTS, OrderUtil.ROUTING_KEY_ORDER_EVENTS); 
		}
	}
	
	
}
