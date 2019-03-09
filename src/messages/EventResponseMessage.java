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

    public EventResponseMessage(String description,
                                LinkedList<Event> events) {
        super(description);
        this.events = events;
    }

    public LinkedList<Event> getEvents() {
        return events;
    }

}
