package demo;

import java.util.*;

import org.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import demo.event.*;
import demo.model.repo.*;

@SpringBootApplication
@RestController
@RequestMapping(value="/events")
public class EventService {

	private static final Logger log = LoggerFactory.getLogger(EventService.class);

	@Autowired
	EventRepository repository;

	@RequestMapping(method=RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<BaseEvent>> findEvents(@RequestParam(name="customerid", required=false) Long customerId, 
														@RequestParam(name="orderid", required=false) Long orderId){
		
		List<BaseEvent> events = new ArrayList();
		
		List<BaseEvent> results = null;
		
		if(orderId != null){
			results = repository.findByOrderId(orderId);
		}else if(customerId != null){
			results = repository.findByOrder_Customer_Id(customerId);
		}else{
			results = repository.findAll();
		}
					
		for (BaseEvent event : results) {
			events.add(event);
		}
		//events.forEach((ev) -> System.out.println(ev.toString()));
		return new ResponseEntity<List<BaseEvent>>(events,HttpStatus.OK);
	}
	
	
	@RequestMapping(value="/eventstree/{orderId}",  method=RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<EventTreeNode> findEventsTree(@PathVariable Long orderId){
		List<BaseEvent> results = repository.findByOrderId(orderId);
		EventTreeNode tree = buildTree(results.get(0));
		return new ResponseEntity<EventTreeNode>(tree,HttpStatus.OK);
	}
	
	public EventTreeNode buildTree(BaseEvent event){
		EventTreeNode current = new EventTreeNode(event);
		List<BaseEvent> children = repository.findByCorrelationId(event.getId());
		for (BaseEvent child : children) {
			current.addChild(buildTree(child));
		} 
		return current;
	}

	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<BaseEvent> findEvent(@PathVariable String id) throws Exception {
		BaseEvent event = repository.findOne(id);
		return new ResponseEntity<BaseEvent>(event,HttpStatus.OK);
	}
	
	@RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<BaseEvent> saveEvent(@RequestBody BaseEvent event) throws Exception {
		event = repository.save(event);
		return new ResponseEntity<BaseEvent>(event,HttpStatus.OK);
	}
}
