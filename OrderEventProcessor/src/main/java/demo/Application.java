package demo;

import java.io.*;

import org.codehaus.jackson.map.*;
import org.springframework.amqp.*;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.cloud.client.discovery.*;
import org.springframework.cloud.netflix.eureka.*;
import org.springframework.context.annotation.*;
import org.springframework.web.bind.annotation.*;

import demo.event.*;
import demo.model.*;
import demo.model.Address;
import demo.util.*;
import springfox.documentation.builders.*;
import springfox.documentation.spi.*;
import springfox.documentation.spring.web.plugins.*;
import springfox.documentation.swagger2.annotations.*;

@SpringBootApplication
@EnableEurekaClient
@EnableSwagger2
@RestController
public class Application {

	@Autowired
	RabbitTemplate rabbitTemplate;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	// @Autowired
	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2).select().apis(RequestHandlerSelectors.any())
				.paths(PathSelectors.any()).build();
	}

	@RequestMapping("/test")
	public String test() {

		Address addr = new Address("My address", "A1B2C3");
		Address shipAddr = new Address("Ship address", "P9Q8R7");
		Customer cust = new Customer("Jack", "Bauer", addr);

		Item item1 = new Item("Drug_A", new Double(2), new Double(100), new Double(0.95), null);
		Item item2 = new Item("Drug_B", new Double(2), new Double(100), new Double(0.95), null);

		Order order = new Order(cust, addr, shipAddr);
		order.addItem(item1);
		order.addItem(item2);

		BaseEvent event = new BaseEvent(OrderUtil.EVENT_ORDER_SUBMIT, OrderUtil.EVENT_SOURCE_CRM, "CRM1001",
				new Long(1001));
		String msg = "";
		try {
			msg = new ObjectMapper().writeValueAsString(event);
		} catch (IOException e) {
			e.printStackTrace();
		}

		MessageProperties prop = new MessageProperties();
		prop.setContentType(MessageProperties.CONTENT_TYPE_JSON);
		rabbitTemplate.send(OrderUtil.EXCHANGE_ORDER_EVENTS, OrderUtil.ROUTING_KEY_ORDER_EVENTS,
				new Message(msg.getBytes(), prop));
		System.out.println("Queued msg = " + msg);

		return msg;
	}

}
