package demo.model.repo;

import java.util.List;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.*;

import demo.model.*;

public interface OrderRepository extends JpaRepository<Order, Long> {

	List<Order> findByStatus(String lastName);
	
	List<Order> findByCustomer_Id(Long id);
}