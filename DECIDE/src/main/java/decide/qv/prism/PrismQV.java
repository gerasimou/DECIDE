package decide.qv.prism;

import java.util.ArrayList;
import java.util.List;

import decide.localAnalysis.LocalCapabilityAnalysis;
import decide.qv.QV;
import decide.qv.ResultQV;

public class PrismQV extends QV {
	
	/** PrismAPI handler */
	private PrismAPI prism;
	
	/** # of CSL properties */
	private final int CSL_PROPERTIES;
	
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
		
		//init prism instance
		this.prism = new PrismAPI(RQV_OUTPUT_FILENAME, PROPERTIES_FILENAME);
		
		//init #of CSL properties
		this.CSL_PROPERTIES = 2;

		//init system characteristics
	    this.NUM_OF_SPEED_CONFIGS	= 21; // [0,21], discrete steps
	    this.NUM_OF_SENSORS			= 3;
	    this.NUM_OF_SENSOR_CONFIGS	= (int) (Math.pow(2,NUM_OF_SENSORS)); //possible sensor configurations
	    this.NUM_OF_CONFIGURATIONS	= (NUM_OF_SENSOR_CONFIGS-1) * NUM_OF_SPEED_CONFIGS; //discard configuration in which all sensors are switch off	    
	}
	
	
	/**
	 * Class <b>copy</b> constructor
	 */
	private PrismQV (PrismQV instance){
		this();
	}

	
	/**
	 * Run quantitative verification for this model
	 * <b>This method is application-specific and works only with the UUV case study </b>
	 * arg[0]: sensor 1 rate
	 * arg[1]: sensor 2 rate
	 * arg[2]: sensor 3 rate
	 * arg[3]: previous sensors configuration \in 1, ..., 7
	 */
	@Override
	public List<ResultQV> run(Object ... args) {
		List<ResultQV> resultsList = new ArrayList<ResultQV>(); 
		
		Object[] arguments = new Object[9]; 

		double[] resultArray = new double[CSL_PROPERTIES];
		
		//For all configurations run QV and populate RQVResultArray
		for (int CSC=1; CSC<NUM_OF_SENSOR_CONFIGS; CSC++){//for all possible sensors configurations

			for (int s=20; s<=40; s++){//for all possible UUV speed				
				arguments[3]	= estimateP(s/10.0, 5);		//sensor 1 accuracy
				arguments[4]	= estimateP(s/10.0, 7);		//sensor 2 accuracy
				arguments[5]	= estimateP(s/10.0, 11);	//sensor 3 accuracy
				arguments[6]	= args[3];					//previous configuration
				arguments[7]	= CSC;						//current configuration
				arguments[8]	= s/10.0;					//speed

				for (int propertyNum=0; propertyNum<CSL_PROPERTIES; propertyNum++){//for all system properties
					arguments[0]	= estimateEnvironment(CSC, propertyNum, Double.parseDouble(args[0].toString())); //sensor 1 rate
					arguments[1]	= estimateEnvironment(CSC, propertyNum, Double.parseDouble(args[1].toString())); //sensor 2 rate
					arguments[2]	= estimateEnvironment(CSC, propertyNum, Double.parseDouble(args[2].toString())); //sensor 3 rate

					//1) Instantiate parametric stochastic model								
					String modelString = realiseProbabilisticModel(arguments);
			
					//2) load PRISM model
					prism.loadModel(modelString);
					
					//3) run PRISM
					resultArray[propertyNum] 	= prism.run(propertyNum);
				}
				
				resultsList.add(new ResultQV(CSC, s/10.0, resultArray[0], resultArray[1]));
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
	
	
	private double estimateEnvironment(int CSC, int propertyNum, double sensorRate){
		double stDeviation = 0.3;
		
		double confidenceValue = -1;
		if (CSC==1 || CSC==2 || CSC==4){
			confidenceValue = LocalCapabilityAnalysis.getConfidenceValue("1");
		}
		else if (CSC==3 || CSC==5 || CSC==6){
			confidenceValue = LocalCapabilityAnalysis.getConfidenceValue("2");
		}
		else if (CSC==7){
			confidenceValue = LocalCapabilityAnalysis.getConfidenceValue("3");
		}
		else 
			throw new IllegalArgumentException("Current sensor configuration outside boundaries");
		
		
		if (propertyNum==0){
			return Math.max(0.1, sensorRate - confidenceValue * stDeviation);
		}
		else if (propertyNum==1){
			return Math.max(0.1, sensorRate + confidenceValue * stDeviation);			
		}
		else 
			throw new IllegalArgumentException("Property index outside boundaries");
	}

}
