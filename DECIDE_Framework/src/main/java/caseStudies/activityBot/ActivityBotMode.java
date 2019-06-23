package caseStudies.activityBot;

import java.util.Map;

import caseStudies.uuv.UUVConfiguration;
import decide.component.requirements.RequirementSet;
import decide.configuration.Configuration;
import decide.configuration.Mode;
import decide.environment.Environment;

public class ActivityBotMode extends Mode{

	public ActivityBotMode() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void findBestConfiguration(Environment environment) {
		double bestBound = 0;
		//evaluate the configuration for this mode
		for (Map.Entry<String, Configuration> entry : configurationsMap.entrySet()){
				Configuration config = entry.getValue();
				
				//1) determine if the config satisfies requirements
				//config.evaluateGlobalRequirements(environment);
				//config.evaluateLocalRequirements(environment);
				
				//2) check whether the config satisfies local requirements
				//boolean reqsSatified = config.requirementsSatisfied(RequirementSet.LOCAL);
				
				//3) if true, get the utility bound (attribute analysis 2)
				double bound = config.getBound();
				
				//boolean reqsSatified = config.requirementsSatisfied(RequirementSet.LOCAL);
				
				//double bound = config.getBound();
				if (bound >= bestBound){
					bestBound 				= bound;
					bestConfigurationKey	= entry.getKey();
				}
		}
	}
	@Override
	public void findBestConfigurationforLocalControl(Environment environment) {
		double bestBound = Double.MAX_VALUE;
		// reset mode best Configuration Key 
		bestConfigurationKey = null;
		//evaluate the configuration for this mode
		for (Map.Entry<String, Configuration> entry : configurationsMap.entrySet()){
				Configuration config = entry.getValue();
				
				//1) determine if the config satisfies requirements
				config.evaluateGlobalRequirements(environment);
				config.evaluateLocalRequirements(environment);
				
				//2) check whether the config satisfies local requirements
				boolean reqsSatified = config.requirementsSatisfied(RequirementSet.ALL);
				
				if(reqsSatified)
				{
				//3) if true, get the bound (attribute analysis 2)
				double bound = (double) ((ActivityBotConfiguration)config).getCost();
				
				//boolean reqsSatified = config.requirementsSatisfied(RequirementSet.LOCAL);
				
				//double bound = config.getBound();
				if (bound <= bestBound){
					bestBound 				= bound;
					bestConfigurationKey	= entry.getKey();
				}
				}
		}
	}

}