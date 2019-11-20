package demo;

import org.slf4j.*;
import org.springframework.amqp.rabbit.core.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.*;
import org.springframework.context.annotation.*;

import demo.model.*;
import demo.model.repo.*;
import springfox.documentation.builders.*;
import springfox.documentation.spi.*;
import springfox.documentation.spring.web.plugins.*;
import springfox.documentation.swagger2.annotations.*;

@SpringBootApplication
@EnableDiscoveryClient
@EnableSwagger2
public class Application {

	private static final Logger log = LoggerFactory.getLogger(Application.class);

	@Autowired
	OrderRepository repository;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	// @Autowired
	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2).select().apis(RequestHandlerSelectors.any())
				.paths(PathSelectors.any()).build();
	}

	@Bean
	public CommandLineRunner demo(OrderRepository repository) {
		return (args) -> {
			Address addr = new Address("My address", "A1B2C3");
			Address shipAddr = new Address("Ship address", "P9Q8R7");
			Customer cust = new Customer("Jack", "Bauer", addr);

			Item item1 = new Item("TV", new Double(2), new Double(100), new Double(0.95), null);
			Item item2 = new Item("TV", new Double(2), new Double(100), new Double(0.95), null);

			Order order = new Order(cust, addr, shipAddr);
			order.addItem(item1);
			order.addItem(item2);

			repository.save(order);

			log.info("Orders found with findAll():");
			log.info("-------------------------------");
			for (Order Order : repository.findAll()) {
				log.info(Order.toString());
			}
			log.info("");

			// fetch an individual Order by ID
			Order Order = repository.findOne(1L);
			log.info("Order found with findOne(1L):");
			log.info("--------------------------------");
			log.info(Order.toString());
			log.info("");
		};
	}
}
