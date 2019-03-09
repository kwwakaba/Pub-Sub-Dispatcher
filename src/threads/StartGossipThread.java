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
import java.util.Set;

/**
 * Thread for starting up the Gossip round.
 */
public class StartGossipThread extends Thread {
    private static final int GOSSIP_TIMEOUT = 5000;

    // stores identifier of the Dispatcher that started this Thread
    private Dispatcher dispatcher;

    public void setup(Dispatcher dispatcher) {
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
        String selectedPattern = keysAsArray.get((int) (Math.random() * keysAsArray.size()));

        System.out.println("Starting gossip round with pattern " + selectedPattern);
        Digest digest = new Digest();

        try {
            for (Event e : dispatcher.getEventCache()) {
                if (e.getPattern().equals(selectedPattern)) {
                    digest.addEvent(e);
                    System.out.println("Event id:" + e.getIdentifier() + " found that matched pattern: "
                            + selectedPattern);
                }
            }
        } catch (InterruptedException e) {
            System.out.println("We caught an InterruptedException in start gossip thread.");
        }

        GossipMessage gossipMessage = new GossipMessage("gossip message description", dispatcher.getIdentifier(), selectedPattern, digest);
	gossipMessage.setSourceNodeId(dispatcher.getIdentifier());
	System.out.println("About to send gossipMessage: " + gossipMessage);

        Set<String> subscriberList = dispatcher.getDispatcherListForPattern(selectedPattern);
        byte[] data;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(gossipMessage);
            data = baos.toByteArray();
            DatagramSocket serverSocket = dispatcher.getSendSocket();

            for (String dispatcherID : subscriberList) {
                // skips this dispatcher (prevents sending message to self!)
                if (dispatcherID.equals(dispatcher.getIdentifier())) { continue; }
                Dispatcher subDispatcher = dispatcher.getNeighbors().stream().filter(x -> dispatcherID.equals(x.getIdentifier())).findFirst().orElse(null);
                if (subDispatcher != null && data != null
                        && data.length > 0) {
                    DatagramPacket sendMe = new DatagramPacket(data, data.length, subDispatcher.getIpAddress(), subDispatcher.getPortNumber());
                    System.out.println("About to send message to " + dispatcherID);
                    serverSocket.send(sendMe);
                    System.out.println("Sent message");
                } else {
                    System.out.println("Dispatcher or data was null.");
                }
            }
        } catch (Exception e) {
            System.out.println("Something went wrong with startGossipRound(). ");
            e.printStackTrace();
        }

    }
}

