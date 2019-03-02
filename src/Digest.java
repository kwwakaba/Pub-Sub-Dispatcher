package src;

import java.io.Serializable;
import java.util.LinkedList;

public class Digest implements Serializable {
	
	// MARK: - Instance Variables
	
	private LinkedList<Event> eventList;
	
	// MARK: - Constructor
	
	public Digest() {
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