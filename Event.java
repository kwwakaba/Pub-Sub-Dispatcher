public class Event {
	
	// MARK: - Instance Variables
	
	private String identifier, description;
	
	// MARK: - Constructor
	
	public Event(String identifier, String description) {
		this.identifier = identifier;
		this.description = description;
	}
	
	// MARK: - Instance Methods
	
	public String getIdentifier() {
		return identifier;
	}
	
	public String getDescription() {
		return description;
	}
	
}