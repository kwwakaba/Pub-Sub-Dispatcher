package src;

import java.io.Serializable;
import java.util.LinkedList;

/**
 * Container for eventsIds that go in a gossip message.
 */
public class Digest implements Serializable {
	
	private LinkedList<Event> eventList;

	public Digest() {
		eventList = new LinkedList<>();
	}

	public void addEvent(Event e) {
		eventList.add(e);
	}
	
	public LinkedList<Event> getEventList() {
		return eventList;
	}
	
	@Override
	public String toString() {
		String str = "EventList in Digest:";
		for (int i = 0; i < eventList.size(); i++) {
			str += ("\n\tEvent " + i + ": " + eventList.get(i));
		}
		return str;
	}
	
}
