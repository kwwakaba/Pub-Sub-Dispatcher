package src.messages;


/** This should be in the messages folder but it was causing red lines sooooo here we are... */

public abstract class Message {
	
	// MARK: - Instance Variable
	
	private String description;

	/** Field denoting the source of the message. */
	private String sourceNodeId;
	
	// MARK: - Constructor
	
	public Message(String description) {
		this.description = description;
	}
	
	// MARK: - Instance Method
	
	public String getDescription() {
		return description;
	}


	public String getSourceNodeId() { return sourceNodeId; }

}