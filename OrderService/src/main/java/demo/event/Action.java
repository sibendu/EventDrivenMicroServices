package demo.event;

import java.io.*;
import java.util.*;

public class Action implements Serializable {
	private String code;
	private List<EventParam> params;

	public Action(String code, List<EventParam> params) {
		super();
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

}
