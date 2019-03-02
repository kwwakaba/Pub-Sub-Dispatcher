package src;
import src.threads.EventGeneratorThread;
import src.threads.StartGossipThread;

import java.net.InetAddress;
import java.util.LinkedList;
import java.util.HashMap;
import java.util.concurrent.Semaphore;

public class Dispatcher {

	// MARK: - Instance Variable
	private static Semaphore mutex = new Semaphore(1);
	private String identifier;
	private InetAddress ipAddress;
	private int portNumber;

	private static LinkedList<Dispatcher> neighborTable; //This probably makes more sense to implement as a set.
	private static HashMap<String, LinkedList<Dispatcher>> subscriptionTable; //Should this be pattern -> List of distributors?

	/** Event cache needs to be protected by a mutex, since multiple threads will be reading/writing to the cache.*/
	private static LinkedList<Event> eventCache;
	
	// MARK: - Constructor
	
	public Dispatcher(String identifier, InetAddress ipAddress, int portNumber) {
		this.identifier = identifier;
		this.ipAddress = ipAddress;
		this.portNumber = portNumber;
		
		neighborTable = new LinkedList<>();
		subscriptionTable = new HashMap<>();
		eventCache = new LinkedList<>();
	}
	
	// MARK: - Instance Methods
	
	public  String getIdentifier() {
		return identifier;
	}
	
	public  InetAddress getIpAddress() {
		return ipAddress;
	}
	
	public int getPortNumber() {
		return portNumber;
	}
	
	public static void addNeighbor(Dispatcher d) {
		neighborTable.add(d);
	}
	
	public static LinkedList<Dispatcher> getNeighbors() {
		return neighborTable;
	}
	
	public static void addSubscription(String pattern, LinkedList<Dispatcher> dispatcherList) {
		subscriptionTable.put(pattern, dispatcherList);
	}
	
	public static void addDispatcherToListWithPattern(Dispatcher d, String pattern) {
		subscriptionTable.get(pattern).add(d);
	}
	
	public static LinkedList<Dispatcher> getDispatcherListForPattern(String pattern) {
		return subscriptionTable.get(pattern);
	}


	public static void addEventToCache(Event e) throws InterruptedException {
		if(mutex.tryAcquire())
			eventCache.add(e);
		mutex.release();
	}
	
	public static LinkedList<Event> getEventCache() {
		return eventCache;
	}


	public static void main(String[] args)
	{

	    //Get the IPAddresss of our local machine.
	    InetAddress ipAddress;
	    try {
	        ipAddress = InetAddress.getLocalHost();
            System.out.println("IPAddress of host: " + ipAddress);
        } catch (Exception e) {
	        System.out.println("Something went wrong trying to get the IP address of the host.");
	        return;
        }

        // Creates the one instance of the dispatcher.
        Dispatcher dispatcher = new Dispatcher("IDENTIFIER", ipAddress, 9234);

        // Starts the thread that wakes up randomly to push gossip messages across the network.
        StartGossipThread startGossipThread = new StartGossipThread();
	    startGossipThread.start();

        // Starts the thread that puts stuff into the Event Cache. Should be started after
        // Dispatcher initialization since this tries to reference the EventCache.
        EventGeneratorThread eventGeneratorThread = new EventGeneratorThread();
        eventGeneratorThread.setup(dispatcher.getIdentifier(), dispatcher.getIpAddress().toString(), dispatcher.getPortNumber());
        eventGeneratorThread.start();

        System.out.println("Starting Dispatcher.");
	}

}