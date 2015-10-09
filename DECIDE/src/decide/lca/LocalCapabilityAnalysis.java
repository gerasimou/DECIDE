package decide.lca;

import java.io.Serializable;
import java.util.List;

import network.ClientDECIDE;
import network.ClientSocketDECIDE;
import network.MulticastTransmitter;

public abstract class LocalCapabilityAnalysis implements Serializable{

	/** DECIDE peers */
	
	protected ClientDECIDE transmitter;
	
	public LocalCapabilityAnalysis() {
		
	}


	public void setTransmitter(ClientDECIDE transmitter){
		this.transmitter = transmitter;
	}
	
	
	public abstract void execute(Object...args);
}
