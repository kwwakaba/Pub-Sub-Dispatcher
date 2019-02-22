package src.threads;

public class StartGossipThread extends Thread {

    public void run() {
        while (true) {
            try {
                Thread.sleep(10000);

                //StartGossipRound()

            } catch (Exception e) {
                System.out.println("Something went wrong with the thread sleeping.");
            }
        }
    }
}
