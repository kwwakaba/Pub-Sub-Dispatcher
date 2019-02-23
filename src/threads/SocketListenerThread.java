package src.threads;

import java.io.IOException;
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
//        try {
//            //Open the socket and listen for messsages.
//            // https://systembash.com/a-simple-java-udp-server-and-udp-client/
//            DatagramSocket serverSocket = new DatagramSocket(8080);
//            byte[] receiveData = new byte[1024];
//
//            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
//            serverSocket.receive(receivePacket);
//
//            String sentence = new String(receivePacket.getData());
//            System.out.println("RECEIVED: " + sentence);
//            // End borrowed code.
//
//            if (!sentence.isEmpty()) {
//                MessageParserThread messageParserThread = new MessageParserThread(receivePacket);
//                messageParserThread.start();
//            }
//
//        } catch (Exception e) {
//
//        }

        // Source: https://www.geeksforgeeks.org/working-udp-datagramsockets-java/
        try {
            DatagramSocket serverSocket = new DatagramSocket(8080);
            DatagramPacket receivedDatagram;

            byte[] buffer = new byte[65535];

            while (true) {
                // Create datagram
                receivedDatagram = new DatagramPacket(buffer, buffer.length);
                serverSocket.receive(receivedDatagram);

                // TODO: Deserialize datagram in child thread
                System.out.println("Client:" + data(buffer));

                // Exit when received "bye"
                if (data(buffer).toString().equals("bye")) {
                    System.out.println("Client sent bye.....EXITING");
                    break;
                }

                // Clear buffer
                buffer = new byte[65535];
            }
        } catch (IOException e) {
            throw new Error(e.getMessage());
        }
    }

    // Utility method to deserialize a string.
    private static StringBuilder data(byte[] a) {
        if (a == null)
            return null;

        StringBuilder ret = new StringBuilder();

        int i = 0;
        while (a[i] != 0) {
            ret.append((char) a[i]);
            i++;
        }

        return ret;
    }

}
