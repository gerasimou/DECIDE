package caseStudies.healthcare;

import java.util.List;
import decide.configuration.ConfigurationNew;
import java.util.Arrays;


public class RobotConfiguration extends ConfigurationNew {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private double pfull;	//probability of doing task 3 of room type 2 in full mode [0, 1)
	private int n1;			//number of rooms of type 1 assigned to this robot; to be determined by the selection algorithm (not part of the model)
	private int n2;			//number of rooms of type 2 assigned to this robot; to be determined by the selection algorithm (not part of the model)
	

	/**
	 * RobotConfiguration constructor
	 */
	public RobotConfiguration(double probFull) {
		//assign probabilities for this configuration
		this.pfull 	= probFull;	
	}

	
	@Override
	public String getModel() {
		StringBuilder model = new StringBuilder("\n\n//Congifuration Variables\n");

    		//add configuration parameters
                model.append("const double p3ifull = ").append(pfull).append(";\n");		
		model.append("const double n1  = ").append(n1).append(";\n");
		model.append("const double n2  = ").append(n2).append(";\n\n");
		
		return model.toString();
	}

	
	/**
	 * Get the configuration elements that affect the confidence (alpha value) on the environment
	 * If no configurable parameter has any effect on the confidence return null.
	 * For instance, in the UUV case study when two sensors (components) are active the conservative estimation
	 * of the environment parameter is affected analogously
     * @return 
	 */
	@Override
	public List<Object> getConfigurationElements() {
		return Arrays.asList(new Object[]{pfull, n1, n2});
	}
	
	
	public double getUtility() {
		return (double) localRequirementsResults.get("local-utility");//FIXME: Hardcoded for now
	}
	
        public double getPfull(){
		return this.pfull;
	}
	
}
