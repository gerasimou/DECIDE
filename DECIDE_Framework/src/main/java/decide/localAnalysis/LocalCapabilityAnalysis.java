package decide.localAnalysis;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import auxiliary.Utility;
import decide.DECIDEConstants;
import decide.capabilitySummary.CapabilitySummary;
import decide.configuration.ConfigurationsCollection;
import decide.environment.Environment;
import network.TransmitterDECIDE;


public abstract class LocalCapabilityAnalysis implements Serializable{

	/** Transmitter to DECIDE peers */
	protected TransmitterDECIDE transmitter;
	
	/** Confidence array*/
	protected static Map<String, Double> confidenceMap;
	
	/** Logging system events*/
    final protected Logger logger = LogManager.getLogger(this.getClass());
	
	
	
	/**
	 * Class constructor
	 * @param qvInstance
	 */
	protected LocalCapabilityAnalysis() {
		assignConfidenceArray();		
	}


	/**
	 * Assign this DECIDE instance client, i.e., where it can transmit
	 * @param client
	 */
	public void setTransmitterToOtherDECIDE(TransmitterDECIDE client){
		this.transmitter = client;
	}
	
	
	public TransmitterDECIDE getTransmitterToOtherDECIDE(){
		return this.transmitter; 
	}

	
	/**
	 * <b>Abstact</b> execute action
	 * @param args
	 */
	public abstract void execute(ConfigurationsCollection modesCollection, Environment environment);
	
	
	/**
	 * Share capability summary with peers
	 */
	public abstract void shareCapabilitySummary(CapabilitySummary[] csArray);

	
	private void assignConfidenceArray(){
		//instantiate the confidence hashmap
		confidenceMap = new HashMap<String, Double>();
		
		//get the confidence values specified in config.properties file.
		double confidence = Double.parseDouble(Utility.getProperty(DECIDEConstants.a_CONFIDENCE));
		
		//and populate the confidence hashmap
		if (confidence == 0.90){
			confidenceMap.put("1", 1.64);	confidenceMap.put("2", 1.95);	confidenceMap.put("3", 2.11);
		}
		else if (confidence == 0.95){
			confidenceMap.put("1", 1.96);	confidenceMap.put("2", 2.24);	confidenceMap.put("3", 2.39);
		}
		else if (confidence == 0.99){
			confidenceMap.put("1", 2.57);	confidenceMap.put("2", 2.79);	confidenceMap.put("3", 2.93);
		}
		else 
			throw new IllegalArgumentException("Confidence level outside boundaries");
	}

	
	public static double getConfidenceValue(String key){
		return confidenceMap.get(key);
	}


	public TransmitterDECIDE getClient() {
		return transmitter;
	}	
}
