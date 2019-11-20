package demo.event;

import java.io.*;

public class EventParam implements Serializable {
	private String key;
	private Object value;
	
	public EventParam() {
	}
	public EventParam(String key, Object value) {
		this.key=key;
		this.value=value;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

}
