# Event Driven MicroServices - A Reference implementation

https://www.linkedin.com/pulse/microservice-event-driven-architecture-containers-devops-sibendu-das/

This is a data service for managing "Event" objects; provides usual CRUD operations.

An event object has an event code, and collection of parameters (name-value pairs) -> look at BaseEvent.java  

Built using Spring Boot and MongoDB. 

The code registers with a service registry (Eureka for now). So if you try to run, either you need to run one (simple and plenty of examples on net) and change the URL in Spring configuration properties file. The easier thing for now might be to just comment out @EnableEurekaClient annotation in the main class. 

For running on your laptop or PC  –
- Setup and run MongoDB
- Point to MongoDB in application.properties (if running on same machine, just comment them out)
- Run – gradle bootRun    

