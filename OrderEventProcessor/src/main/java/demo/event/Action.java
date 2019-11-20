package demo.event;

import java.io.*;
import java.util.*;

public class Action implements Serializable {
	
	public static final String TYPE_GENERATE_EVENT = "TYPE_GENERATE_EVENT";
	public static final String TYPE_EXECUTE = "ACTION_TYPE_EXECUTE";
	
	
	private String type;
	private String code;
	private List<EventParam> params;

	public Action(){
	}
	
	public Action(String code, List<EventParam> params) {
		super();
		this.type=TYPE_EXECUTE;
		this.code = code;
		this.params = params;
	}
	
	public Action(String type, String code, List<EventParam> params) {
		super();
		this.type=type;
		this.code = code;
		this.params = params;
	}

	public EventParam getEventParam(String key) {
		EventParam param = null;
		if (this.params != null && this.params.size() > 0) {
			for (EventParam eventParam : params) {
				if(eventParam.getKey().equals(key)){
					param = eventParam;
				}
			}
		}
		return param;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public List<EventParam> getParams() {
		return params;
	}

	public void setParams(List<EventParam> params) {
		this.params = params;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
