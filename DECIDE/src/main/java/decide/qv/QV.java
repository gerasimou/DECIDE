package decide.qv;

import java.util.List;

import auxiliary.Utility;

public abstract class QV {
	
	/** Stochastic model & properties filenames*/
	protected String MODEL_FILENAME 		= Utility.getProperty("MODEL_FILE");
	protected String PROPERTIES_FILENAME 	= Utility.getProperty("PROPERTIES_FILE");
	protected String RQV_OUTPUT_FILENAME	= Utility.getProperty("RQV_OUTPUT_FILE");
	
	
	/** Model string*/
	protected String modelAsString;
	
	
	/**
	 * Class constructor
	 */
	public QV(){
		//Read the model
		this.modelAsString = Utility.readFile(MODEL_FILENAME);		
	}
    
	
	public abstract void run(Object ... args);

	public abstract void close();
	
	public abstract QV deepClone(Object ... args);
	
}
