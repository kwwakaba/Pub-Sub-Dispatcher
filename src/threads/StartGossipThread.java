package src.threads;

import src.Digest;
import src.Dispatcher;
import src.Event;
import src.messages.GossipMessage;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Thread for starting up the Gossip round.
 */
public class StartGossipThread extends Thread {
    private static final int GOSSIP_TIMEOUT = 5000;

    // stores identifier of the Dispatcher that started this Thread
    private String identifier;

    public void setup(String identifier) {
        this.identifier = identifier;
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

    private void startGossipRound() {
        // choose a random pattern from the subscription table
        if (Dispatcher.getSubscriptionTable().isEmpty()) {
            System.out.println("Subscription Table is empty, so aborting gossip round.");
            return;
        }
        ArrayList<String> keysAsArray = new ArrayList<>(Dispatcher.getSubscriptionTable().keySet());
        String selectedPattern = keysAsArray.get((int)(Math.random() * keysAsArray.size()));

        Digest digest = new Digest();

        for (Event e : Dispatcher.getEventCache()) {
            if (e.getPattern().equals(selectedPattern)) {
                digest.addEvent(e);
            }
        }

        GossipMessage gossipMessage = new GossipMessage("gossip message description", identifier, selectedPattern, digest);

        // TODO: send gossipMessage to one or more subscribers for the selectedPattern
    }
}
