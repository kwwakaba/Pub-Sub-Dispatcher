package src.threads;

import src.Dispatcher;
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

    Dispatcher dispatcher;

    public void setup(Dispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    public void run() {
        try {
            //Open the socket and listen for messsages.
            // https://systembash.com/a-simple-java-udp-server-and-udp-client/
            System.out.println("Socket Listener Thread started. \n");
            System.out.println("SocketListenerThread: Listening to socket at port " + dispatcher.getPortNumber()
                + "for dispatcher " + dispatcher.getIdentifier());

            DatagramSocket serverSocket = new DatagramSocket(dispatcher.getPortNumber());

            while (true) {
                byte[] receiveData = new byte[1024];
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                serverSocket.receive(receivePacket);

                System.out.println("A message has been received on dispatcher " + dispatcher.getIdentifier());

                //Try to parse the data received into a Message
                byte[] data = receivePacket.getData();
                ByteArrayInputStream in = new ByteArrayInputStream(data);
                ObjectInputStream is = new ObjectInputStream(in);
                Message message = null;
                try {
                    message = (Message)is.readObject();

                } catch (ClassNotFoundException e) {
                    System.out.println("Something went wrong in the socket listener thread dispatcherID: "
                    + dispatcher.getIdentifier() + "\n");
                    e.printStackTrace();
                }

                if (message != null) {
                    MessageHandlerThread messageParserThread = new MessageHandlerThread(receivePacket, message, dispatcher);
                    messageParserThread.start();
                } else { System.out.println("MessageHandlerThread not starting - MESSAGE IS NULL"); }
            }

        } catch (Exception e) {
            System.out.println("SocketListenerThread: Something went wrong. " + e.getStackTrace());
        }

    }

}
