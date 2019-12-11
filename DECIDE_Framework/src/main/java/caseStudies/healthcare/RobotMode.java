package caseStudies.healthcare;

import java.util.Map;

import caseStudies.uuv.UUVConfiguration;
import decide.configuration.Configuration;
import decide.configuration.Mode;


public class RobotMode extends Mode {

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
//		double bestUtility 		= Double.MIN_NORMAL;
		bestConfigurationKey		= null;

		for (Map.Entry<String, Configuration> entry : configurationsMap.entrySet()){
			Configuration config = entry.getValue();
			
			//1) determine if the configuration satisfies *local* constraints
			boolean satisfied = config.evaluateLocalConstraints();
			
//			//2) evaluate the *local* objectives for this configuration
//			config.evaluateLocalObjectives();
//			
//			//3) if constraints are satisfied => get the local optimisation objective value (should be maximised)
//			double utility = satisfied ? ((RobotConfiguration)config).getUtility() : Double.MIN_NORMAL;
//			
//			//4) if utility better than current best utility, make the update
//			if (utility > bestUtility) {
//				bestUtility = utility;
//				bestConfigurationKey = entry.getKey();
//			}
			if (satisfied) {
				bestConfigurationKey = entry.getKey();
				return;
			}
		}
	}

	
	/**
	 * Find the best configuration for this mode considering local constraints
	 * and my responsibilities
	 */
	@Override
	public void findBestConfigurationforLocalControl() {
		double bestUtility 		= -100;//Double.MIN_VALUE;
		bestConfigurationKey	= null;

		for (Map.Entry<String, Configuration> entry : configurationsMap.entrySet()){
			Configuration config = entry.getValue();
			
			//0) determine if the configuration satisfies the component's responsibilities
			boolean satisfiedResponsibilities = config.evaluateResponsibilities();
			
			//1) determine if the configuration satisfies *local* constraints
			boolean satisfied = config.evaluateLocalConstraints();

			//2) evaluate the *local* objectives for this configuration
			config.evaluateLocalObjectives();

			//3) if constraints are satisfied => get the local optimisation objective value (should be maximised)
			double utility = satisfied && satisfiedResponsibilities ? ((RobotConfiguration)config).getUtility() : Double.MIN_VALUE;

			//4) if utility better than current best utility, make the update
			if (utility > bestUtility) {
				bestUtility 		 = utility;
				bestConfigurationKey = entry.getKey();
			}
		
		}		
	}
}
