package network;

import java.util.concurrent.atomic.AtomicReference;

import decide.StatusComponent;
import decide.capabilitySummary.CapabilitySummaryCollection;
import decide.localControl.LocalControl;
import decide.receipt.CLAReceipt;


public abstract class ReceiverDECIDE implements Runnable {

	protected CLAReceipt claReceipt;
	
	protected LocalControl localControl;
	
	protected CapabilitySummaryCollection capabilitySummaryCollection;
	
	protected volatile long timeStamp;
	
	protected volatile String replyMessage = "";
	
	protected  int status;
	
	/** server address*/
	protected String serverAddress;
		
	protected AtomicReference<StatusComponent> atomicPeerStatus;				
	
	
	public void setCLAReceipt (CLAReceipt claReceipt, CapabilitySummaryCollection capabilitySummaryCollection, long timeStamp){
		this.claReceipt 			= claReceipt;
		this.capabilitySummaryCollection 	= capabilitySummaryCollection;
		this.timeStamp			= timeStamp;
		this.status 				= 1;
		this.atomicPeerStatus	= new AtomicReference<StatusComponent>(StatusComponent.MISSING);
	}
	
	
	public void setLocalControl (LocalControl localControl, long timeStamp){
		this.localControl 		= localControl;
		this.timeStamp			= timeStamp;
		this.status 				= 1;
		this.atomicPeerStatus	= new AtomicReference<StatusComponent>(StatusComponent.MISSING);
	}
	
	
	public String getServerAddress() {
		return serverAddress;
	}
	
	
//	public CapabilitySummaryCollection getCapabilitySummary() {
//		return capabilitySummaryCollection;
//	}

	
	public AtomicReference<StatusComponent> getAtomicPeerStatus() {
		return atomicPeerStatus;
	}

	
	public int getStatus() {
		return status;
	}
	

	public String getReplyMessage() {
		return replyMessage;
	}

	
	public void setReplyMessage(String replyMessage) {
		this.replyMessage = replyMessage;
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
