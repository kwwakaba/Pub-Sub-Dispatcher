package src;

import src.threads.SocketListenerThread;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.LinkedList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class Dispatcher {
	
	// MARK: - Instance Variables
	
	private String identifier, ipAddress;
	private int portNumber;
	private LinkedList<Dispatcher> neighborTable;
	private HashMap<String, LinkedList<Dispatcher>> subscriptionTable; //Should this be pattern -> List of distributors?

	/** Event cache needs to be protected by a mutex, since multiple threads will be reading/writing to the cache.*/
	private LinkedList<Event> eventCache;
	
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
	
	public String getIdentifier() {
		return identifier;
	}
	
	public String getIpAddress() {
		return ipAddress;
	}
	
	public int getPortNumber() {
		return portNumber;
	}
	
	public void addNeighbor(Dispatcher d) {
		neighborTable.add(d);
	}
	
	public LinkedList<Dispatcher> getNeighbors() {
		return neighborTable;
	}
	
	public void addSubscription(String pattern, LinkedList<Dispatcher> dispatcherList) {
		subscriptionTable.put(pattern, dispatcherList);
	}
	
	public void addDispatcherToListWithPattern(Dispatcher d, String pattern) {
		subscriptionTable.get(pattern).add(d);
	}
	
	public LinkedList<Dispatcher> getDispatcherListForPattern(String pattern) {
		return subscriptionTable.get(pattern);
	}
	
	public void addEventToCache(Event e) {
		eventCache.add(e);
	}
	
	public LinkedList<Event> getEventCache() {
		return eventCache;
	}

	public static void main(String [ ] args) throws IOException, InterruptedException {
		SocketListenerThread listenerThread = new SocketListenerThread();
		listenerThread.start();

		// Client example
		DatagramSocket ds = new DatagramSocket();
		InetAddress ip = InetAddress.getLocalHost();

		byte buff[] = null;
		buff = "Hello World!".getBytes();
		DatagramPacket dp0 = new DatagramPacket(buff, buff.length, ip, 8080);
		ds.send(dp0);

		buff = "bye".getBytes();
		DatagramPacket dp1 = new DatagramPacket(buff, buff.length, ip, 8080);
		ds.send(dp1);

		ds.close();
	}
	
}