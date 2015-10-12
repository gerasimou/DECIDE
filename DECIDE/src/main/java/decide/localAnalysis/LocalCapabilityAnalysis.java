package decide.localAnalysis;

import java.io.Serializable;
import java.util.List;

import decide.qv.prism.QV;
import network.ClientDECIDE;
import network.ClientSocketDECIDE;
import network.MulticastTransmitter;

public abstract class LocalCapabilityAnalysis implements Serializable{

	/** DECIDE peers */
	protected ClientDECIDE client;
	
	/** QV handler */
	private QV qv;
	
	
	/**
	 * Class constructor
	 * @param qvInstance
	 */
	public LocalCapabilityAnalysis(QV qvInstance) {
		this.qv = qvInstance;
	}


	/**
	 * Assign this DECIDE instance client, i.e., where it can transmit
	 * @param client
	 */
	public void client(ClientDECIDE client){
		this.client = client;
	}

	
	/**
	 * Return the QV instance
	 * @return
	 */
	public QV getQV(){
		return this.qv;
	}

	
	/**
	 * <b>Abstact</b> execute action
	 * @param args
	 */
	public abstract void execute(Object...args);
}
