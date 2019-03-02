package src.threads;

import src.Message;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
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
            int portNumber = 9876;
            System.out.println("Socket Listener Thread started. \n");
            System.out.println("SocketListenerThread: Listening to socket at port " + portNumber);

            DatagramSocket serverSocket = new DatagramSocket(portNumber);

            while (true) {
                byte[] receiveData = new byte[1024];
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                serverSocket.receive(receivePacket);

                //Try to parse the data received into a Message
                byte[] data = receivePacket.getData();
                ByteArrayInputStream in = new ByteArrayInputStream(data);
                ObjectInputStream is = new ObjectInputStream(in);
                Message message = null;
                try {
                    message = (Message) is.readObject();

                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

                if (message != null) {
                    MessageHandlerThread messageParserThread = new MessageHandlerThread(receivePacket, message);
                    messageParserThread.start();
                }
            }

        } catch (Exception e) {
            System.out.println("SocketListenerThread: Something went wrong. " + e.getStackTrace());
        }

    }

}
