package demo.action;

import org.springframework.amqp.rabbit.core.*;

import demo.*;
import demo.action.*;
import demo.event.*;
import demo.model.*;
import demo.util.*;

public class ActionHandlerFactory {

	public static AbstractActionHandler getActionHandler(RabbitTemplate rabbitTemplate, Action action) {
		AbstractActionHandler handler = null;
		
		switch (action.getCode()) {
		
		case OrderUtil.ACTION_REQUEST_PAYMENT_AUTHORIZE:
			handler = new PaymentHandler(rabbitTemplate, action);
			break;
		case OrderUtil.ACTION_REQUEST_REPORT:
			handler = new ReportHandler(rabbitTemplate, action);
			break;
			
			
		case OrderUtil.ACTION_RECEIVE_REPORT:
			handler = new ReportHandler(rabbitTemplate, action);
			break;
		case OrderUtil.ACTION_RECEIVE_PAYMENT_AUTH:
			handler = new PaymentHandler(rabbitTemplate, action);
			break;
		case OrderUtil.ACTION_SHIP_ORDER:
			handler = new ShipmentHandler(rabbitTemplate, action);
			break;
		default:
			break;
		}
		return handler;
	}
}
