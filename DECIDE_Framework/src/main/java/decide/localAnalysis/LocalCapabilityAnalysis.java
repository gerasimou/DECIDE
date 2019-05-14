package decide.localAnalysis;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import auxiliary.Utility;
import decide.configuration.ConfigurationsCollection;
import decide.environment.Environment;
import decide.evaluator.PropertyEvaluator;
import network.ClientDECIDE;

public abstract class LocalCapabilityAnalysis implements Serializable{

	/** DECIDE peers */
	protected ClientDECIDE client;
	
	/** Property Evaluator handler */
	private PropertyEvaluator propertyEvaluator;
	
	
	
	/** Confidence array*/
	protected static Map<String, Double> confidenceMap;
	
	
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
	public void assignClient(ClientDECIDE client){
		this.client = client;
	}

	
	
	
	/**
	 * <b>Abstact</b> execute action
	 * @param args
	 */
	public abstract void execute(ConfigurationsCollection modesCollection, Environment environment, Object...args);
	
	
	
	public abstract LocalCapabilityAnalysis deepClone(Object ... args);
	
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

	// newlly added
	public ClientDECIDE getClient() {
		return client;
	}

	/**
	 * Return the QV instance
	 * @return
	 */
	public PropertyEvaluator getPropertyEvaluator() {
		return propertyEvaluator;
	}


	public void setPropertyEvaluator(PropertyEvaluator propertyEvaluator) {
		this.propertyEvaluator = propertyEvaluator;
	}

	
}
