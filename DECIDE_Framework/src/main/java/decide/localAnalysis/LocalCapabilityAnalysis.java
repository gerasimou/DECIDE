package decide.localAnalysis;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import auxiliary.Utility;
import decide.configuration.ConfigurationsCollection;
import decide.environment.Environment;
import decide.evaluator.AttributeEvaluator;
import network.NetworkUser;
import network.TransmitterDECIDE;

public abstract class LocalCapabilityAnalysis implements Serializable{

	/** DECIDE peers */
	protected TransmitterDECIDE transmitter;
	
	/** Property Evaluator handler */
	private AttributeEvaluator attributeEvaluator;
	
	/** Confidence array*/
	protected static Map<String, Double> confidenceMap;
	
	
	
	/**
	 * Class constructor
	 * @param qvInstance
	 */
	protected LocalCapabilityAnalysis(boolean deepClone) {
		assignConfidenceArray();
		
		if (deepClone) {
			attributeEvaluator	= attributeEvaluator.deepClone();
			transmitter				= transmitter.deepClone();
		}
			
	}


	/**
	 * Assign this DECIDE instance client, i.e., where it can transmit
	 * @param client
	 */
	public void setTransmitterToOtherDECIDE(TransmitterDECIDE client){
		this.transmitter = client;
	}

	
	/**
	 * <b>Abstact</b> execute action
	 * @param args
	 */
	public abstract void execute(ConfigurationsCollection modesCollection, Environment environment);
	
	
	/**
	 * Deep clone this local capability analysis object
	 * @param args
	 * @return
	 */
	public abstract LocalCapabilityAnalysis deepClone();
	
	
	/**
	 * Share capability summary with peers
	 */
	public abstract void shareCapabilitySummary(Object ... args);

	
	private void assignConfidenceArray(){
		//instantiate the confidence hashmap
		confidenceMap = new HashMap<String, Double>();
		
		//get the confidence values specified in config.properties file.
		double confidence = Double.parseDouble(Utility.getProperty("a-CONFIDENCE"));
		
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

	
	/**
	 * Return the QV instance
	 * @return
	 */
	public AttributeEvaluator getAttributeEvaluator() {
		return attributeEvaluator;
	}


	/** Set this property evaluator
	 * 
	 * @param attributeEvaluator
	 */
	public void setPropertyEvaluator(AttributeEvaluator attributeEvaluator) {
		this.attributeEvaluator = attributeEvaluator;
	}

	
}