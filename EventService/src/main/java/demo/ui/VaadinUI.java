package demo.ui;

import java.util.*;

import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import com.vaadin.annotations.*;
import com.vaadin.data.util.*;
import com.vaadin.server.*;
import com.vaadin.spring.annotation.*;
import com.vaadin.ui.*;

import demo.*;
import demo.event.*;
import demo.model.*;
import demo.model.repo.*;

@SpringUI
@Theme("valo")
public class VaadinUI extends UI {

	EventRepository repo;
	Grid grid;
	TextField filter;
	Tree menu;
	
	@Autowired
	public VaadinUI(EventRepository repo) {
	    this.repo = repo;
	    this.filter = new TextField();
	    this.grid = new Grid();
	    menu = new Tree();

	    grid.setHeight("90%");
	    grid.setWidth("90%");

	    this.filter.addTextChangeListener(e -> listEvents(e.getText()));

	}

	@Override
	protected void init(VaadinRequest request) {
		VerticalLayout mainLayout = new VerticalLayout(filter, grid, menu);
		setContent(mainLayout);
	   // setContent(grid);
	    
	    listEvents(null);
	}

	private void listEvents(String orderId) {
		List<BaseEvent> events = new ArrayList<>();
		if(orderId == null || orderId.trim().equals("")){
			for (BaseEvent ev : repo.findAll()) {
				events.add(ev);
			}
		}else{
			events = repo.findByOrderId(new Long(orderId));
		}
		grid.setContainerDataSource(
	            new BeanItemContainer(BaseEvent.class, events));
		
		
		if(orderId == null || orderId.trim().equals("")){
			menu = new Tree();
		}else{
			menu = new Tree();
			
			EventTreeNode node = buildTree(events.get(0)); 
			
			buildMenu(node, node.getChildren());
		}
		
//		BaseEvent e1 = new BaseEvent("E1","src","x",null);
//		menu.expandItem(e1); 
	}
	
	public void buildMenu(EventTreeNode parent, List<EventTreeNode> children){
		menu.addItem(getText(parent));
		for (EventTreeNode child : children) {
			if(child.getChildren() == null){
				//No more children
				menu.addItem(getText(child));
				menu.setParent(getText(child), getText(parent));
			}else{
				buildMenu(child, child.getChildren());;
			}
		}
	}
	
	public EventTreeNode buildTree(BaseEvent event){
		EventTreeNode current = new EventTreeNode(event);
		List<BaseEvent> children = repo.findByCorrelationId(event.getId());
		for (BaseEvent child : children) {
			current.addChild(buildTree(child));
		} 
		return current;
	}
	
	public String getText(EventTreeNode node){
		return node.getEvent().getEventCode()+" :: params == "+node.getEvent().getResults();
	}
}
