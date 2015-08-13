package decide.lca;

import java.io.Serializable;
import java.util.List;

import network.ClientDECIDE;

public abstract class LocalCapabilityAnalysis implements Serializable{

	/** DECIDE peers */
	private List<ClientDECIDE> peersList;
	
	
	public LocalCapabilityAnalysis() {
		
	}
	
	
	/** Start clients: connect to their servers */
	public void startPeers(){
		for (ClientDECIDE peer: peersList){
			peer.init();
		}
		for (ClientDECIDE peer: peersList){
			new Thread(peer, this.getClass().getSimpleName()).start();
		}
//		System.out.println("DONE");
//		new Thread(server, this.getClass().getSimpleName()).start();
	}

	
	
	public void setPeersList(List<ClientDECIDE> peersList){
		this.peersList = peersList;
	}
	
	public abstract void execute();
}
