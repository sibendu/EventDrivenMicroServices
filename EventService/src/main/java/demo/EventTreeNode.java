package demo;

import java.io.*;
import java.util.*;

import demo.event.*;

public class EventTreeNode implements Serializable{
	private BaseEvent event;
	private List<EventTreeNode> children;
	
	public EventTreeNode(BaseEvent event) {
		this.event = event;
		this.children = new ArrayList<EventTreeNode>();
	}
	
	public void addChild(EventTreeNode child){
		this.children.add(child);
	}

	public BaseEvent getEvent() {
		return event;
	}

	public void setEvent(BaseEvent event) {
		this.event = event;
	}

	public List<EventTreeNode> getChildren() {
		return children;
	}

	public void setChildren(List<EventTreeNode> children) {
		this.children = children;
	}

}
