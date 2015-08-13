
package decide.receipt;

import java.io.Serializable;

import network.ServerDECIDE;

public abstract class CLAReceipt implements Serializable{
	
	/** server DECIDE handler */
	private ServerDECIDE server;

	
	public CLAReceipt() {
		
	}


	/**
	 * Set the DECIDE server
	 * @param server
	 */
	public void setServerDECIDE(ServerDECIDE server){
		this.server = server;
	}
	

	/** Start server */
	public void startServer(){
		new Thread(server, this.getClass().getSimpleName()).start();
	}

	public abstract void execute();

	public abstract CLAReceipt deepClone();
}
