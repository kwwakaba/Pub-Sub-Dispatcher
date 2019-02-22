package src.threads;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 *  Thread for listening to the socket.
 *
 *  Upon listening to the socket, if a message is received, fires off
 *  a thread for parsing so it can continue listening to the socket for more messages.
 */
public class SocketListenerThread extends Thread {

    public void run() {
        try {
            //Open the socket and listen for messsages.
            // https://systembash.com/a-simple-java-udp-server-and-udp-client/
            DatagramSocket serverSocket = new DatagramSocket(9876);
            byte[] receiveData = new byte[1024];

            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            serverSocket.receive(receivePacket);

            String sentence = new String(receivePacket.getData());
            System.out.println("RECEIVED: " + sentence);
            // End borrowed code.

            if (!sentence.isEmpty()) {
                MessageParserThread messageParserThread = new MessageParserThread(receivePacket);
                messageParserThread.start();
            }

        } catch (Exception e) {

        }

    }

}
