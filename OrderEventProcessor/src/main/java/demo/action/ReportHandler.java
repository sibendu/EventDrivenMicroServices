package demo.action;

import java.util.*;

import org.springframework.amqp.rabbit.core.*;

import demo.*;
import demo.action.*;
import demo.event.*;
import demo.model.*;
import demo.util.*;

public class ReportHandler extends AbstractActionHandler {

	public ReportHandler(RabbitTemplate rabbitTemplate, Action action) {
		super(rabbitTemplate, action);
	}

	/*
	 * Handle DemoUtil.ACTION_REQUEST_REPORT: DemoUtil.ACTION_RECEIVE_REPORT:
	 * DemoUtil.ACTION_REPORT_VERIFIED:
	 */
	@Override
	public void process(BaseEvent event, List<BaseEvent> pastEvents, Order order, Customer customer) throws Exception {
		if (OrderUtil.ACTION_REQUEST_REPORT.equals(getAction().getCode())) {
			sendRequest(event, pastEvents, order, customer);
		} else if (OrderUtil.ACTION_RECEIVE_REPORT.equals(getAction().getCode())) {
			receiveResponse(event, pastEvents, order, customer);
		} else {
			System.out.println("ReportHandler error :: No logic defined");
		}
	}

	public void sendRequest(BaseEvent event, List<BaseEvent> pastEvents, Order order, Customer customer)
			throws Exception {

		System.out.println(
				"Report request sent for " + this.getAction().getEventParam(OrderUtil.PARAM_REPORT_NAME).getValue()
						+ " thorugh " + this.getAction().getEventParam(OrderUtil.PARAM_REPORT_METHOD).getValue());
	}

	public void receiveResponse(BaseEvent event, List<BaseEvent> pastEvents, Order order, Customer customer)
			throws Exception {

		System.out.println(
				"Report received for Order (id = "+event.getOrderId()+"); Report_Name == " + event.getParam(OrderUtil.PARAM_REPORT_NAME).getValue());
		
		//Decide what should happen next
		// If all reports requested have been already received, and payment is authorized
		//generate event proceed with next step i.e. Ship Order 
		
		//First add current event to the list of past events, otherwise current report will 
		pastEvents.add(event);
		
		if(OrderUtil.checkPendingReports(pastEvents) && OrderUtil.isPaymentAuthorized(pastEvents)){
			System.out.println("Order (id="+order.getId()+") has no pending reports and payment is authorized; proceeding to Ship Order ... ");
			BaseEvent newEvent = new BaseEvent(OrderUtil.EVENT_SHIP_ORDER, OrderUtil.EVENT_SOURCE_SYSTEM, event.getId(),order.getId());
			postEvent(newEvent,OrderUtil.EXCHANGE_ORDER_EVENTS, OrderUtil.ROUTING_KEY_ORDER_EVENTS); 
		}else{
			System.out.println("Order (id="+order.getId()+") either has pending reports, or payment not authorized; will wait ... ");
		}
	}
	
	
}
