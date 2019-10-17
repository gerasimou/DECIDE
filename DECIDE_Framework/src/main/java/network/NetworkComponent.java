package network;

import java.util.concurrent.atomic.AtomicReference;

import decide.StatusComponent;


public abstract class NetworkComponent {

	/** server address*/
	final protected String serverAddress;
	
	/** server port */
	final protected int serverPort;

    final protected ComponentTypeDECIDE networkType;
    
	protected volatile long timeStamp;
    
    final long TIME_WINDOW;

	private AtomicReference<StatusComponent> componentStatus;		

    
    public NetworkComponent (String serverAddress, int serverPort, ComponentTypeDECIDE networkType) {
    	this.serverAddress 		= serverAddress;
    	this.serverPort			= serverPort;
    	this.networkType		= networkType;
		this.componentStatus	= new AtomicReference<StatusComponent>(StatusComponent.OFFLINE);
		this.TIME_WINDOW		= 15000; // time window for detecting whether the network component is stale or not
    }
    
    
	public String getServerAddress() {
		return serverAddress;
	}
	
	
	public StatusComponent getAtomicPeerStatus() {
		return componentStatus.get();
	}
	
	
	public void setAtomicPeerStatus (StatusComponent status) {
		componentStatus.set(status);
	}


	public boolean checkStatus (StatusComponent status) {
		return (componentStatus.get() == status); 
	}

	
	public void setTimeStamp(long timeStamp) {
		this.timeStamp = timeStamp;
	}


	public boolean hasMajorChange (final long TIME_NOW) {
		//component is ALIVE/CHANGED and received a message (CLA or heartbeat) within the time window
		if ( ( checkStatus(StatusComponent.ALIVE) || checkStatus(StatusComponent.CHANGE) ) && (TIME_NOW - timeStamp <= TIME_WINDOW) ) {
			return false;
		}
		//component is already missing (so no need to to anything else)
		else if (checkStatus(StatusComponent.MISSING)) {
			return false;
		}
		//component is affected by a major change
		else {
			//change the status of this receiver
			setAtomicPeerStatus(StatusComponent.MISSING);
			return true;
		}
	}
}
