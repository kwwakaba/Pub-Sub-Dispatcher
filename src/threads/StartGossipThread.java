package src.threads;

/**
 * Thread for starting up the Gossip round.
 */
public class StartGossipThread extends Thread {
    private static final int GOSSIP_TIMEOUT = 60000;

    public void run() {
        System.out.println("StartGossipThread started.");
        while (true) {
            try {
                Thread.sleep(GOSSIP_TIMEOUT);
                startGossipRound();
            } catch (Exception e) {
                System.out.println("Error in StartGossipThread caught. \n");
                System.out.println("Something went wrong with the thread sleeping." + e.getStackTrace());
            }
        }
    }

    private void startGossipRound() {
        // start gossip round
    }
}
