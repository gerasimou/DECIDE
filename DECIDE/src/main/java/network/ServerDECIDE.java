package network;

import decide.receipt.CLAReceipt;

public abstract class ServerDECIDE implements Runnable {

	protected CLAReceipt claReceipt;
	
	public void setCLAReceipt (CLAReceipt claReceipt){
		this.claReceipt = claReceipt;
	}
}
