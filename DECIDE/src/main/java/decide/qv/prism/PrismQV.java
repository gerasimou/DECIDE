package decide.qv.prism;

import java.util.ArrayList;
import java.util.List;

import auxiliary.Utility;
import decide.configuration.Configuration;
import decide.configuration.ResultDECIDE;
import decide.localAnalysis.LocalCapabilityAnalysis;
import decide.qv.QV;

public class PrismQV implements QV {
	
	/** PrismAPI handler */
	private PrismAPI prism;
	
	/** Stochastic model & properties filenames*/
	protected String MODEL_FILENAME 		= Utility.getProperty("MODEL_FILE");
	protected String PROPERTIES_FILENAME 	= Utility.getProperty("PROPERTIES_FILE");
	protected String RQV_OUTPUT_FILENAME	= Utility.getProperty("RQV_OUTPUT_FILE");

	/** # of CSL properties */
	private int NUM_OF_PROPERTIES;	

	/**
	 * Class constructor
	 */
	public PrismQV() {
		super();
		
		//init prism instance
		this.prism = new PrismAPI(RQV_OUTPUT_FILENAME, PROPERTIES_FILENAME);
		
		//init #of CSL properties
		this.NUM_OF_PROPERTIES = 2;
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
	public void run(Object ... args) {
		
//		//For all configurations run QV
		ResultDECIDE result = null;
		Configuration config = (Configuration)args[0];

		
		while ((result = config.getNext()) != null){
			List<Double> resultsList = new ArrayList<Double> ();

			//1) Instantiate parametric stochastic model								
			String model = result.getModel();
	
			//2) load PRISM model
			prism.loadModel(model);
			
			//3) run PRISM
			for (int propertyNum=0; propertyNum<NUM_OF_PROPERTIES; propertyNum++){//for all system properties
				Double res = prism.run(propertyNum);
				resultsList.add(res);
			}
			result.setResults(resultsList);
		}
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
