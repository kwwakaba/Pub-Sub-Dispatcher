package src;
import src.threads.EventGeneratorThread;
import src.threads.StartGossipThread;

import java.io.File;
import java.net.InetAddress;
import java.util.LinkedList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class Dispatcher {

	// MARK: - Instance Variable
	private static Semaphore mutex = new Semaphore(1);
	private String identifier;
	private InetAddress ipAddress;
	private int portNumber;

	private static LinkedList<Dispatcher> neighborTable;
	static {
	    try {

            neighborTable.add(new Dispatcher("1", InetAddress.getByName("127.0.0.1"), 9576));
            neighborTable.add(new Dispatcher("2", InetAddress.getByName("127.0.0.1"), 9577));
            neighborTable.add(new Dispatcher("3", InetAddress.getByName("127.0.0.1"), 9578));

        } catch (Exception e) {

        }

    }
	private static HashMap<String, LinkedList<Dispatcher>> subscriptionTable; //Should this be pattern -> List of distributors?

	/** Event cache needs to be protected by a mutex, since multiple threads will be reading/writing to the cache.*/
	private static LinkedList<Event> eventCache;
	
	// MARK: - Constructor
	
	public Dispatcher(String identifier, InetAddress ipAddress, int portNumber) {
		this.identifier = identifier;
		this.ipAddress = ipAddress;
		this.portNumber = portNumber;

		subscriptionTable = new HashMap<>();
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
	

	public void addNeighbor(Dispatcher d) {
        neighborTable.push(d);
    }
    
	public LinkedList<Dispatcher> getNeighbors() {
		return neighborTable;
	}

	public static HashMap<String, LinkedList<Dispatcher>> getSubscriptionTable() {
		return subscriptionTable;
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

		if(mutex.tryAcquire(2000, TimeUnit.MILLISECONDS)) {
            eventCache.add(e);
            mutex.release();
        }
	}
	
	public static LinkedList<Event> getEventCache() {
		return eventCache;
	}

/*    *//**
     * Reads in the information about the other dispatchers in the network to create the subscription
     * table.
     *//*
	public void createSubscriptionTable() {

	    try {
            File file =
                    new File(System.getProperty("user.dir") + "/neighborsConfig.txt");
            System.out.println(file.getAbsolutePath());
            Scanner sc = new Scanner(file);

            List<Dispatcher> dispatcherList = new LinkedList<Dispatcher>();
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                System.out.println(line);
                String parsed[] = line.split(";");

                InetAddress inetAddress = InetAddress.getByName(parsed[1]);
                Dispatcher dispatcher = new Dispatcher(parsed[0], inetAddress, Integer.parseInt(parsed[2]));
                if (!inetAddress.getHostAddress().equals(this.getIpAddress().getHostAddress())) {
                    dispatcherList.add(dispatcher);
                }
            }

        } catch (Exception e) {
	        System.out.println("Something went wrong trying to create the subscription table. \n"
                    + e.getStackTrace());
        }

    }*/


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
        Dispatcher dispatcher = new Dispatcher("IDENTIFIER", ipAddress, 9573);
	    System.out.println("Dispatcher neighbor table size: " + dispatcher.getNeighbors().size());

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