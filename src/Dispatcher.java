package src;
import src.messages.*;
import src.threads.NotificationThread;

import java.net.InetAddress;
import java.util.LinkedList;
import java.util.HashMap;
import java.util.concurrent.Semaphore;

public class Dispatcher {

	// MARK: - Instance Variable
	private static Semaphore mutex = new Semaphore(1);
	private static String identifier, ipAddress;
	private static int portNumber;

	private static LinkedList<Dispatcher> neighborTable; //This probably makes more sense to implement as a set.
	private static HashMap<String, LinkedList<Dispatcher>> subscriptionTable; //Should this be pattern -> List of distributors?

	/** Event cache needs to be protected by a mutex, since multiple threads will be reading/writing to the cache.*/
	private static LinkedList<Event> eventCache;
	
	// MARK: - Constructor
	
	public Dispatcher(String identifier, String ipAddress, int portNumber) {
		this.identifier = identifier;
		this.ipAddress = ipAddress;
		this.portNumber = portNumber;
		
		neighborTable = new LinkedList<>();
		subscriptionTable = new HashMap<>();
		eventCache = new LinkedList<>();
	}
	
	// MARK: - Instance Methods
	
	public static String getIdentifier() {
		return identifier;
	}
	
	public static String getIpAddress() {
		return ipAddress;
	}
	
	public static int getPortNumber() {
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

	    String ipAddress = null;
	    try {
	        ipAddress = InetAddress.getLocalHost().toString();
            System.out.println("IPAddress of host:" + ipAddress);
        } catch (Exception e) {
	        System.out.println("Something went wrong trying to get the IP address of the host.");
	        return;
        }

        //Starts the process that puts stuff into the Event Cache.
        NotificationThread notificationThread = new NotificationThread();
        notificationThread.start();

        System.out.println("Starting Dispatcher.");
	}







}