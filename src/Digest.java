package src;

import java.io.Serializable;
import java.util.LinkedList;

/**
 * Container for eventsIds that go in a gossip message.
 */
public class Digest implements Serializable {
	
	private LinkedList<String> eventList;

	public Digest() {
		eventList = new LinkedList<>();
	}

	public void addEvent(String e) {
		eventList.add(e);
	}
	
	public LinkedList<String> getEventList() {
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
