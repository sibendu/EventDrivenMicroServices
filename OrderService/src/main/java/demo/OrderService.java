package demo;

import java.util.*;

import org.codehaus.jackson.map.*;
import org.slf4j.*;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.boot.autoconfigure.security.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import demo.event.*;
import demo.model.*;
import demo.model.repo.*;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class })
@RestController
@RequestMapping("/orders")
public class OrderService {
	private static final Logger log = LoggerFactory.getLogger(OrderService.class);

	public static final String EVENT_ORDER_SUBMIT = "Order Submitted";
	public static final String EXCHANGE_ORDER_EVENTS = "event_exchange";
	public static final String ROUTING_KEY_ORDER_EVENTS = "100";
	
	@Autowired
	RabbitTemplate rabbitTemplate;
	
	@Autowired
	OrderRepository orderRepository;
	
	@Autowired
	CustomerRepository customerRepository;
	
	@Autowired
	AddressRepository addressRepository;

	@RequestMapping(method=RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Order>> findAll(@RequestParam(name="customerid", required=false) Long customerId, 
													@RequestParam(name="status", required=false) String status){
		List<Order> orders = null;
		
		if(customerId != null){
			orders = orderRepository.findByCustomer_Id(customerId);
		}else{
			orders = (List<Order>)orderRepository.findAll();
		}
		
//		for (Order order : results) {
//			orders.add(order);
//		}
//		orders.forEach((ord) -> System.out.println(ord.toString()));
		return new ResponseEntity<List<Order>>(orders,HttpStatus.OK);
	}
	
	@RequestMapping(value="/{id}", method=RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Order> findById(@PathVariable Long id){
		Order order = orderRepository.findOne(id);
		return new ResponseEntity<Order>(order,HttpStatus.OK);
	}
	
	
	@RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Order> saveOrder(@RequestBody Order order) throws Exception {
		
		if(order.getId() != null){
			return new ResponseEntity<Order>(order,HttpStatus.BAD_REQUEST);
		}
		
		order = transferValues(order);
		
		order = orderRepository.save(order);
		log.info("Order saved in the database :: Order Id == "+order.getId());
		
		//Post an event to trigger Order processing
		BaseEvent event = new BaseEvent(EVENT_ORDER_SUBMIT,"CRM", "CRM"+order.getId().toString(),order.getId());
		postEvent(event);
		log.info("Event posted :: "+EVENT_ORDER_SUBMIT+ " for Order Id == "+order.getId());
		
		return new ResponseEntity<Order>(order,HttpStatus.OK);
	}
	
	private Order transferValues(Order order){
		if(order.getCustomer().getId() != null){
			order.setCustomer(customerRepository.findOne(order.getCustomer().getId()));
		}else if(order.getCustomer().getAddress() != null && order.getCustomer().getAddress().getId() != null){
			order.getCustomer().setAddress(addressRepository.findOne(order.getCustomer().getAddress().getId()));
		}
		
		if(order.getShipAddress().getId() != null){
			order.setShipAddress(addressRepository.findOne(order.getShipAddress().getId()));
		}
		if(order.getBillAddress().getId() != null){
			order.setBillAddress(addressRepository.findOne(order.getBillAddress().getId()));
		}
		return order;
	}
	
	@RequestMapping(method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Order> updateOrder(@RequestBody Order order) throws Exception {
		if(order.getId() == null){
			return new ResponseEntity<Order>(order,HttpStatus.BAD_REQUEST);
		}
	
		order = orderRepository.save(order);
		
		order = orderRepository.save(order);
		return new ResponseEntity<Order>(order,HttpStatus.OK);
	}
	
	public void postEvent(BaseEvent event) throws Exception{
		String msg = new ObjectMapper().writeValueAsString(event);
		MessageProperties prop = new MessageProperties();
		prop.setContentType(MessageProperties.CONTENT_TYPE_JSON);
		rabbitTemplate.send(EXCHANGE_ORDER_EVENTS, ROUTING_KEY_ORDER_EVENTS,new Message(msg.getBytes(), prop));
	}
	
}
