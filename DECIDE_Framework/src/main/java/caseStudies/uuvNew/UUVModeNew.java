package caseStudies.uuvNew;

import java.util.Map;

import decide.configuration.ConfigurationNew;
import decide.configuration.ModeNew;

public class UUVModeNew extends ModeNew {

	public UUVModeNew(String id) {
		super(id);
	}

	
	/**
	 * Find the best configuration for this mode
	 * The best configuration satisfies the local constraints and maximises the local objective (utility) 
	 */
	@Override
	public void findBestConfiguration() {
		double bestUtility 		= Double.MIN_NORMAL;
		bestConfigurationKey	= null;

		for (Map.Entry<String, ConfigurationNew> entry : configurationsMap.entrySet()){
			ConfigurationNew config = entry.getValue();
			
			//1) determine if the configuration satisfies *local* constraints
			boolean satisfied = config.evaluateLocalConstraints();

			//2) evaluate the *local* objectives for this configuration
			config.evaluateLocalObjectives();
			
			//3) if constraints are satisfied => get the local optimisation objective value (should be maximised)
			double utility = satisfied ? ((UUVConfigurationNew)config).getUtility() : Double.MIN_NORMAL;

			//4) if utility better than current best utility, make the update
			if (utility > bestUtility) {
				bestUtility = utility;
				bestConfigurationKey = entry.getKey();
			}
		}
	}

	@Override
	public void findBestConfigurationforLocalControl() {
		double bestUtility 		= Double.MAX_VALUE;
		bestConfigurationKey	= null;

		for (Map.Entry<String, ConfigurationNew> entry : configurationsMap.entrySet()){
			ConfigurationNew config = entry.getValue();
			
			//0) determine if the configuration satisfies the component's responsibilities
			boolean satisfiedResponsibilities = config.evaluateResponsibilities();
			
			//1) determine if the configuration satisfies *local* constraints
			boolean satisfied = config.evaluateLocalConstraints();

			//2) evaluate the *local* objectives for this configuration
			config.evaluateLocalObjectives();
			
			//3) if constraints are satisfied => get the local optimisation objective value (should be maximised)
			double utility = satisfied && satisfiedResponsibilities ? ((UUVConfigurationNew)config).getUtility() : Double.MAX_VALUE;

			//4) if utility better than current best utility, make the update
			if (utility < bestUtility) {
				bestUtility 		 = utility;
				bestConfigurationKey = entry.getKey();
			}
		}
	}

}
