package decide.qv.prism;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import decide.qv.QV;
import parser.ast.ModulesFile;
import parser.ast.PropertiesFile;
import prism.Prism;
import prism.PrismFileLog;
import prism.PrismLog;
import prism.Result;

public class PrismAPI extends QV{
	
	/** Prism handler */
	private Prism prism;

	/** PrismLog (required by Prism) */
	private PrismLog mainLog;
	
	/** Modules file */
	private ModulesFile modulesFile;
	
	/** Properties file */
	private PropertiesFile propertiesFile;

	/** Model string */
	private String modelString;

	/** Property file*/
	private File propertyFile;

	
	
	/**
	 * Class constructor
	 */
	public PrismAPI(){
		super();
		try {
			// initialise PRISM
			mainLog = new PrismFileLog(RQV_OUTPUT_FILENAME, false);
			prism = new Prism(mainLog, mainLog);
			prism.initialise();
//			prism.setLinEqMethod(1);
//			prism.setMaxIters(100000);
			
			this.propertyFile = new File(PROPERTIES_FILENAME);
			
		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
		} 
	}
	
	
	/**
	 * Class <b>copy</b> constructor
	 * @param instance
	 */
	private PrismAPI (PrismAPI instance){
		this();
	}
	

	/**
	 * Load the model given as a String
	 * @param modelString
	 */
	public void loadModel(String modelString) {
		// and build the model
		try {
			this.modelString = modelString;
			modulesFile = prism.parseModelString(this.modelString);
			modulesFile.setUndefinedConstants(null);
			propertiesFile = prism.parsePropertiesFile(modulesFile,propertyFile);
			propertiesFile.setUndefinedConstants(null);
			prism.buildModel(modulesFile);
		} 
		catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
		}
	}

	
	/**
	 * This function receives data for the model and returns a double value for
	 * the quantified property.
	 */
	public List<Double> run() {

		List<Double> results = new ArrayList<Double>();

		try {
			// run QV
			for (int i = 0; i < propertiesFile.getNumProperties(); i++) {
				Result result = prism.modelCheck(propertiesFile,propertiesFile.getProperty(i));
//				System.out.println(propertiesFile.getProperty(i));
				if (result.getResult() instanceof Boolean) {
					boolean booleanResult = (Boolean) result.getResult();
					
					if (booleanResult) {
						results.add(1.0);
					} else {
						results.add(0.0);
					}
				} else
					results.add(Double.parseDouble(result.getResult().toString()));
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Model checking error");
			System.exit(-1);
		}
		return results;
	}

	
	/**
	 * Cleanup
	 */
	public void closeDown(){
		if (mainLog!=null)
			mainLog.close();
		modulesFile = null;
		propertiesFile = null;
		propertyFile = null;
		prism = null;
	}


	/**
	 * Clone the QV handler
	 */
	@Override
	public QV deepClone() {
		QV newHandler = new PrismAPI(this);
		return newHandler;
	}
	
	

}
