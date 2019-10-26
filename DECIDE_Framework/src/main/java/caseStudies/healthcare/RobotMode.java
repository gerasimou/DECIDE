package caseStudies.healthcare;

import java.util.Map;

import decide.configuration.ConfigurationNew;
import decide.configuration.ModeNew;


public class RobotMode extends ModeNew {

	/**
	 * RobotMode constructor
	 */
	public RobotMode (String id) {
		super(id);

	}

	
	/**
	 * Find the best configuration for this mode
	 * The best configuration satisfies the local constraints and maximises the local objective (utility) 
	 */
	@Override
	public void findBestConfiguration() {
		double bestUtility 		= Double.MIN_NORMAL;
		bestConfigurationKey		= null;
		
		for (Map.Entry<String, ConfigurationNew> entry : configurationsMap.entrySet()){
			ConfigurationNew config = entry.getValue();
			
			//1) determine if the configuration satisfies *local* constraints
			boolean satisfied = config.evaluateLocalConstraints();
			
			//2) evaluate the *local* objectives for this configuration
			config.evaluateLocalObjectives();
			
			//3) if constraints are satisfied => get the local optimisation objective value (should be maximised)
			double utility = satisfied ? ((RobotConfiguration)config).getUtility() : Double.MIN_NORMAL;
			
			//4) if utility better than current best utility, make the update
			if (utility > bestUtility) {
				bestUtility = utility;
				bestConfigurationKey = entry.getKey();
			}
		}
	}

	
	/**
	 * Find the best configuration for this mode considering local constraints
	 * and my responsibilities
	 */
	@Override
	public void findBestConfigurationforLocalControl() {
		// TODO Auto-generated method stub
		
	}
}
