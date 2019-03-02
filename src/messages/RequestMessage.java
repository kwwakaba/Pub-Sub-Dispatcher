package src.messages;

import java.io.Serializable;
import java.util.LinkedList;
import src.Event;
import src.Message;


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
	
}