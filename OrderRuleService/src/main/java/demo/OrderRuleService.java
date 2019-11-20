package demo;

import java.util.*;

import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import demo.event.*;
import demo.model.*;
import demo.rule.*;
import demo.util.*;

@RestController(value="/orderrule")
public class OrderRuleService {
	
	@RequestMapping(method=RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ArrayList<Action> processEvent(@RequestBody RuleInput ruleIp) {
		
		BaseEvent event = ruleIp.getEvent();
		List<BaseEvent> pastEvents = ruleIp.getPastEvents();
		Order order = ruleIp.getOrder();
		Customer customer = ruleIp.getCustomer();
		
		ArrayList<Action> actions = new ArrayList<Action>();

		if (event.getEventCode().equalsIgnoreCase(OrderUtil.EVENT_ORDER_SUBMIT)) {
			actions.add(new GenerateEventAction(OrderUtil.EVENT_DECIDE_IF_DOCS_NEEDED, null));
			actions.add(new GenerateEventAction(OrderUtil.EVENT_REQUEST_PAYMENT_AUTHORIZE, null));
		}

		if (event.getEventCode().equalsIgnoreCase(OrderUtil.EVENT_DECIDE_IF_DOCS_NEEDED)) {
			actions = decideIfDocsNeeded(event, pastEvents, order, customer);
		}

		if (event.getEventCode().equalsIgnoreCase(OrderUtil.EVENT_LIST_DOCS)) {
			actions = listDocsNeeded(event, pastEvents, order, customer);
		}
		
		if (event.getEventCode().equalsIgnoreCase(OrderUtil.EVENT_REQUEST_REPORT)){
			actions.add(new Action(OrderUtil.ACTION_REQUEST_REPORT, event.getParams()));
		}

		if (event.getEventCode().equalsIgnoreCase(OrderUtil.EVENT_REQUEST_PAYMENT_AUTHORIZE)) {
			actions.add(new Action(OrderUtil.ACTION_REQUEST_PAYMENT_AUTHORIZE, null));
		}
		
		if (event.getEventCode().equalsIgnoreCase(OrderUtil.EVENT_RECEIVE_REPORT)) {
			actions.add(new Action(OrderUtil.ACTION_RECEIVE_REPORT, null));
		}
		
		if (event.getEventCode().equalsIgnoreCase(OrderUtil.EVENT_RECEIVE_PAYMENT_AUTH)) {
			actions.add(new Action(OrderUtil.ACTION_RECEIVE_PAYMENT_AUTH, null));
		}
		
		if (event.getEventCode().equalsIgnoreCase(OrderUtil.EVENT_SHIP_ORDER)) {
			actions.add(new Action(OrderUtil.ACTION_SHIP_ORDER, null));
		}

		return actions;
	}

	private ArrayList<Action> decideIfDocsNeeded(@RequestBody BaseEvent event, @RequestBody List<BaseEvent> pastEvents,
			Order order, Customer customer) {
		ArrayList<Action> actions = new ArrayList<Action>();
		List<Item> items = order.getItems();
		boolean docNeeded = false;
		for (Item item : items) {
			if (item.getProductCode().equals("Drug_A") || item.getProductCode().equals("Drug_B")) {
				docNeeded=true;
			}
		}
		
		if(docNeeded){
			actions.add(new GenerateEventAction(OrderUtil.EVENT_LIST_DOCS, null));
		}else{
			actions.add(new GenerateEventAction(OrderUtil.EVENT_NO_DOCS_NEEDED, null));
		}

		return actions;
	}

	private ArrayList<Action> listDocsNeeded(@RequestBody BaseEvent event, @RequestBody List<BaseEvent> pastEvents,
			Order order, Customer customer) {
		ArrayList<Action> actions = new ArrayList<Action>();
		List<Item> items = order.getItems();
		List<EventParam> params = null;
		for (Item item : items) {
			if (item.getProductCode().equals("Drug_A")) {
				params = new ArrayList<EventParam>();
				params.add(new EventParam(OrderUtil.PARAM_REPORT_NAME, "REPORT_P"));
				params.add(new EventParam(OrderUtil.PARAM_REPORT_METHOD, "Email"));
				actions.add(new GenerateEventAction(OrderUtil.EVENT_REQUEST_REPORT, params));

				params = new ArrayList<EventParam>();
				params.add(new EventParam(OrderUtil.PARAM_REPORT_NAME, "REPORT_Q"));
				params.add(new EventParam(OrderUtil.PARAM_REPORT_METHOD, "Fax"));
				actions.add(new GenerateEventAction(OrderUtil.EVENT_REQUEST_REPORT, params));
			}

			if (item.getProductCode().equals("Drug_B")) {
				params = new ArrayList<EventParam>();
				params.add(new EventParam(OrderUtil.PARAM_REPORT_NAME, "REPORT_S"));
				params.add(new EventParam(OrderUtil.PARAM_REPORT_METHOD, "Phone"));
				actions.add(new GenerateEventAction(OrderUtil.EVENT_REQUEST_REPORT, params));
			}
		}
		return actions;
	}

}
