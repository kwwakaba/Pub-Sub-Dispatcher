package src.threads;

import src.Message;
import src.messages.EventResponseMessage;
import src.messages.GossipMessage;
import src.messages.RequestMessage;

import java.net.DatagramPacket;

public class MessageHandlerThread extends Thread {

    /** The converted data from the packet as an object. */
    Message message;

    /** The packet that was read from the socket. */
    DatagramPacket packet;

    /** Constructor
     *
     * @param packet the unparsed string form of the message received on the socket.
     */
    public MessageHandlerThread(DatagramPacket packet, Message message) {
        this.packet = packet;
        this.message = message;
    }

    public void handleEventResponseMessage(EventResponseMessage eventResponseMessage) {

    }

    public void handleRequestMessage(RequestMessage requestMessage) {

    }

    public void handleGossipMessage(GossipMessage gossipMessage) {

    }

    public void sendMessage() {

    }

    public void run() {

        // Call appropriate handler.
        if (message instanceof EventResponseMessage) {
            System.out.println("EventResponseMessage received. \n");

            //No response needed, processing events in response to a request.
            EventResponseMessage eventResponseMessage = (EventResponseMessage) message;
            handleEventResponseMessage(eventResponseMessage);

        } else if (message instanceof RequestMessage) {
            System.out.println("RequestMessage received. \n");

            RequestMessage requestMessage = (RequestMessage) message;
            handleRequestMessage(requestMessage);
            sendMessage();
        } else if (message instanceof GossipMessage) {
            System.out.println("GossipMessage received. \n");

            GossipMessage gossipMessage = (GossipMessage) message;
            handleGossipMessage(gossipMessage);
            sendMessage();
        } else {
            System.out.println("Something went wrong and we received a message we didn't understand. \n");
         }

    }
}
