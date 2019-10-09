package network;

import java.util.concurrent.atomic.AtomicReference;


public abstract class ReceiverDECIDENew implements Runnable {

	protected NetworkUser networkUser;
		
	protected volatile long timeStamp;
	
	protected volatile String replyMessage = "";
	
	protected  int status;
	
	/** server address*/
	protected String serverAddress;
		
	protected AtomicReference<PeerStatus> atomicPeerStatus;				
	
	
	public void setNetworkUser (NetworkUser networkUser, long timeStamp){
		this.networkUser 		= networkUser;
		this.timeStamp			= timeStamp;
		this.status 			= 1;
		this.atomicPeerStatus	= new AtomicReference<PeerStatus>(PeerStatus.MISSING);
	}
	
	
	public String getServerAddress() {
		return serverAddress;
	}
	
		
	public AtomicReference<PeerStatus> getAtomicPeerStatus() {
		return atomicPeerStatus;
	}

	
	public int getStatus() {
		return status;
	}
	

	public String getReplyMessage() {
		return replyMessage;
	}

	
	public void setReplyMessage(String replyMessage, boolean receivedEnvironmentMapUpdated) {
		this.replyMessage = replyMessage;
		if (receivedEnvironmentMapUpdated)
			status			  = 1;
	}

	
	public void setStatus(int status) {
		this.status = status;
	}

	
	public long getTimeStamp() {
		return timeStamp;
	}

	
	public void setTimeStamp(long timeStamp) {
		this.timeStamp = timeStamp;
	}

	
	public abstract void  run();
}
