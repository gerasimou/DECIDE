package decide.configuration;

import java.util.List;

import auxiliary.Utility;

public abstract class Configuration {

	/** Stochastic model & properties filenames*/
	protected String MODEL_FILENAME 		= Utility.getProperty("MODEL_FILE");

	/** Model string*/
	protected String modelAsString;

	protected List<?> verificationResults;

	
	protected Configuration(){
		//Read the model
		this.modelAsString = Utility.readFile(MODEL_FILENAME);		
	}
	
	
	public void setResults(List<?> resultsList) {
		this.verificationResults = resultsList;
	}	
	
	
	public List<?> getResults(){
		return this.verificationResults;
	}
	
	
	public abstract String getModel(); 
	
	public abstract List<?> getConfigurationElements();

}
