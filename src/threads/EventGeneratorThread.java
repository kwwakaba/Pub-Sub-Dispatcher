package src.threads;

import src.Dispatcher;
import src.Event;

import java.util.ArrayList;

/**
 *  A thread that simulates receiving notifications of a certain event type
 *  by putting events in to the distributor's eventCache at random times.
 *
 */
public class EventGeneratorThread extends Thread {
	// constants (in milliseconds) for generating the random sleep time
	private static final int MIN_BOUND_SLEEP = 5000;
	private static final int MAX_BOUND_SLEEP = 10000;

	// constant for number of random patterns to generate
	private static final int NUM_PATTERNS = 5;

	// private static Semaphore mutex;

	private int randomTimeToSleep;

	// stores information about the Dispatcher that started this Thread to generate Events
	private Dispatcher dispatcher;

	// predefined set of possible patterns (this may be needed in other Threads/Classes as well!)
	ArrayList<String> possiblePatterns;

	// setup thread
	public void setup(Dispatcher dispatcher) {
		this.dispatcher = dispatcher;
		initializePossiblePatterns();
	}

	// initialize the patterns of the messages
	private void initializePossiblePatterns() {
		possiblePatterns = new ArrayList<>();
		for (int i = 0; i < NUM_PATTERNS; i++) {
			possiblePatterns.add("Pattern " + i);
		}
	}

    public void run() {

        int counter = 1;
        while (true) {

            try {
                // generates a random sleep time between MIN_BOUND_SLEEP to MAX_BOUND_SLEEP
                randomTimeToSleep = (int)(Math.random() * (MAX_BOUND_SLEEP - MIN_BOUND_SLEEP) + MIN_BOUND_SLEEP);
                Thread.sleep(randomTimeToSleep);

                System.out.println("Event Generation Thread waking up. ");

                // generate a random Event to add to Dispatcher's cache
                String identifier = "Event " + counter;
                String description = "Dispatcher '" + identifier + "' with IP '" + dispatcher.getIpAddress().getHostAddress() + "' and port " + dispatcher.getPortNumber();
                // chooses a random pattern from the list of possible patterns
                String pattern = possiblePatterns.get((int)(Math.random() * possiblePatterns.size()));

                Event newEvent = new Event(identifier, description, pattern);
                System.out.println("Adding event to cache with description: " + description);
                dispatcher.addEventToCache(newEvent);

                counter++;
            } catch (Exception e) {
                System.out.println("Something went wrong with the thread sleeping.");
            }
        }
    }

}
