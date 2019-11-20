package demo.event;

import java.io.*;
import java.util.*;


public abstract class AbstractEvent implements Serializable{

    public String id;

	private String eventCode;
	private String correlationId;
	private String source;
	
	private List<EventParam> params;
	private List<String> results; 

	public AbstractEvent(){	
	}
	

	public AbstractEvent(String eventCode,  String source, String correlationId, List<EventParam> params) {
		super();
		this.eventCode = eventCode;
		this.correlationId = correlationId;
		this.source = source;
		
		if(params != null){
			this.params = params;
		}else{
			this.params = new ArrayList<EventParam>();
		}
		
	}

	public List<String> getResults() {
		return results;
	}


	public void setResults(List<String> results) {
		this.results = results;
	}
	public void setParams(List<EventParam> params) {
		this.params = params;
	}

	public List<EventParam> getParams() {
		return params;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getEventCode() {
		return eventCode;
	}

	public void setEventCode(String eventCode) {
		this.eventCode = eventCode;
	}

	public String getCorrelationId() {
		return correlationId;
	}

	public void setCorrelationId(String correlationId) {
		this.correlationId = correlationId;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}
	
	public void addParam(EventParam param){
		if(this.params == null){
			this.params = new ArrayList<EventParam>();
		}
		this.params.add(param);
	}
	
	public EventParam getParam(String key){
		if(this.params != null){
			for (EventParam eventParam : params) {
				if(eventParam.getKey().equals(key)){
					return eventParam;
				}
			}
		}
		return null;
	}
	
	public void addRsult(String result){
		if(this.results == null){
			this.results = new ArrayList<String>();
		}
		this.results.add(result);
	}
}
