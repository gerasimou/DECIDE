package network;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.Arrays;
import java.util.Objects;
import org.apache.log4j.Logger;
import decide.capabilitySummary.CapabilitySummary;
import decide.receipt.CLAReceipt;
import decide.receipt.CLAReceiptNew;


public class MulticastReceiverNew extends ReceiverDECIDENew{		
	/** server port */
	private int serverPort;
	
	/** Multicast socket */
	private MulticastSocket receiverSocket;
	
	/** Buffer containing the data received */
	byte[] buf;
	private final int BUFFER_SIZE = 20000;

	/** Logging system events*/
    final static Logger logger = Logger.getLogger(MulticastReceiverNew.class);
	
	/**
	 * Class constructor: create a new multicast receiver
	 * @param serverAddress
	 * @param port
	 */
	public MulticastReceiverNew (String serverAddress, int port) {
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
	

	/**
	 * Log system events to console or file
	 * @param String log
	 */
	private void logEvents(String parameter){

		if(logger.isDebugEnabled())
			logger.debug(parameter);
		else
			logger.info(parameter);

		//logger.error("This is error : " + parameter);
	}

	
	@Override
	public void run() {
		try{
			DatagramPacket msgPacket = new DatagramPacket(buf, buf.length);
	        
			while (true) {
	        		// Receive the information and print it
	            receiverSocket.receive(msgPacket);
	            
	            byte data[] 					= msgPacket.getData();
	            ByteArrayInputStream bais 	= new ByteArrayInputStream(data);
	            ObjectInputStream     ois  	= new ObjectInputStream(bais);
	            try {
	            		Object [] csArray = ((Object[])ois.readObject());
	            		processReceivedData(csArray);
				} 
	            catch (ClassNotFoundException e) {
					e.printStackTrace();
				}	            
	        }		
	    }
	    catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	private void processReceivedData(Object[] csArray) {
		int tempHashCode = 0;
		int hashCode = 0;
		boolean exists = true;

	    	// find the array hashcode to compare with previous received message
	    	hashCode = Objects.hash(csArray);//Arrays.hashCode(csArray);
			
	    	//System.out.println("Received from:"+serverAddress+"\t " + cs.toString()+"Peer Status is"+this.getAtomicPeerStatus().get());
		if(logger.isDebugEnabled())
			logger.debug("Received from:"+serverAddress+", " +Arrays.toString(csArray)+",[Status: "+atomicPeerStatus.get()+"]");
		// reset boolean flag
		exists = false;
		/* reset configuartion map if peer had been Missing */
		if (atomicPeerStatus.get()==PeerStatus.MISSING) { //&& capabilitySummaryCollection.concurrentCapabilitySummaryMap.contains(serverAddress) )		
			//capabilitySummaryCollection.concurrentCapabilitySummaryMap.remove(serverAddress);
			tempHashCode = 0;
		}
		
		//id = Knowledge.getPeerID(serverAddress);
		if ( (networkUser instanceof CLAReceiptNew) && ( ((CLAReceiptNew)networkUser).isKnownReceiver(serverAddress)) ) {
			if (hashCode == tempHashCode)
				exists = true;
		}
	    					
		if (!exists) {
			tempHashCode = hashCode;
			// if key is not there then either status has changed or a new peer has joined
			switch (atomicPeerStatus.get()) {
			    case ALIVE:{
				    	// its a minor change, peer has recalculated lca phase
				    	// alter concurrent CS map
//				    	capabilitySummaryCollection.addCapabilitySummary(serverAddress, (CapabilitySummary[])csArray);
				    	atomicPeerStatus.set(PeerStatus.CHANGE);
				    	networkUser.receive(serverAddress, csArray);
				    	this.setTimeStamp(System.currentTimeMillis());
				    	break;
			    }
		    	
			    case MISSING: {
				    	// its a minor change, new component has just joined.
//				    	capabilitySummaryCollection.addCapabilitySummary(serverAddress, (CapabilitySummary[])csArray);
				    	atomicPeerStatus.set(PeerStatus.NEW_JOIN);
				    	networkUser.receive(serverAddress, csArray);
				    	this.setTimeStamp(System.currentTimeMillis());
				    	break;
			    }
			    	
		    		default:{ 
		    			// invoke receive method to trace peer heartbeat.
		    			networkUser.receive(serverAddress, csArray);
		    			this.setTimeStamp(System.currentTimeMillis());
		    			break;
		    		}
			}
		}
		else {
			// update the time stamp, peer is alive 
		 	this.setTimeStamp(System.currentTimeMillis());
		 	// do i really need to set this to alive.
		 	//this.getAtomicPeerStatus().set(PeerStatus.ALIVE);
		}
	}
}
