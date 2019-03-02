package src.threads;

/**
 * Thread for starting up the Gossip round.
 */
public class StartGossipThread extends Thread {

    public void run() {
        System.out.println("StartGossipThread started.");
        while (true) {
            try {
                //TODO - Implement this
                Thread.sleep(10000);

                //StartGossipRound()

            } catch (Exception e) {
                System.out.println("Error in StartGossipThread caught. \n");
                System.out.println("Something went wrong with the thread sleeping." + e.getStackTrace());
            }
        }
    }
}
