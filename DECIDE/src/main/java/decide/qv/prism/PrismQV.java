package decide.qv.prism;

import java.util.ArrayList;
import java.util.List;

import decide.qv.QV;
import decide.qv.ResultQV;

public class PrismQV extends QV {
	
	/** PrismAPI handler */
	private PrismAPI prism;
	
    /** System characteristics*/
    private final int NUM_OF_SENSORS		;
    private final int NUM_OF_SENSOR_CONFIGS	;//possible sensor configurations
    private final int NUM_OF_SPEED_CONFIGS	; // [0,21], discrete steps
    private final int NUM_OF_CONFIGURATIONS ;
	

	/**
	 * Class constructor
	 */
	public PrismQV() {
		super();
		this.prism = new PrismAPI(RQV_OUTPUT_FILENAME, PROPERTIES_FILENAME);

		//init system characteristics
	    NUM_OF_SPEED_CONFIGS	= 21; // [0,21], discrete steps
	    NUM_OF_SENSORS			= 3;
	    NUM_OF_SENSOR_CONFIGS	= (int) (Math.pow(2,NUM_OF_SENSORS)); //possible sensor configurations
	    NUM_OF_CONFIGURATIONS	= (NUM_OF_SENSOR_CONFIGS-1) * NUM_OF_SPEED_CONFIGS; //discard configuration in which all sensors are switch off
	}
	
	
	/**
	 * Class <b>copy</b> constructor
	 */
	private PrismQV (PrismQV instance){
		this();
	}

	
	@Override
	public List<ResultQV> run(Object ... args) {
		List<ResultQV> resultsList = new ArrayList<ResultQV>(); 
		
		
		//For all configurations run QV and populate RQVResultArray
		for (int CSC=1; CSC<NUM_OF_SENSOR_CONFIGS; CSC++){
			for (int s=20; s<=40; s++){

				Object[] arguments = new Object[9]; 
				arguments[0]	= args[0];
				arguments[1]	= args[1];
				arguments[2]	= args[2];
				arguments[3]	= estimateP(s/10.0, 5);
				arguments[4]	= estimateP(s/10.0, 7);
				arguments[5]	= estimateP(s/10.0, 11);
				arguments[6]	= args[3];
				arguments[7]	= CSC;
				arguments[8]	= s/10.0;

				//1) Instantiate parametric stochastic model								
				String modelString = realiseProbabilisticModel(arguments);
		
				//2) load PRISM model
				prism.loadModel(modelString);
		
				//3) run PRISM
				List<Double> prismResult 	= prism.run();
				double req1Result 			= prismResult.get(0);
				double req2Result 			= prismResult.get(1);
				
				resultsList.add(new ResultQV(CSC, s/10.0, req1Result, req2Result));
			}
		}
		
		return resultsList;
	}

	
	@Override
	public void close() {
		prism.close();
	}

	
	/**
	 * Clone the QV handler
	 */
	@Override
	public QV deepClone(Object ... args) {
		QV newHandler = new PrismQV(this);
		return newHandler;
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

}
