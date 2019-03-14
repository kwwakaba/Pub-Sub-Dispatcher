package src;

import java.io.Serializable;

/**
 * Events that are propagated throughout the gossip network.
 */
public class Event implements Serializable {
	
	// Identifier of event
	private String identifier;

	// Description of the event.
	private String description;

	// Pattern of the event.
	private String pattern;

	// Constructor
	public Event(String identifier, String description, String pattern) {
		this.identifier = identifier;
		this.description = description;
		this.pattern = pattern;
	}
	
	// Returns identifier of event
	public String getIdentifier() {
		return identifier;
	}

	// Returns pattern of event
	public String getPattern() {
		return pattern;
	}

}