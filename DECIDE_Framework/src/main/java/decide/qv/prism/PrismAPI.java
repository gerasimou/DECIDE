package decide.qv.prism;

import java.io.File;

import parser.ast.ModulesFile;
import parser.ast.PropertiesFile;
import prism.Prism;
import prism.PrismFileLog;
import prism.PrismLog;
import prism.Result;

public class PrismAPI{
	
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
	public PrismAPI(final String outputFileName, final String propertiesFileName){
		try {
			// initialise PRISM
			mainLog = new PrismFileLog(outputFileName, false);
			prism = new Prism(mainLog, mainLog);
			prism.initialise();
//			prism.setLinEqMethod(1);
//			prism.setMaxIters(100000);
			
			this.propertyFile = new File(propertiesFileName);
			
		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
		} 
	}
	
	
	/**
	 * Class <b>copy</b> constructor
	 * @param instance
	 */
	protected PrismAPI (PrismAPI instance){
//		this();
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
			
			if (propertiesFile == null){
				propertiesFile = prism.parsePropertiesFile(modulesFile,propertyFile);
				propertiesFile.setUndefinedConstants(null);
			}
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
	public Double run(int propertyNum) {
		try {
			double resultQV;

			// run QV
			Result result = prism.modelCheck (propertiesFile,propertiesFile.getProperty(propertyNum));
//				System.out.println(propertiesFile.getProperty(i));
			if (result.getResult() instanceof Boolean) {
				boolean booleanResult = (Boolean) result.getResult();
				
				if (booleanResult) {
					resultQV = 1.0;
				} else {
					resultQV = 0.0;
				}
			} 
			else
				resultQV = Double.parseDouble(result.getResult().toString());

			return resultQV;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Model checking error");
			System.exit(-1);
			return -1.0;
		}
	}

	
	/**
	 * Cleanup
	 */
	public void close(){
		if (mainLog!=null)
			mainLog.close();
		modulesFile = null;
		propertiesFile = null;
		propertyFile = null;
		prism = null;
	}
	
	

}