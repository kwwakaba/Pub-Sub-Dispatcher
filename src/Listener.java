package src;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class Listener implements Runnable {
    @Override
    public void run() {
        try {
            DatagramSocket socket = new DatagramSocket(8080);
            DatagramPacket receivedDatagram;

            byte[] buffer = new byte[65535];

            while (true) {
                // Create datagram
                receivedDatagram = new DatagramPacket(buffer, buffer.length);
                socket.receive(receivedDatagram);

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
