package src;


import java.io.Serializable;

/** This should be in the messages folder but it was causing red lines sooooo here we are... */

public abstract class Message implements Serializable {
	
	// Message description.
	private String description;

	/** Field denoting the source of the message. */
	private String sourceNodeId;
	
	// Constructor
	public Message(String description) {
		this.description = description;
	}
	
	// Accessor for the description.
	public String getDescription() {
		return description;
	}

    // Source distributor ID of the message.
	public String getSourceNodeId() { return sourceNodeId; }

	// Setter.
	public void setSourceNodeId(String sourceId){
		this.sourceNodeId = sourceId;
	}

	@Override
	public String toString() {
		return String.format("Message: description = " + description + " sourceNodeId = " + sourceNodeId);
	}

}