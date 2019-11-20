package demo.model.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.*;

import demo.event.*;
import demo.model.*;

public interface EventRepository extends MongoRepository<BaseEvent, String> {

    //@Query("{customer.$firstName : ?0}")
	//public List<BaseEvent> findByCustomer_FirstName(String firstName);
	
	public List<BaseEvent> findByCorrelationId(String id);
	
	public List<BaseEvent> findByOrder_Customer_Id(Long id);
	
	public List<BaseEvent> findByOrderId(Long id);
	
	//public List<BaseEvent> findByCustomer_Address_Zip(String zip);
    
    public List<BaseEvent> findByEventCode(String code);
}
