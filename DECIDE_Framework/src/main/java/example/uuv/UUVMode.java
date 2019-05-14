package example.uuv;

import java.util.Map;

import decide.component.requirements.RequirementSet;
import decide.configuration.Configuration;
import decide.configuration.Mode;
import decide.environment.Environment;

public class UUVMode extends Mode {

	public UUVMode() {
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
				config.evaluateLocalRequirements(environment);
				
				//2) check whether the config satisfies local requirements
				boolean reqsSatified = config.requirementsSatisfied(RequirementSet.LOCAL);
				
				//3) if true, get the bound (attribute analysis 2)
				double bound = reqsSatified ? config.getBound() : 0.0;
				
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
		double bestBound = 0;
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
				double bound = ((UUVConfiguration)config).getLocalBound();
				
				//boolean reqsSatified = config.requirementsSatisfied(RequirementSet.LOCAL);
				
				//double bound = config.getBound();
				if (bound >= bestBound){
					bestBound 				= bound;
					bestConfigurationKey	= entry.getKey();
				}
				}
		}
	}

}
