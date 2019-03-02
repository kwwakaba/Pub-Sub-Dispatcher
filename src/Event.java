package src;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class Event implements Serializable {
	
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

	/** This will probably be needed when we go to write the events to the DatagramPacket message.
	 * Since it is written to the socket as a stream of bytes. */
	// !!! UNTESTED !!!!
	public static byte[] toStream(Event event) {
		// Reference for stream of bytes
		byte[] stream = null;

		try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			 ObjectOutputStream outputStream = new ObjectOutputStream(byteArrayOutputStream);) {

			outputStream.writeObject(event);
			stream = byteArrayOutputStream.toByteArray();

		} catch (IOException e) {
			System.out.println("Something went wrong trying to write the Event object to an array of bytes.");
			e.printStackTrace();
		}
		return stream;
	}
	
}