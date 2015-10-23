package network;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

import decide.localAnalysis.CapabilitySummary;

public class MulticastReceiver extends ServerDECIDE{
	
	/** server address*/
	private String serverAddress;
	
	/** server port */
	private int serverPort;
	
	/** Multicast socket */
	private MulticastSocket receiverSocket;
	
	/** Buffer containing the data received */
	byte[] buf;
	private final int BUFFER_SIZE = 1024;

	
	/**
	 * Class constructor: create a new multicast receiver
	 * @param serverAddress
	 * @param port
	 */
	public MulticastReceiver (String serverAddress, int port) {
		this.serverAddress	= serverAddress;
		this.serverPort		= port;

        try {
    		// Get the address that we are going to connect to.
			InetAddress address = InetAddress.getByName(this.serverAddress);
			 
			// Create a buffer of bytes, which will be used to store
	        // the incoming bytes containing the information from the server.
	        // Since the message is small here, 256 bytes should be enough.
	        buf = new byte[BUFFER_SIZE];		
	    
	        // Create a new Multicast socket (that will allow other sockets/programs to join it as well.
	        receiverSocket = new MulticastSocket(serverPort);
	        
	        //Joint the Multicast group.
            receiverSocket.joinGroup(address);            
        }
        catch (IOException e) {
			e.printStackTrace();
		}
	}

	

	@Override
	public void run() {
		try{
	        while (true) {
	            // Receive the information and print it
	            DatagramPacket msgPacket = new DatagramPacket(buf, buf.length);
	            receiverSocket.receive(msgPacket);
	            
	            byte data[] = msgPacket.getData();
	            ByteArrayInputStream bais = new ByteArrayInputStream(data);
	            ObjectInputStream     ois  = new ObjectInputStream(bais);
	            try {
					CapabilitySummary cs = (CapabilitySummary)ois.readObject();
					System.out.println(cs.toString());					
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
	
//	            String msg = new String(buf, 0, buf.length);
	            
//	            System.out.println("Received from ["+ serverAddress +":"+ receiverSocket.getLocalPort() + "] <-- " + msg);
//	            claReceipt.receive(msg);
	        }		
	    }
	    catch (IOException e) {
			e.printStackTrace();
		}
	}

}
