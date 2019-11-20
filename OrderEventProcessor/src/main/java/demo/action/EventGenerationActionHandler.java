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
import demo.util.*;

public class EventGenerationActionHandler extends AbstractActionHandler {

	public EventGenerationActionHandler(RabbitTemplate rabbitTemplate, Action action) {
		super(rabbitTemplate, action);
	}

	/*
	 * Handle DemoUtil.ACTION_ORDER_SUBMIT:
	 */
	@Override
	public void process(BaseEvent event, List<BaseEvent> pastEvents, Order order, Customer customer) throws Exception {
		//Generate event EVENT_DECIDE_DOCS_NEEDED
		BaseEvent newEvent = new BaseEvent(getAction().getCode(), OrderUtil.EVENT_SOURCE_SYSTEM, event.getId(),order.getId(), getAction().getParams());
		postEvent(newEvent,OrderUtil.EXCHANGE_ORDER_EVENTS, OrderUtil.ROUTING_KEY_ORDER_EVENTS); 
	}

}
