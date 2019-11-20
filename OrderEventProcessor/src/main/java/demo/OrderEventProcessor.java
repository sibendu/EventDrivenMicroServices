package demo;

import java.io.*;
import java.net.*;
import java.util.*;

import org.codehaus.jackson.*;
import org.codehaus.jackson.map.*;
import org.springframework.amqp.core.*;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.*;
import org.springframework.amqp.rabbit.listener.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.cloud.*;
import org.springframework.cloud.client.loadbalancer.*;
import org.springframework.context.annotation.*;
import org.springframework.http.*;
import org.springframework.messaging.converter.*;
import org.springframework.web.client.*;
import org.springframework.web.util.*;

import com.rabbitmq.client.*;

import demo.action.*;
import demo.event.*;
import demo.model.*;
import demo.rule.*;
import demo.util.*;

@Configuration
public class OrderEventProcessor {

	@Autowired
	private RestTemplate restTemplate;

	@Bean
	@LoadBalanced
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	@Bean
	public ConnectionFactory connectionFactory() {
		ConnectionFactory cf = new CachingConnectionFactory(OrderUtil.RABBIT_HOST);
		((CachingConnectionFactory)cf).setUsername(OrderUtil.RABBIT_USER);
		((CachingConnectionFactory)cf).setPassword(OrderUtil.RABBIT_PASSWORD);
		return cf;
		// CloudFactory cloudFactory = new CloudFactory();
		// Cloud cloud = cloudFactory.getCloud();
		// AmqpServiceInfo serviceInfo = (AmqpServiceInfo)
		// cloud.getServiceInfo("cares_event");
		// String serviceID = serviceInfo.getId();
		// return cloud.getServiceConnector(serviceID, ConnectionFactory.class,
		// null);
	}

	@Bean
	public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
		return new RabbitTemplate(connectionFactory);
	}

	@Bean
	public MessageListenerContainer messageListenerContainer() {
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		container.setConcurrentConsumers(5);
		container.setConnectionFactory(connectionFactory());
		container.setQueueNames(OrderUtil.QUEUE_ORDER_EVENTS);
		container.setMessageListener(exampleListener());
		container.setAcknowledgeMode(AcknowledgeMode.MANUAL);
		// container.setChannelTransacted(transactional);
		return container;
	}

	@Bean
	public ChannelAwareMessageListener exampleListener() {
		ChannelAwareMessageListener chnlAwareListener = new ChannelAwareMessageListener() {
			public void onMessage(Message message, Channel channel) throws Exception {

				String strMsg = null;
				Long orderId = null;
				String eventCode = null;

				ObjectMapper mapper = new ObjectMapper();
				try {
					strMsg = new String(message.getBody());
					System.out.println("OrcerEventProcessor received event: " + strMsg);

					BaseEvent newEvent = mapper.readValue(strMsg, BaseEvent.class);

					eventCode = newEvent.getEventCode();
					orderId = newEvent.getOrderId();
					
					newEvent = processEvent(newEvent);
					
					System.out.println("Event saved : id = " + newEvent.getId() + "; results = " + newEvent.getResults());
					
				} catch (Exception e) {
					System.out.println("Process error: " + e.getMessage() + "; sending to error queue");

					MessageProperties prop = new MessageProperties();
					prop.setContentType(MessageProperties.CONTENT_TYPE_JSON);
					rabbitTemplate(connectionFactory()).send(OrderUtil.EXCHANGE_ORDER_EVENTS,
							OrderUtil.ROUTING_KEY_ERROR_EVENTS, new Message(strMsg.getBytes(), prop));

				}

				channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
				System.out.println("Event processed successfully: " + eventCode + " for Order Id " + orderId);

			}
		};

		/*
		 * MessageListener listener = new MessageListener() { public void
		 * onMessage(Message message) { String strMsg = new
		 * String(message.getBody()); System.out.println("received msg: " +
		 * strMsg);
		 * 
		 * ObjectMapper mapper = new ObjectMapper(); try { BaseEvent event =
		 * mapper.readValue(strMsg, BaseEvent.class);
		 * System.out.println("Parsed message: "+event.getEventCode()+":"+event.
		 * getOrder().getId()); } catch (Exception e) {
		 * System.out.println("Parsing error: " + e.getMessage()); }
		 * 
		 * //connectionFactory().createConnection().createChannel(true). } };
		 */
		return chnlAwareListener;
	}

	public BaseEvent processEvent(BaseEvent newEvent) throws Exception {
		// Save this event
		newEvent = saveEvent(newEvent);
		System.out.println("Event saved : id = " + newEvent.getId());

		// Get Order
		Order order = getOrder(newEvent.getOrderId());
		// newEvent.setOrder(order);

		// Get all past events for this order
		List<BaseEvent> pastEvents = getEvents(newEvent.getOrderId());

		List<Action> actions = invokeRule(newEvent, pastEvents, order, order.getCustomer());

		System.out.println("Retreived order details, past event history for the order and invoked rule");

		/*
		 * RuleApplication rule = new RuleApplication(); List<Action> actions =
		 * rule.processEvent(newEvent, pastEvents,order, order.getCustomer());
		 */

		List<String> results = new ArrayList<String>();

		for (Action action : actions) {

			AbstractActionHandler handler = null;

			if (action.getType().equals(Action.TYPE_GENERATE_EVENT)) {

				handler = new EventGenerationActionHandler(rabbitTemplate(connectionFactory()), action);
				handler.process(newEvent, pastEvents, order, order.getCustomer());

				results.add("Event genetated: " + action.getCode() + " : params = " + action.getParams());

			} else {

				handler = ActionHandlerFactory.getActionHandler(rabbitTemplate(connectionFactory()), action);
				if (handler != null) {

					handler.process(newEvent, pastEvents, order, order.getCustomer());

					results.add("Action executed: " + action.getCode() + " : params = " + action.getParams());
				} else {
					throw new Exception("No handler exists for action :: " + action.getCode());
				}
			}
		}

		newEvent.setResults(results);

		// Processing done, update the event with result in event repository
		newEvent = saveEvent(newEvent);
		
		return newEvent;
	}

	private Order getOrder(Long orderId) throws Exception {
		String strUri = "http://ORDERSERVICE/orders/" + orderId;
		URI uri = UriComponentsBuilder.fromUriString(strUri).build().toUri();
		return restTemplate.getForObject(uri, Order.class);
	}

	public List<Action> invokeRule(BaseEvent newEvent, List<BaseEvent> pastEvents, Order order, Customer customer)
			throws Exception {
		List<Action> actions = new ArrayList<Action>();

		String strUri = "http://ORDERRULESERVICE/orderrule";
		URI uri = UriComponentsBuilder.fromUriString(strUri).build().toUri();
		RuleInput ruleIp = new RuleInput(newEvent, pastEvents, order, customer);
		Action[] result = restTemplate.postForObject(strUri, ruleIp, Action[].class);
		for (int i = 0; i < result.length; i++) {
			actions.add(result[i]);
		}
		return actions;
	}

	private List<BaseEvent> getEvents(Long orderId) throws Exception {
		List<BaseEvent> events = new ArrayList<BaseEvent>();
		String strUri = "http://EVENTSERVICE/events?orderid=" + orderId;
		URI uri = UriComponentsBuilder.fromUriString(strUri).build().toUri();
		BaseEvent[] result = restTemplate.getForObject(uri, BaseEvent[].class);
		for (int i = 0; i < result.length; i++) {
			events.add(result[i]);
		}
		return events;
	}

	private BaseEvent saveEvent(BaseEvent event) throws Exception {
		String strUri = "http://EVENTSERVICE/events";
		URI uri = UriComponentsBuilder.fromUriString(strUri).build().toUri();
		return restTemplate.postForObject(uri, event, BaseEvent.class);
	}

}
