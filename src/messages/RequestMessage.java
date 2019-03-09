package src.messages;

import src.Event;
import src.Message;

import java.io.Serializable;
import java.util.LinkedList;


public class RequestMessage extends Message implements Serializable {
	
	// MARK: - Instance Variables
	
	private LinkedList<Event> eventList;
	
	// MARK: - Constructors
	
	public RequestMessage() {
		super("");
		
		eventList = new LinkedList<>();
	}
	
	public RequestMessage(String description) {
		super(description);
		
		eventList = new LinkedList<>();
	}
	
	// MARK: - Instance Methods
	
	public void addEvent(Event e) {
		eventList.add(e);
	}
	
	public LinkedList<Event> getEventList() {
		return eventList;
	}
	
	@Override
	public String toString() {
		String str = "EventList in RequestMessage (with source node id: " + getSourceNodeId() + "):";
		for (int i = 0; i < eventList.size(); i++) {
			str += ("\n\tEvent " + i + ": " + eventList.get(i));
		}
		return str;
	}
	
}
