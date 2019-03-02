package src.messages;

import src.Event;
import src.Message;

import java.io.Serializable;
import java.net.DatagramPacket;
import java.util.List;

/** Class for EventResponseMessage objects.
 *
 * Event Messages are sent from one Dispatcher to another
 * after receipt of a RequestMessage.
 */
public class EventResponseMessage extends Message implements Serializable {

    /** A list of events requested by the client. */
    private List<Event> events;

    public EventResponseMessage(String description,
                                List<Event> events) {
        super(description);
        this.events = events;
    }

    public List<Event> getEvents() {
        return events;
    }
}
