package decide.qv.prism;

import auxiliary.Utility;
import decide.evaluator.AttributeEvaluatorNew;
import decide.evaluator.QVNew;

public class PrismQVNew implements QVNew {
	
	/** PrismAPI handler */
	private PrismAPINew prism;
	
	/** Stochastic model & properties filenames*/
//	protected String MODEL_FILENAME 		= Utility.getProperty("MODEL_FILE");
//	protected String PROPERTIES_FILENAME 	= Utility.getProperty("PROPERTIES_FILE");
	protected String RQV_OUTPUT_FILENAME	= Utility.getProperty("RQV_OUTPUT_FILE");

//	/** # of CSL properties */
//	private int NUM_OF_PROPERTIES;	

	
	/**
	 * Class constructor
	 */
	public PrismQVNew() {
		super();
		
		//init prism instance
		this.prism = new PrismAPINew(RQV_OUTPUT_FILENAME);		
	}	
	
	
	/**
	 * Class <b>copy</b> constructor
	 */
	private PrismQVNew (PrismQVNew instance){
		this();
	}

	
	/**
	 * Run quantitative verification for this model
	 */
	@Override
	public double run (String model, String property) {
		return prism.run(model, property);
	}
	
//	@Override
//	public void run(ConfigurationsCollectionNew modesCollection, Environment environment, boolean adjustEnvironment) {
//		
////		//For all configurations run QV
//		ConfigurationNew	config 			= null;
//		
//		while ((config = modesCollection.getNextConfiguration()) != null){ //iterate over all configurations (and their modes)
//
//			String models = config.getModel() + environment.getModel(adjustEnvironment, config, congi)
//			
//			
////			List<Double> resultsList = new ArrayList<Double> ();
////
////			for (int propertyNum=0; propertyNum<NUM_OF_PROPERTIES; propertyNum++){//for all system properties
////				//1) Instantiate parametric stochastic model								
////				String model = config.getModel() + environment.getModel(adjustEnvironment, config, propertyNum);
////		
////				//2) load PRISM model
////				prism.loadModel(model);
////				
////				//3) run PRISM
////				Double res = prism.run(propertyNum);
////				resultsList.add(res);
////			}
////			config.setResults(resultsList);
//		}
//	}

	
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
	public AttributeEvaluatorNew deepClone () {
		return new PrismQVNew(this);
	}

}
