package decide.qv.prism;

import java.util.ArrayList;
import java.util.List;

import auxiliary.Utility;
import decide.configuration.Configuration;
import decide.configuration.ConfigurationsCollection;
import decide.environment.Environment;
import decide.evaluator.QV;

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
	 */
	@Override
	public void run(ConfigurationsCollection modesCollection, Environment environment, boolean adjustEnvironment) {
		
//		//For all configurations run QV
		Configuration 	config 			= null;
		
		while ((config = modesCollection.getNextConfiguration()) != null){
			List<Double> resultsList = new ArrayList<Double> ();

			for (int propertyNum=0; propertyNum<NUM_OF_PROPERTIES; propertyNum++){//for all system properties
				//1) Instantiate parametric stochastic model								
				String model = config.getModel() + environment.getModel(adjustEnvironment, config, propertyNum);
		
				//2) load PRISM model
				prism.loadModel(model);
				
				//3) run PRISM
				Double res = prism.run(propertyNum);
				resultsList.add(res);
			}
			config.setResults(resultsList);
		}
	}

	
	/**
	 * Close PrismQV & PrismAPI
	 */
	@Override
	public void close() {
		prism.close();
	}

	
	/**
	 * Clone the QV handler
	 */
	@Override
	public QV deepClone () {
		return new PrismQV(this);
	}

}