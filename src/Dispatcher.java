package src;

import src.threads.EventGeneratorThread;
import src.threads.SocketListenerThread;
import src.threads.StartGossipThread;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.*;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class Dispatcher {

	// MARK: - Instance Variable
	private static Semaphore mutex = new Semaphore(1);

	private String identifier;
	private InetAddress ipAddress;
	private int portNumber;

	private DatagramSocket sendSocket;

	private static LinkedList<Dispatcher> neighborTable = new LinkedList<>();
	static {
	    try {

            neighborTable.add(new Dispatcher("1", InetAddress.getByName("129.210.16.138"), 9576));
            neighborTable.add(new Dispatcher("2", InetAddress.getByName("129.210.16.143"), 9577));
            neighborTable.add(new Dispatcher("3", InetAddress.getByName("129.210.16.140"), 9578));

        } catch (Exception e) {
            System.out.println("Something went wrong creating the neighbor table");
        }

    }

    //Key is Pattern ID.
    //Value is Set of Dispatcher IDs subscribed to that pattern.
	private static HashMap<String, Set<String>> subscriptionTable = new HashMap<>();
	static {
        subscriptionTable.put("Pattern 0", new HashSet(Arrays.asList("1", "2")));
        subscriptionTable.put("Pattern 1", new HashSet(Arrays.asList("1", "3")));
        subscriptionTable.put("Pattern 2", new HashSet(Arrays.asList("2", "3")));
        subscriptionTable.put("Pattern 3", new HashSet(Arrays.asList("1", "2")));
        subscriptionTable.put("Pattern 4", new HashSet(Arrays.asList("3", "2")));
        subscriptionTable.put("Pattern 5", new HashSet(Arrays.asList("1", "2", "3")));
        subscriptionTable.put("Pattern 6", new HashSet());
        subscriptionTable.put("Pattern 7", new HashSet(Arrays.asList("1", "2")));
        subscriptionTable.put("Pattern 8", new HashSet(Arrays.asList("1", "3")));
        subscriptionTable.put("Pattern 9", new HashSet(Arrays.asList("3", "2")));
    }

	/** Event cache needs to be protected by a mutex, since multiple threads will be reading/writing to the cache.*/
	private LinkedList<Event> eventCache;
	
	// MARK: - Constructor
	
	public Dispatcher(String identifier, InetAddress ipAddress, int portNumber) {
		this.identifier = identifier;
		this.ipAddress = ipAddress;
		this.portNumber = portNumber;

        int identifyPort = new Random().nextInt(100);
        try {
            this.sendSocket = new DatagramSocket(9579 + identifyPort);
            System.out.println("All messages sent by Dispatcher id: " + identifier + " out on port " + identifyPort);
        } catch (Exception e) {
		    System.out.println("Something went wrong trying to create the port for dispatcher id: " + identifier);
		    e.printStackTrace();
        }

		eventCache = new LinkedList<>();
	}

	public  String getIdentifier() {
		return identifier;
	}

	public  InetAddress getIpAddress() {
		return ipAddress;
	}
	
	public int getPortNumber() {
		return portNumber;
	}

	public static LinkedList<Dispatcher> getNeighbors() {
		return neighborTable;
	}

	public static HashMap<String, Set<String>> getSubscriptionTable() {
		return subscriptionTable;
	}
	
	public static Set<String> getDispatcherListForPattern(String pattern) {
		return subscriptionTable.get(pattern);
	}

	public void addEventToCache(Event e) throws InterruptedException {
		if(mutex.tryAcquire(2000, TimeUnit.MILLISECONDS)) {
            eventCache.add(e);
            mutex.release();
        }
	}

	public LinkedList<Event> getEventCache() throws InterruptedException{

		LinkedList<Event> returnList = null;
		if(mutex.tryAcquire(2000, TimeUnit.MILLISECONDS)) {
			returnList = eventCache;
			mutex.release();
		}

		return returnList;
	}

    public DatagramSocket getSendSocket() throws InterruptedException {
            return sendSocket;
    }

    public static void main(String[] args)
	{

	    //Get the IPAddresss of our local machine.
	    InetAddress ipAddress = null;
	    try {
	        ipAddress = InetAddress.getLocalHost();
            System.out.println("IPAddress of host: " + ipAddress.getLocalHost());
        } catch (Exception e) {
	        System.out.println("Something went wrong trying to get the IP address of the host.");
	        return;
        }

        // Creates the one instance of the dispatcher.
        Dispatcher dispatcher = new Dispatcher("IDENTIFIER", ipAddress, 9578);
	    System.out.println("Dispatcher neighbor table size: " + dispatcher.getNeighbors().size());

        // Starts the thread that wakes up randomly to push gossip messages across the network.
        StartGossipThread startGossipThread = new StartGossipThread();
        startGossipThread.setup(dispatcher);
	    startGossipThread.start();

        // Starts the thread that puts stuff into the Event Cache. Should be started after
        // Dispatcher initialization since this tries to reference the EventCache.
        EventGeneratorThread eventGeneratorThread = new EventGeneratorThread();
        eventGeneratorThread.setup(dispatcher);
        eventGeneratorThread.start();

        SocketListenerThread socketListenerThread = new SocketListenerThread();
        socketListenerThread.setup(dispatcher);

        System.out.println("Starting Dispatcher.");
	}

}
