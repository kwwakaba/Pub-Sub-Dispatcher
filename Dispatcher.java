package src;

import java.util.LinkedList;
import java.util.HashMap;

public class Dispatcher {
	
	// MARK: - Instance Variables
	
	private String identifier, ipAddress;
	private int portNumber;
	private LinkedList<Dispatcher> neighborTable;
	private HashMap<String, LinkedList<Dispatcher>> subscriptionTable; //Should this be pattern -> List of distributors?
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
	
}