
package decide.receipt;

import java.io.Serializable;
import java.util.List;
import network.ServerDECIDE;

public abstract class CLAReceipt implements Serializable{
	
	/** peers list */	
	protected List<ServerDECIDE> serversList;

	
	protected CLAReceipt() {
		
	}

	
	public void setServersList(List<ServerDECIDE> serverList){
		this.serversList = serverList;
		//start the receivers
		for (ServerDECIDE server : this. serversList){
			new Thread(server, server.toString()).start();
		}
	}
	
	public abstract void execute(Object...args);

	public abstract CLAReceipt deepClone();
}
