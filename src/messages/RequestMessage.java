package src.messages;

import src.Message;

import java.io.Serializable;
import java.util.LinkedList;

/**
 * This class is the Message object for RequestMessage.
 *
 * A request message contains a list of events requested by the distributor.
 */
public class RequestMessage extends Message implements Serializable {

    /** List of events ids. **/
	private LinkedList<String> eventList;

    /**
     * Constructor.
     */
	public RequestMessage(String description) {
		super(description);
		
		eventList = new LinkedList<>();
	}

    /**
     * Add an event to the message.
     * @param e event to add.
     */
	public void addEvent(String e) {
		eventList.add(e);
	}

    /**
     * Accessor for list in message.
     * @return list of events ids in message.
     */
	public LinkedList<String> getEventList() {
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
