package demo.event;

import java.util.*;

import demo.event.*;

public class GenerateEventAction extends Action {
	public GenerateEventAction(){
	}
	
	public GenerateEventAction(String code, List<EventParam> params) {
		super(Action.TYPE_GENERATE_EVENT, code, params);
	}

}
