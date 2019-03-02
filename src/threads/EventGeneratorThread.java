package src.threads;

import java.lang.Math;
import java.util.ArrayList;

/**
 *  A thread that simulates receiving notifications of a certain event type
 *  by putting events in to the cache at random times.
 *
 */
public class EventGeneratorThread extends Thread {
	// constants (in milliseconds) for generating the random sleep time
	private static final MIN_BOUND_SLEEP = 15000;
	private static final MAX_BOUND_SLEEP = 25000;

	// constant for number of random patterns to generate
	private static final NUM_PATTERNS = 10;

	// private static Semaphore mutex;

	private int randomTimeToSleep;

	// stores information about the Dispatcher that started this Thread to generate Events
	private String identifier, ipAddress;
	private int portNumber;

	// predefined set of possible patterns (this may be needed in other Threads/Classes as well!)
	ArrayList<String> possiblePatterns;

	public void setup(String identifier, String ipAddress, int portNumber) {
		this.identifier = identifier;
		this.ipAddress = ipAddress;
		this.portNumber = portNumber;

		initializePossiblePatterns();
	}

	private void initializePossiblePatterns() {
		possiblePatterns = new ArrayList<>();
		for (int i = 0; i < NUM_PATTERNS; i++) {
			possiblePatterns.add("Pattern " + i);
		}
	}

    public void run() {
        // mutex = new Semaphore(1);

        while (true) {
            int counter = 1;

            try {
                // generates a random sleep time between MIN_BOUND_SLEEP to MAX_BOUND_SLEEP
                randomTimeToSleep = (int)(Math.random() * (MAX_BOUND_SLEEP - MIN_BOUND_SLEEP) + MIN_BOUND_SLEEP);
                Thread.sleep(randomTimeToSleep);

                // generate a random Event to add to Dispatcher's cache
                String identifier = "Event " + counter;
                String description = "Dispatcher '" + identifier + "' with IP '" + ipAddress + "' and port " + portNumber;
                // chooses a random pattern from the list of possible patterns
                String pattern = possiblePatterns.get((int)(Math.random() * possiblePatterns.size()));
                Event newEvent = new Event(identifier, description, pattern);

                // I don't think we need the following line because addEvent() in Dispatcher already has this...
                /*if (mutex.tryAcquire()) {

                }*/
                Dispatcher.addEventToCache(newEvent);

                counter++;
            } catch (Exception e) {
                System.out.println("Something went wrong with the thread sleeping.");
            }
        }
    }

}
