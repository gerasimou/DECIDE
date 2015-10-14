package decide.qv.prism;

import java.util.List;

import decide.qv.QV;

public class PrismQV extends QV {
	
	private PrismAPI prism;

	/**
	 * Class constructor
	 */
	public PrismQV() {
		super();
		this.prism = new PrismAPI(RQV_OUTPUT_FILENAME, PROPERTIES_FILENAME);
	}
	
	
	/**
	 * Class <b>copy</b> constructor
	 */
	private PrismQV (PrismQV instance){
		super();
		this.prism = new PrismAPI(instance.RQV_OUTPUT_FILENAME, instance.PROPERTIES_FILENAME);
	}

	
	@Override
	public List<Double> run() {
		//1) Instantiate parametric stochastic model								
		String modelString = realiseProbabilisticModel(null);//(arguments);

		//2) load PRISM model
		prism.loadModel(modelString);

		//3) run PRISM
		List<Double> prismResult 	= prism.run();
		double req1result 			= prismResult.get(0);
		double req2result 			= prismResult.get(1);

		return prismResult;
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
