package decide.lca;

import java.io.Serializable;
import java.util.List;

import network.ClientDECIDE;
import network.MulticastTransmitter;

public abstract class LocalCapabilityAnalysis implements Serializable{

	/** DECIDE peers */
//	private List<ClientDECIDE> peersList;
	
//	private MulticastTransmitter transmitter;
	
	public LocalCapabilityAnalysis() {
		
	}
	
	
	/** Start clients: connect to their servers */
	@Deprecated
	public void startPeers(){
//		for (ClientDECIDE peer: peersList){
//			peer.init();
//		}
//		for (ClientDECIDE peer: peersList){
//			new Thread(peer, this.getClass().getSimpleName()).start();
//		}
////		System.out.println("DONE");
////		new Thread(server, this.getClass().getSimpleName()).start();
	}


	@Deprecated
	public void setTransmitter(MulticastTransmitter transmitter){
//		this.transmitter = transmitter;
	}
	
	@Deprecated
	public void setPeersList(List<ClientDECIDE> peersList){
//		this.peersList = peersList;
	}
	
	public abstract void execute();
}
