package demo;

import java.util.*;

import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.cloud.netflix.eureka.*;
import org.springframework.context.annotation.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.*;

import demo.event.*;
import demo.model.*;
import demo.model.repo.*;
import springfox.documentation.builders.*;
import springfox.documentation.schema.*;
import springfox.documentation.service.*;
import springfox.documentation.spi.*;
import springfox.documentation.spi.service.contexts.*;
import springfox.documentation.spring.web.plugins.*;
import springfox.documentation.swagger2.annotations.*;

import static springfox.documentation.schema.AlternateTypeRules.newRule;

import com.fasterxml.classmate.TypeResolver;
import static com.google.common.collect.Lists.newArrayList;

@SpringBootApplication
@EnableEurekaClient
@EnableSwagger2
public class Application implements CommandLineRunner {

	@Autowired
	private EventRepository repository;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	// start Swagger stuff
	
	// @Autowired
	@Bean
    public Docket api() { 
        return new Docket(DocumentationType.SWAGGER_2)  
          .select()                                  
          .apis(RequestHandlerSelectors.any())              
          .paths(PathSelectors.any())                          
          .build();                                           
    }
	
	// end Swagger stuff

	@Override
	public void run(String... args) throws Exception {

		repository.deleteAll();

		BaseEvent event = new BaseEvent("TEST_EVENT", "CRM", "CRM999", new Long(999));
		repository.save(event);
		
		// fetch all events
		System.out.println("Events found with findAll():");
		System.out.println("-------------------------------");
		for (BaseEvent thisEvent : repository.findAll()) {
			System.out.println(thisEvent);
		}
		System.out.println(" ----- ");

		// List<BaseEvent> result = repository.findByEventCode("ORDER_SUBMIT");
		// result.forEach((ev) -> System.out.println(ev.toString()));

		// List<BaseEvent> result = repository.findByCustomer_FirstName("Jack");
		// result.forEach((ev) -> System.out.println(ev.toString()));

		//List<BaseEvent> result = repository.findByCustomer_Address_Zip("A1B2C3");
		//result.forEach((ev) -> System.out.println(ev.toString()));
	}

}
