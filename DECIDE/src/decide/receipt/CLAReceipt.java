
package decide.receipt;

import java.io.Serializable;
import java.util.List;

import network.MulticastReceiver;
import network.ServerDECIDE;
import network.ServerSocketDECIDE;

public abstract class CLAReceipt implements Serializable{
	
	/** peers list */	
	protected List<ServerDECIDE> peersList;

	
	public CLAReceipt() {
		
	}

	
	public void setPeersList(List<ServerDECIDE> peersList){
		this.peersList = peersList;
		//start the receivers
		for (ServerDECIDE receiver : this. peersList){
			new Thread(receiver, receiver.toString()).start();
		}
	}
	
	public abstract void execute(Object...args);

	public abstract CLAReceipt deepClone();
}
