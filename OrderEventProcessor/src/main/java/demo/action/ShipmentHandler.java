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

public class ShipmentHandler extends AbstractActionHandler {
	public ShipmentHandler(RabbitTemplate rabbitTemplate,Action action){
		super(rabbitTemplate, action);
	}

	@Override
	public void process(BaseEvent event, List<BaseEvent> pastEvents, Order order, Customer customer) throws Exception {
		System.out.println("Start Shipping of Order (id="+order.getId()+")");
	}	
}
