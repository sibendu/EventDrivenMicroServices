package demo.action;

import java.io.*;
import java.util.*;

import org.codehaus.jackson.map.*;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.*;
import org.springframework.beans.factory.annotation.*;

import demo.*;
import demo.event.*;
import demo.model.*;

public abstract class AbstractActionHandler {
	
	RabbitTemplate rabbitTemplate;
	
	private Action action;
	
	public RabbitTemplate getRabbitTemplate() {
		return rabbitTemplate;
	}

	public void setRabbitTemplate(RabbitTemplate rabbitTemplate) {
		this.rabbitTemplate = rabbitTemplate;
	}
	
	public AbstractActionHandler(RabbitTemplate rabbitTemplate, Action action){
		this.action = action;
		this.rabbitTemplate=rabbitTemplate;
	}
	
	public Action getAction() {
		return action;
	}
	public void setAction(Action action) {
		this.action = action;
	}
	
	public void postEvent(BaseEvent event, String exchange, String routing) throws Exception{
		String msg = msg = new ObjectMapper().writeValueAsString(event);
		MessageProperties prop = new MessageProperties();
		prop.setContentType(MessageProperties.CONTENT_TYPE_JSON);
		getRabbitTemplate().send(exchange, routing,new Message(msg.getBytes(), prop));
	}
	
	public abstract void process(BaseEvent event, List<BaseEvent> pastEvents, Order order, Customer customer) throws Exception;
	
	
}
