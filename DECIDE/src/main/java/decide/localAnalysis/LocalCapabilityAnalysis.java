package decide.localAnalysis;

import java.io.Serializable;
import java.util.List;

import network.ClientDECIDE;
import network.ClientSocketDECIDE;
import network.MulticastTransmitter;

public abstract class LocalCapabilityAnalysis implements Serializable{

	/** DECIDE peers */
	
	protected ClientDECIDE client;
	
	public LocalCapabilityAnalysis() {
		
	}


	public void client(ClientDECIDE client){
		this.client = client;
	}
	
	
	public abstract void execute(Object...args);
}
