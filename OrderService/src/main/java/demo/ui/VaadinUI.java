package demo.ui;

import java.util.*;

import org.springframework.beans.factory.annotation.*;

import com.vaadin.annotations.*;
import com.vaadin.data.util.*;
import com.vaadin.server.*;
import com.vaadin.spring.annotation.*;
import com.vaadin.ui.*;

import demo.model.*;
import demo.model.repo.*;

@SpringUI
@Theme("valo")
public class VaadinUI extends UI {

	CustomerRepository repo;
	Grid grid;

	@Autowired
	public VaadinUI(CustomerRepository repo) {
	    this.repo = repo;
	    this.grid = new Grid();
	}

	@Override
	protected void init(VaadinRequest request) {
	    setContent(grid);
	    grid.setHeight("90%");
	    grid.setWidth("90%");
	    listCustomers();
	}

	private void listCustomers() {
		List<Customer> customers = new ArrayList<>();
		for (Customer customer : repo.findAll()) {
			customers.add(customer);
		}
	    grid.setContainerDataSource(
	            new BeanItemContainer(Customer.class, customers));
	}
	
}
