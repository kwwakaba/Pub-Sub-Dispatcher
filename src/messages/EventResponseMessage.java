package src.messages;

import src.Event;
import src.Message;

import java.io.Serializable;
import java.util.LinkedList;

/** Class for EventResponseMessage objects.
 *
 * Event Messages are sent from one Dispatcher to another
 * after receipt of a RequestMessage.
 */
public class EventResponseMessage extends Message implements Serializable {

    /** A list of events requested by the client. */
    private LinkedList<Event> events;

    /**
     * Constructor
     * @param description description of the message
     * @param events list of events that were requested.
     */
    public EventResponseMessage(String description,
                                LinkedList<Event> events) {
        super(description);
        this.events = events;
    }

    /**
     * Returns events of message
     * @return list of events.
     */
    public LinkedList<Event> getEvents() {
        return events;
    }

	@Override
	public String toString() {
		String str = "EventList in EventResponseMessage (with source node id: " + getSourceNodeId() + "):";
		for (int i = 0; i < events.size(); i++) {
			str += ("\n\tEvent " + i + ": " + events.get(i));
		}
		return str;
	}

}
