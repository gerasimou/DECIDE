
package decide.receipt;

import java.io.Serializable;
import java.util.List;

import network.MulticastReceiver;
import network.ServerDECIDE;

public abstract class CLAReceipt implements Serializable{
	
	/** server DECIDE handler */
//	private ServerDECIDE server;
	
//	private List<MulticastReceiver> peersList;

	
	public CLAReceipt() {
		
	}

	
	@Deprecated
	public void setPeersList(List<MulticastReceiver> peersList){
//		this.peersList = peersList;
	}
	

	/**
	 * Set the DECIDE server
	 * @param server
	 */
	@Deprecated
	public void setServerDECIDE(ServerDECIDE server){
//		this.server = server;
	}
	

	/** Start server */
	@Deprecated
	public void startServer(){
//		new Thread(server, this.getClass().getSimpleName()).start();
	}

	public abstract void execute();

	public abstract CLAReceipt deepClone();
}
