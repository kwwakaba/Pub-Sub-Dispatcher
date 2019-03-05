package src.threads;

import src.Digest;
import src.Dispatcher;
import src.Event;
import src.messages.GossipMessage;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Thread for starting up the Gossip round.
 */
public class StartGossipThread extends Thread {
    private static final int GOSSIP_TIMEOUT = 5000;

    // stores identifier of the Dispatcher that started this Thread
    private String identifier;
    private Dispatcher dispatcher;

    public void setup(String identifier) {
        this.identifier = identifier;
    }

    public void setup(Dispatcher dispatcher){
        this.dispatcher = dispatcher;
    }

    public void run() {
        System.out.println("StartGossipThread has started!");
        while (true) {
            try {
                Thread.sleep(GOSSIP_TIMEOUT);
                startGossipRound();
            } catch (Exception e) {
                System.out.println("Error in StartGossipThread caught.");
                System.out.println("Something went wrong with the thread sleeping." + e.getStackTrace());
            }
        }
    }

    private void startGossipRound() throws InterruptedException {
        // choose a random pattern from the subscription table
        if (Dispatcher.getSubscriptionTable().isEmpty()) {
            System.out.println("Subscription Table is empty, so aborting gossip round.");
            return;
        }
        ArrayList<String> keysAsArray = new ArrayList<>(Dispatcher.getSubscriptionTable().keySet());
        String selectedPattern = keysAsArray.get((int)(Math.random() * keysAsArray.size()));

        Digest digest = new Digest();

        if(Dispatcher.getEventCache().size() > 0){
            for (Event e : Dispatcher.getEventCache()) {
                if (e.getPattern().equals(selectedPattern)) {
                    digest.addEvent(e);
                }
            }
        } else{
            System.out.println("Event Cache is empty");
        }


        GossipMessage gossipMessage = new GossipMessage("gossip message description", identifier, selectedPattern, digest);

        LinkedList<Dispatcher> subscriberList = Dispatcher.getDispatcherListForPattern(selectedPattern);
        byte[] data;
        try{
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(gossipMessage);
            data = baos.toByteArray();
            DatagramSocket serverSocket = new DatagramSocket(dispatcher.getPortNumber());
            for(Dispatcher subDispatcher: subscriberList){
                serverSocket.send(new DatagramPacket(data, data.length, subDispatcher.getIpAddress(), subDispatcher.getPortNumber()));
            }
        }catch (Exception e){
            System.out.println("Something went wrong with startGossipRound(). " + e.getStackTrace());
        }

    }
}
