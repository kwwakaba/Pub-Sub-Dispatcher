package src.messages;
import src.Digest;
import src.Message;

import java.io.Serializable;

public class GossipMessage extends Message implements Serializable {
	
	// MARK: - Instance Variables
	
	private String dispatcherIdentifier, pattern;
	private Digest digest;
	
	// MARK: - Constructor
	
	public GossipMessage(String description, String dispatcherIdentifier, String pattern, Digest digest) {
		super(description);
		
		this.dispatcherIdentifier = dispatcherIdentifier;
		this.pattern = pattern;
		this.digest = digest;
	}
	
	// MARK: - Instance Methods
	
	public String getDispatcherIdentifier() {
		return dispatcherIdentifier;
	}
	
	public String getPattern() {
		return pattern;
	}
	
	public Digest getDigest() {
		return digest;
	}
	
	@Override
	public String toString() {
		return String.format("Message: description = " + getDescription() + " dispatcherIdentifier = " + dispatcherIdentifier + " pattern: " + pattern + " digest: " + digest);
	}
	
}
