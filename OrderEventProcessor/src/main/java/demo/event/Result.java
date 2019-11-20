package demo.event;

import java.io.*;

import demo.event.*;
import demo.model.*;

public class Result implements Serializable {

	public Result(String message, BaseEvent event) {
		super();
		this.message = message;
		this.event = event;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public BaseEvent getEvent() {
		return event;
	}

	public void setEvent(BaseEvent event) {
		this.event = event;
	}

	String message;
	BaseEvent event;
	
	public Result() {
		// TODO Auto-generated constructor stub
	}

}
