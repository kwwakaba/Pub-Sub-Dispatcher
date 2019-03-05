package src.threads;

import src.Digest;
import src.Dispatcher;
import src.Event;
import src.Message;
import src.messages.EventResponseMessage;
import src.messages.GossipMessage;
import src.messages.RequestMessage;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.LinkedList;


/**
 * Thread for handling the messages we received. This thread is started by
 * the Socket Listener Thread.
 */
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
        System.out.println("Handling event response message " + eventResponseMessage);

        //TODO - Extract the events from message and add the events to the cache.
        try{
            for(Event event: eventResponseMessage.getEvents()){
                Dispatcher.addEventToCache(event);
            }
        }catch (Exception e){
            System.out.println("Error occurred trying to populate cache from response message. " + e.getStackTrace());
        }


    }

    public void handleRequestMessage(RequestMessage requestMessage) throws InterruptedException {
        System.out.println("Handling request message. " + requestMessage);

        LinkedList<Event> eventList = requestMessage.getEventList();
        LinkedList<Event> eventCache = Dispatcher.getEventCache();
        LinkedList<Event> eventResponseList = new LinkedList<>();

        byte[] data;
        for(Event event: eventList){
            if(eventCache.contains(event)){
                eventResponseList.add(event);
            }
        }

        try{
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(eventResponseList);
            data = baos.toByteArray();

            packet.setData(data);
            DatagramSocket datagramSocket = new DatagramSocket();
            datagramSocket.send(packet);

        } catch (Exception e){
            System.out.println("Something went wrong trying to send handleRequestMessage. " + e.getStackTrace());
        }
    }

    public void handleGossipMessage(GossipMessage gossipMessage) throws InterruptedException {
        System.out.println("Handling gossip message" + gossipMessage);
        LinkedList<Dispatcher> dispatcherList = Dispatcher.getDispatcherListForPattern(gossipMessage.getPattern());

        //Checking to see if self is subscribed to pattern
        if(dispatcherList.contains(this)){
            RequestMessage reqMsg = new RequestMessage();
            Digest digest = gossipMessage.getDigest();
            LinkedList<Event> eventList = digest.getEventList();

            for(Event event: eventList){
                if(!isReceived(event.getIdentifier())){
                    reqMsg.addEvent(event);
                }
            }
            //Send request message to Gossip Message Initiator
            if(reqMsg.getEventList().size() != 0){
                sendDispatcherMessage(reqMsg);
            }

            //Send request message to neighbors
            for(Dispatcher dispatcher: Dispatcher.getNeighbors()){
                try{
                    byte[] data = getDataByteArray(reqMsg);
                    DatagramSocket serverSocket = new DatagramSocket(packet.getPort());
                    serverSocket.send(new DatagramPacket(data, data.length, dispatcher.getIpAddress(), dispatcher.getPortNumber()));
                } catch (Exception e){
                    System.out.println("Something went wrong trying to send handleGossipMessage. " + e.getStackTrace());
                }
            }
        }
    }

    public byte[] getDataByteArray(Message message){
        byte[] data = null;

        try{
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(message);
            data = baos.toByteArray();

            System.out.println("Successfully serialized the data to a byte array.");

        }catch (Exception e){
            System.out.println("Something went wrong trying to get data byte array. " + e.getStackTrace());
        }
        return data;
    }

    public void sendDispatcherMessage(Message message){
        try{
            byte[] reqData = getDataByteArray(message);

            packet.setData(reqData);
            DatagramSocket datagramSocket = new DatagramSocket();
            datagramSocket.send(packet);
        }catch (Exception e){
            System.out.println("Something went wrong trying to sendDispatcherMessage. " + e.getStackTrace());
        }
    }


    public void sendMessage() {
        try {
            byte[] data = getDataByteArray(message);

            //Send data
            DatagramSocket serverSocket = new DatagramSocket(packet.getPort());
            serverSocket.send(new DatagramPacket(data, data.length, packet.getAddress(), packet.getPort()));
        } catch (Exception e) {
            System.out.println("Something went wrong trying to sendMessage. " + e.getStackTrace());
        }

    }

    public void run() {

        try{

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

        }catch (Exception e){
            System.out.println("Issue in the MessageHandlerThread run(). " + e.getStackTrace());
        }

    }

    /**  returns true if the dispatcher received an event with the given id **/
    public boolean isReceived(String id) throws InterruptedException {
        LinkedList<Event> eventCache = Dispatcher.getEventCache(); // Will Fix

        for(Event event: eventCache){
            if(event.getIdentifier() == id){
                return true;
            }
        }
        return false;
    }
}
