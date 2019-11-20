package demo.event;

import java.io.*;
import java.util.*;

import demo.model.*;

public class BaseEvent extends AbstractEvent{
	
	private Long orderId;
	private Order order;
	
	public BaseEvent(){
		super();
	}
	
	public BaseEvent(String eventCode, String source, String correlationId, Long orderId ) {
		super(eventCode, source, correlationId, null);
		this.orderId = orderId;
		this.order=order;
	}

	public BaseEvent(String eventCode, String source, String correlationId, Long orderId, List<EventParam> params ) {
		super(eventCode, source, correlationId, params);
		this.orderId = orderId;
		this.order=order;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}


	 @Override
	    public String toString() {
	        return String.format(
	                "BaseEvent[id='%s', source='%s', eventCode='%s', correlationId='%s',order='%s', params = '%s']",
	                 getId(), getSource(), getEventCode(), getCorrelationId(),order, getParams());
	    }
}
