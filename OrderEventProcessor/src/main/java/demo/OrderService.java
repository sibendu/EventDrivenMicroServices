package demo;

import java.util.*;

import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import demo.event.*;
import demo.util.*;

@SpringBootApplication
@RestController(value="/orders")
public class OrderService {
	
	@Autowired
	OrderEventProcessor processor;
	
	@RequestMapping(value="/{id}/authpay", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	public String receivePaymentAuthorize (
			@PathVariable Long id,
			@RequestParam(name="source", required=true) String source,
			@RequestParam(name="correlationid", required=true, defaultValue="") String correlationId,
			@RequestParam(name="approvedind", required=true) Boolean approved,
			@RequestParam(name="message", required=false) String message
			) throws Exception{
		List<EventParam> params = new ArrayList<EventParam>();
		params.add(new EventParam(OrderUtil.PARAM_PAYMENT_APPROVED_FLAG,approved));
		if(message != null){
			params.add(new EventParam(OrderUtil.PARAM_PAYMENT_APPROVED_MESSAGE,message));
		}
		
		BaseEvent event = new BaseEvent(OrderUtil.EVENT_RECEIVE_PAYMENT_AUTH,source, correlationId,id, params);

		event = processor.processEvent(event);

		return "Order (id="+id+") authorized for payment"; 
		
	}
	
	@RequestMapping(value="/{id}/report", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	public String receiveReport (
			@PathVariable Long id,
			@RequestParam(name="source", required=true) String source,
			@RequestParam(name="correlationid", required=true, defaultValue="") String correlationId,
			@RequestParam(name="name", required=true) String name,
			@RequestParam(name="method", required=false) String method,
			@RequestParam(name="message", required=false) String message
			) throws Exception{
		List<EventParam> params = new ArrayList<EventParam>();
		params.add(new EventParam(OrderUtil.PARAM_REPORT_NAME,name));
		if(method != null){
			params.add(new EventParam(OrderUtil.PARAM_REPORT_METHOD,method));
		}
		if(message != null){
			params.add(new EventParam(OrderUtil.PARAM_REPORT_MESSAGE,message));
		}
		
		BaseEvent event = new BaseEvent(OrderUtil.EVENT_RECEIVE_REPORT,source, correlationId,id, params);

		event = processor.processEvent(event);

		return "Report "+name+"received for Order (id="+id+")"; 
		
	}
	
}
