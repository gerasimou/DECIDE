package decide.localAnalysis;

import java.io.Serializable;

import decide.qv.QV;
import network.ClientDECIDE;

public abstract class LocalCapabilityAnalysis implements Serializable{

	/** DECIDE peers */
	protected ClientDECIDE client;
	
	/** QV handler */
	protected QV qv;
	
	
	/**
	 * Class constructor
	 * @param qvInstance
	 */
	protected LocalCapabilityAnalysis() {
	}


	/**
	 * Assign this DECIDE instance client, i.e., where it can transmit
	 * @param client
	 */
	public void assignClient(ClientDECIDE client){
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
	
	public abstract LocalCapabilityAnalysis deepClone(Object ... args);

}
