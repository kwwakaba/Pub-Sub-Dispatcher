package src.messages;
import src.Digest;
import src.Message;

import java.io.Serializable;

/**
 * This class is the Message object for GossipMessage.
 *
 * A gossip message contains a digest, which contains the event IDs found by the source dispatcher
 * matching the pattern of the gossip message.
 */
public class GossipMessage extends Message implements Serializable {
	
	private String dispatcherIdentifier, pattern;
	private Digest digest;


	/**
	 * Constructor
	 * @param description information about the gossip message.
	 * @param dispatcherIdentifier ID of the dispatcher sending the gossip message.
	 * @param pattern Pattern of the events in the digest.
	 * @param digest Events found in the dispatcher's event cache that it's trying to propagate in the gossip message.
	 */
	public GossipMessage(String description, String dispatcherIdentifier, String pattern, Digest digest) {
		super(description);
		
		this.dispatcherIdentifier = dispatcherIdentifier;
		this.pattern = pattern;
		this.digest = digest;
	}

	/**
	 * Getter for pattern.
	 * @return pattern of events in digest.
	 */
	public String getPattern() {
		return pattern;
	}

	/**
	 * Getter for Digest of message.
	 * @return returns digest of the message.
	 */
	public Digest getDigest() {
		return digest;
	}

	@Override
	public String toString() {
		return String.format("GossipMessage: description = " + getDescription() + " dispatcherIdentifier = " + dispatcherIdentifier + " pattern: " + pattern + " digest: " + digest);
	}
	
}
