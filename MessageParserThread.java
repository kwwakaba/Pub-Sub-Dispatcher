package src.threads;

import java.net.DatagramPacket;

public class MessageParserThread extends Thread {

    /** The packet that was read from the socket. */
    DatagramPacket packet;

    /** Constructor
     *
     * @param packet the unparsed string form of the message received on the socket.
     */
    public MessageParserThread(DatagramPacket packet) {
        this.packet = packet;
    }

    public void run() {
        // Parse message

        // Call appropriate handler.
        // if (message instanceof EventResponseMessage)
        // else if (instanceof RequestMessage)
        //    handleRequestMessage()
        //    sendMessage()
        // else if (instance of GossipMessage)
        // else {
        //  log out message that we received something unrecognizable
        // }

    }
}
