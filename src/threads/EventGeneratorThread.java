package src.threads;

/**
 *  A thread that simulates receiving notifications of a certain event type
 *  by putting events in to the cache at random times.
 *
 */
public class EventGeneratorThread extends Thread {
	private int randomTimeToSleep;

    public void run() {
        // TODO
        // Thread.sleep() some random amount of time that varies
        // Get instance of Dispatcher
        // Get Mutex
        // Put stuff in the cache.

        while (true) {
            try {
                // generates a random sleep time between 5000 to 15000 (5 to 15 seconds)
                randomTimeToSleep = (int)(Math.random() * 10000 + 5000);
                Thread.sleep(randomTimeToSleep);

                // get instance of static Dispatcher
            } catch (Exception e) {
                System.out.println("Something went wrong with the thread sleeping.");
            }
        }
    }

}
