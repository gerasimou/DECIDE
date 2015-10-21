package decide.configuration;

import java.util.List;

import auxiliary.Utility;

public abstract class Configuration {

	/** Stochastic model & properties filenames*/
//	protected String MODEL_FILENAME 		= Utility.getProperty("MODEL_FILE");

	/** Model string*/
	protected String modelAsString;

	/** List holding verification for this configuration */ 
	protected List<?> verificationResults;


	
	/** Class constructor
	 * Create a new configuration instance
	 */
	protected Configuration(){
		//Read the model
//		this.modelAsString = Utility.readFile(MODEL_FILENAME);		
	}
	
	
	
	/**
	 * Assign the verification results for this configuration
	 * @param resultsList
	 */
	public void setResults(List<?> resultsList) {
		this.verificationResults = resultsList;
	}	
	
	
	/** 
	 * Retrieve the verification results for this configuration
	 * @return
	 */
	public List<?> getVerificationResults(){
		return this.verificationResults;
	}
	
	
	/** 
	 * Populate and retrieve the model associated with this configuration
	 * @return
	 */
	public abstract String getModel(); 
	
	
	/**
	 * Retrieve as a list the elements of this configuration
	 * @return
	 */
	public abstract List<?> getConfigurationElements();

}
