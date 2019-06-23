package caseStudies.uuv;

import org.apache.log4j.Logger;

import decide.OperationMode;
import decide.receipt.CLAReceipt;
import network.PeerStatus;
import network.ReceiverDECIDE;


public class UUVCLAReceipt extends CLAReceipt{
	
	/** Logging system events*/
    final static Logger logger = Logger.getLogger(UUVCLAReceipt.class);
		
    
	/**
	 * Class constructor
	 */
	public UUVCLAReceipt() {
		super();
	}
		
		
	/**
	 * Class <b>copy</b> constructor
	 */
	private UUVCLAReceipt(UUVCLAReceipt instance) {
		super();
	}


	/**
	 * Execute this CLAReceipt handler
	 */
	@Override
	public boolean execute(Object...args){
		return this.receivedNewCapabilitySummary;
	}

		
	/**
	 * Maintains heartbeat trace of peer components
	 */
	@Override
	public boolean executeListeningThread() {
		// 0 is no change has been recorded, 1 is major change
		boolean change = false;
		long timestamp = 0;
		int alivecounter = 0;
		
		receivedTimeStamp = System.currentTimeMillis();
		
		//for all connections to my peers 
		for (ReceiverDECIDE server : serversList){
			timestamp = server.getTimeStamp();
			
			//check if peer is missing
			if (timestamp > 0) { 
				if(logger.isDebugEnabled())
					logger.debug("[PEER:"+server.getServerAddress()+",Current TS:"+
							receivedTimeStamp+",PreviousTS:"+timestamp+",Latency:"+(receivedTimeStamp-timestamp)+"]");
				else
					logger.info("[PEER:"+server.getServerAddress()+",Latency:"+(receivedTimeStamp-timestamp)+"]");
				
				alivecounter++;
				
				//i.e., I haven't received any notification recently, probably peer is missing
				if ((receivedTimeStamp - timestamp) > (TIME_WINDOW+20000)) {
					alivecounter--;
					server.setTimeStamp(0);
					server.getAtomicPeerStatus().set(PeerStatus.MISSING);	// Set its status as missing
					removeCapabilitySummary(server.getServerAddress()); 	// Remove missing peer capability summary
					change = true;
				}
			}
			
			switch(server.getAtomicPeerStatus().get()) {
			    case NEW_JOIN: // its a Major change
				    	change = true;
				    	server.getAtomicPeerStatus().set(PeerStatus.ALIVE);
				    	break;
			    	
			    case CHANGE: // its a minor change			    	
				    	change = true;
				    	server.getAtomicPeerStatus().set(PeerStatus.ALIVE);
				    	break;			    	
			    	default: break;
			}
		}
			
		// its a major change, kill the thread and reset received flag after safe time window. 
		// Also interrupt the main thread
			if(change) 
				atomicOperationReference.set(OperationMode.MAJOR_CHANGE_MODE);
		
			if (alivecounter ==0) {
				receivedNewCapabilitySummary = false;
				return true;
			}
			
		return false;
	}

		
		/**
	 * Clone the CLAReceipt handler
	 */
	@Override
	public CLAReceipt deepClone(Object ... args) {
		CLAReceipt newHandler = new UUVCLAReceipt(this);
		return newHandler;
	}		
}

