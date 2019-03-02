package src.threads;

/**
 *  A thread that simulates receiving notifications of a certain event type
 *  by putting events in to the cache at random times.
 *
 */
public class EventGeneratorThread extends Thread {
	// constants (in milliseconds) for generating the random sleep time
	private static final MIN_BOUND_SLEEP = 15000;
	private static final MAX_BOUND_SLEEP = 25000;

	private static Semaphore mutex;

	private int randomTimeToSleep;

    public void run() {
        // TODO
        // Thread.sleep() some random amount of time that varies
        // Get instance of Dispatcher
        // Get Mutex
        // Put stuff in the cache.

        mutex = new Semaphore(1);

        while (true) {
            try {
                // generates a random sleep time between MIN_BOUND_SLEEP to MAX_BOUND_SLEEP
                randomTimeToSleep = (int)(Math.random() * (MAX_BOUND_SLEEP - MIN_BOUND_SLEEP) + MIN_BOUND_SLEEP);
                Thread.sleep(randomTimeToSleep);

                // I don't think we need the following line because addEvent() in Dispatcher already has this...
                /*if (mutex.tryAcquire()) {

                }*/

                // generate a random Event to add to Dispatcher's cache
                
            } catch (Exception e) {
                System.out.println("Something went wrong with the thread sleeping.");
            }
        }
    }

}
