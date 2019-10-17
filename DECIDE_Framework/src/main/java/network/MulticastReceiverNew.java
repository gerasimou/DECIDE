package network;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.Objects;
import org.apache.log4j.Logger;

import decide.StatusComponent;
import decide.receipt.CLAReceiptNew;


public class MulticastReceiverNew extends ReceiverDECIDENew{		
	
	/** Multicast socket */
	private MulticastSocket receiverSocket;
	
	
	/** Buffer containing the data received */
	byte[] buf;
	private final int BUFFER_SIZE = 20000;

	
	/** Logging system events*/
    final static Logger logger = Logger.getLogger(MulticastReceiverNew.class);
    
	
    /** Heartbeat string*/
    private final String HR = "1111";
	
	

    /**
	 * Class constructor: create a new multicast receiver
	 * @param serverAddress
	 * @param port
	 */
	public MulticastReceiverNew (String serverAddress, int port, ComponentTypeDECIDE networkType) {
    	super(serverAddress, port, networkType);
		

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
            
            setAtomicPeerStatus(StatusComponent.ALIVE);
        }
        catch (IOException e) {
			e.printStackTrace();
		}
	}
	

	@Override
	public void run() {
		try{
			DatagramPacket msgPacket = new DatagramPacket(buf, buf.length);
	        
			while (true) {
	        		// Receive the information and print it
	            receiverSocket.receive(msgPacket);
	            
	            byte data[] 				= msgPacket.getData();
	            ByteArrayInputStream bais 	= new ByteArrayInputStream(data);
	            ObjectInputStream     ois  	= new ObjectInputStream(bais);
	            try {
//	            	Object [] csArray = ((Object[])ois.readObject());
//	            	processReceivedData(csArray);
	            	Object receivedObject = ois.readObject();
	            	
	            	if (receivedObject.equals(HR))
	            		processReceivedHeartBeat();
	            	else
	            		processReceivedDataCLA(receivedObject);
	            	
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
	
	
	private void processReceivedDataCLA(Object receivedObject) {

//		if(logger.isDebugEnabled())
			logger.info("Received from:"+serverAddress+", " + receivedObject+",[Status: "+getAtomicPeerStatus()+"]");
	
		//this DECIDE peer is known to use
		if ( (networkUser instanceof CLAReceiptNew)) {// && ( ((CLAReceiptNew)networkUser).isKnownReceiver(serverAddress)) ) {
    		setAtomicPeerStatus(StatusComponent.CHANGE);
	    	networkUser.receive(serverAddress, receivedObject);
	    	setTimeStamp(System.currentTimeMillis());
		}
	}
	
	
	private void processReceivedHeartBeat() {

		logger.info("Heartbeat received from " + getServerAddress());

		//update received timestamp from peer
		setTimeStamp(System.currentTimeMillis());
		
		//if the network component has been offline, then set it to alive
		if (getAtomicPeerStatus() == StatusComponent.OFFLINE)
			setAtomicPeerStatus(StatusComponent.ALIVE);		
		else if (getAtomicPeerStatus() == StatusComponent.CHANGE)
			setAtomicPeerStatus(StatusComponent.ALIVE);		
	}
	
	
//	private void processReceivedDataCLA2(Object receivedObject) {
//	int tempHashCode = 0;
//	int hashCode = 0;
//	boolean exists = true;
//
//	// find the array hashcode to compare with previous received message
//	hashCode = Objects.hash(receivedObject);//Arrays.hashCode(csArray);
//		
//    	//System.out.println("Received from:"+serverAddress+"\t " + cs.toString()+"Peer Status is"+this.getAtomicPeerStatus().get());
//	if(logger.isDebugEnabled())
//		logger.debug("Received from:"+serverAddress+", " + receivedObject+",[Status: "+getAtomicPeerStatus()+"]");
//	// reset boolean flag
//	exists = false;
//	/* reset configuartion map if peer had been Missing */
//	if (getAtomicPeerStatus() == StatusComponent.MISSING) { //&& capabilitySummaryCollection.concurrentCapabilitySummaryMap.contains(serverAddress) )		
//		//capabilitySummaryCollection.concurrentCapabilitySummaryMap.remove(serverAddress);
//		tempHashCode = 0;
//	}
//	
//	//id = Knowledge.getPeerID(serverAddress);
//	if ( (networkUser instanceof CLAReceiptNew) && ( ((CLAReceiptNew)networkUser).isKnownReceiver(serverAddress)) ) {
//		if (hashCode == tempHashCode)
//			exists = true;
//	}
//    					
//	if (!exists) {
//		tempHashCode = hashCode;
//		// if key is not there then either status has changed or a new peer has joined
//		switch (getAtomicPeerStatus()) {
//		    case ALIVE:{
//			    	// its a minor change, peer has recalculated lca phase
//			    	// alter concurrent CS map
////			    	capabilitySummaryCollection.addCapabilitySummary(serverAddress, (CapabilitySummary[])csArray);
//		    		setAtomicPeerStatus(StatusComponent.CHANGE);
//			    	networkUser.receive(serverAddress, receivedObject);
//			    	this.setTimeStamp(System.currentTimeMillis());
//			    	break;
//		    }
//	    	
//		    case MISSING: {
//			    	// its a minor change, new component has just joined.
////			    	capabilitySummaryCollection.addCapabilitySummary(serverAddress, (CapabilitySummary[])csArray);
//		    		setAtomicPeerStatus(StatusComponent.NEW_JOIN);
//			    	networkUser.receive(serverAddress, receivedObject);
//			    	this.setTimeStamp(System.currentTimeMillis());
//			    	break;
//		    }
//		    	
//	    		default:{ 
//	    			// invoke receive method to trace peer heartbeat.
//	    			networkUser.receive(serverAddress, receivedObject);
//	    			this.setTimeStamp(System.currentTimeMillis());
//	    			break;
//	    		}
//		}
//	}
//	else {
//		// update the time stamp, peer is alive 
//	 	this.setTimeStamp(System.currentTimeMillis());
//	 	// do i really need to set this to alive.
//	 	//this.getAtomicPeerStatus().set(PeerStatus.ALIVE);
//	}
//}
}
