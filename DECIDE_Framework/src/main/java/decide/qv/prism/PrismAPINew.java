package decide.qv.prism;

import parser.ast.ModulesFile;
import parser.ast.PropertiesFile;
import prism.Prism;
import prism.PrismDevNullLog;
import prism.PrismLog;
import prism.Result;


public class PrismAPINew{
	
	/** Prism handler */
	private Prism prism;

	/** PrismLog (required by Prism) */
	private PrismLog mainLog;
	
	/** Modules file */
	private ModulesFile modulesFile;
	
	
	/**
	 * Class constructor
	 */
	public PrismAPINew(){
		try {PrismDevNullLog
			// initialise PRISM
			mainLog				= new PrismDevNullLog();// FileLog(outputFileName, false);
			prism 				= new Prism (mainLog);
			prism.initialise();
//			prism.setLinEqMethod(1);
//			prism.setMaxIters(100000);
			
		} 
		catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
		} 
	}
	

	/**
	 * Load the model given as a String
	 * @param modelString
	 */
	private void loadModel(String modelString) {
		// and build the model
		try {
			modulesFile = prism.parseModelString(modelString);
			modulesFile.setUndefinedConstants(null);	
			prism.loadPRISMModel(modulesFile);
			prism.buildModel();
		} 
		catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
		}
	}

	
	/**
	 * This function receives data for the model and returns a double value for
	 * the quantified property.
	 */
	private Double evaluate(String property) {
		try {
			double resultQV;

			PropertiesFile propertiesFile = prism.parsePropertiesString(modulesFile, property);
			propertiesFile.setUndefinedConstants(null);

			// run QV
			Result result = prism.modelCheck (propertiesFile, propertiesFile.getProperty(0));
			
			//convert boolean to 1 (true) or 0 (false)
			if (result.getResult() instanceof Boolean) {
				boolean booleanResult = (Boolean) result.getResult();				
				if (booleanResult) 
					resultQV = 1.0;
				else
					resultQV = 0.0;
			} 
			else
				resultQV = Double.parseDouble(result.getResult().toString());

			return resultQV;
		} 
		catch (Exception e) {
			e.printStackTrace();
			System.out.println("Model checking error");
			System.exit(-1);
			return -1.0;
		}
	}

		
	public double run (String modelString, String property) {
		loadModel(modelString);
		return evaluate(property);
	}
	
	
	/**
	 * Cleanup
	 */
	public void close(){
		if (mainLog!=null)
			mainLog.close();
		modulesFile = null;
		prism = null;
	}
	
	

}
