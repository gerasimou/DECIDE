package decide.qv;

import java.util.List;

import auxiliary.Utility;

public abstract class QV {
	
	/** Stochastic model & properties filenames*/
	protected final String MODEL_FILENAME 		= Utility.getProperty("MODEL_FILE");
	protected final String PROPERTIES_FILENAME 	= Utility.getProperty("PROPERTIES_FILE");
	protected final String RQV_OUTPUT_FILENAME	= Utility.getProperty("RQV_OUTPUT_FILE");
	
	
	/** Model string*/
	String modelAsString;
	
	
	/**
	 * Class constructor
	 */
	public QV(){
		//Read the model
		this.modelAsString = Utility.readFile(MODEL_FILENAME);		
	}

    /**
     * Generate a complete and correct PRISM model using parameters given
     * @param parameters for creating a correct PRISM model
     * @return a correct PRISM model instance as a String
     */
    private String realiseProbabilisticModel(Object ... parameters){
    	StringBuilder model = new StringBuilder(modelAsString + "\n\n//Variables\n");

    	//process the given parameters
		model.append("const double r1  = "+ parameters[0].toString() +";\n");
		model.append("const double r2  = "+ parameters[1].toString() +";\n");
		model.append("const double r3  = "+ parameters[2].toString() +";\n");
		model.append("const double p1  = "+ parameters[3].toString() +";\n");
		model.append("const double p2  = "+ parameters[4].toString() +";\n");
		model.append("const double p3  = "+ parameters[5].toString() +";\n");
		model.append("const int    PSC = "+ parameters[6].toString() +";\n");
		model.append("const int    CSC = "+ parameters[7].toString() +";\n");
		model.append("const double s   = "+ parameters[8].toString() +";\n\n");
    	
    	return model.toString();
    }   
    
	/**
	 * Estimate Probability of producing a successful measurement
	 * @param speed
	 * @param alpha
	 * @return
	 */
	private static double estimateP(double speed, double alpha){
		return 100 - alpha * speed;
	}
    
    	
	protected abstract void loadModel(String modelString);

	public abstract List<Double> run();

	public abstract void closeDown();
	
	public abstract QV deepClone();
	
}
