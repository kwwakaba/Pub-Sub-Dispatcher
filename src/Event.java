package src;

import java.io.Serializable;

public class Event implements Serializable {
	
	// MARK: - Instance Variables
	
	private String identifier, description, pattern;
	
	// MARK: - Constructor
	
	public Event(String identifier, String description, String pattern) {
		this.identifier = identifier;
		this.description = description;
		this.pattern = pattern;
	}
	
	// MARK: - Instance Methods
	
	public String getIdentifier() {
		return identifier;
	}
	
	public String getDescription() {
		return description;
	}

	public String getPattern() {
		return pattern;
	}

}