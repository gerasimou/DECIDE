package network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import org.apache.log4j.Logger;

public class DatagramSocketReceiver extends ReceiverDECIDE{
	
	
	
	/** server port */
	private int serverPort;
	
	/** Multicast socket */
	private DatagramSocket receiverSocket;
	
	/** Buffer containing the data received */
	byte[] buf;
	private final int BUFFER_SIZE = 256;

	/** Logging system events*/
    final static Logger logger = Logger.getLogger(DatagramSocketReceiver.class);
	
	/**
	 * Class constructor: create a new multicast receiver
	 * @param serverAddress
	 * @param port
	 */
	public DatagramSocketReceiver (String serverAddress, int port) {
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
	        receiverSocket = new DatagramSocket(serverPort);
	        
	                 
        }
        catch (IOException e) {
			logger.error(e.getStackTrace().toString());
		}
	}
	

	/**
	 * Log system events to console or file
	 * @param String log
	 */
	private void logEvents(String parameter){

		if(logger.isDebugEnabled())
			logger.debug(parameter);
		else
			logger.info(parameter);	
	}
	
    
	@Override
	public void run() {
		try{
			String message;
			String tempHashCode = "";
			boolean result = true;
			DatagramPacket msgPacket = new DatagramPacket(buf, buf.length);
	        while (true) {
	            // Receive the information and print it
	            
	            receiverSocket.receive(msgPacket);
	            
	            
	          /*  byte data[] = msgPacket.getData();
	            ByteArrayInputStream bais = new ByteArrayInputStream(data);
	            ObjectInputStream     ois  = new ObjectInputStream(bais);*/
	            try {
	            	
	            	 //new
		            message = new String(msgPacket.getData()); 
					if(logger.isDebugEnabled())
						logger.debug("Received from:"+serverAddress+", " + message +",[Status: "+ atomicPeerStatus.get()+"]");
					
					// just clear configuartion map if peer is absent
					if(atomicPeerStatus.get()==PeerStatus.MISSING) {
						atomicPeerStatus.set(PeerStatus.NEW_JOIN);
					}
					
					this.setTimeStamp(System.currentTimeMillis());
					
					this.localControl.receive(serverAddress, message);				
				} 
	            catch (Exception e) {
					e.printStackTrace();
				}
	
	            
	            
	            //System.out.println("Received from ["+ serverAddress +":"+ receiverSocket.getLocalPort() + "] <-- " + msg);
	            
	        }		
	    }
	    catch (IOException e) {
			e.printStackTrace();
		}
	}

}