package caseStudies.healthcare;

import java.util.Arrays;
import java.util.List;

import decide.component.requirements.DECIDEAttributeCollection;
import decide.configuration.Configuration;


public class RobotConfiguration extends Configuration {
	
	private double pfull;	//probability of doing task 3 of room type 2 in full mode [0, 1)
	private int n1;			//number of rooms of type 1 assigned to this robot; to be determined by the selection algorithm (not part of the model)
	private int n2;			//number of rooms of type 2 assigned to this robot; to be determined by the selection algorithm (not part of the model)
	

	/**
	 * RobotConfiguration constructor
	 */
	public RobotConfiguration(DECIDEAttributeCollection attributeCollection, double probFull) {
		super (attributeCollection);

		//assign probabilities for this configuration
		this.pfull 	= probFull;	
	}

	
	@Override
	public String getConfigurationModel() {
		StringBuilder model = new StringBuilder("\n\n//Congifuration Variables\n");

    		//add configuration parameters
		model.append("const double rt2p3ifull = "  + pfull  +";\n");		
		
		return model.toString();
	}

	
	/**
	 * Get the configuration elements that affect the confidence (alpha value) on the environment
	 * If no configurable parameter has any effect on the confidence return null.
	 * For instance, in the UUV case study when two sensors (components) are active the conservative estimation
	 * of the environment parameter is affected analogously
	 */
	@Override
	public List<Object> getConfigurationElements() {
		return Arrays.asList(new Object[]{pfull});
	}
	
	
	public double getUtility() {
		return (double) localRequirementsResults.get("local-utility");//FIXME: Hardcoded for now
	}
	
	
	public double getP3Full(){
		return this.pfull;
	}

}
