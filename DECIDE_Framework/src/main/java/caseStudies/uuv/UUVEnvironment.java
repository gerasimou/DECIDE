package caseStudies.uuv;

import java.util.List;
import java.util.Map;

import auxiliary.Utility;
import decide.component.requirements.DECIDEAttribute;
import decide.configuration.Configuration;
import decide.environment.Environment;
import decide.localAnalysis.LocalCapabilityAnalysis;

public class UUVEnvironment extends Environment {

	public UUVEnvironment() {
		//Nothing to do
	}

	@Override
	public String getEnvironmentModel() {
		// TODO Auto-generated method stub
		StringBuilder envModelParams = new StringBuilder("\n\n//Environment Variables\n");
		
		for (Map.Entry<String, Object> entry : environmentMap.entrySet()){
			envModelParams.append("const double " + entry.getKey() +" = " + entry.getValue() +";\n");
		}

		//return the model
		return envModelParams.toString();
	}

	
	@Override
	protected void adjustEnvironment(Configuration configuration, DECIDEAttribute attribute) {
		List<Object> configurationElements = configuration.getConfigurationElements();
		
		int CSC 					= (int)configurationElements.get(0);

		for (Map.Entry<String, Object> entry : environmentMap.entrySet()){
			double environmentParam 	= Double.parseDouble(entry.getValue().toString());
			double stDeviation = 0.3;

			double confidenceValue = -1;
			if (CSC==1 || CSC==2 || CSC==4 || CSC==0)
				confidenceValue = LocalCapabilityAnalysis.getConfidenceValue("1");
			else if (CSC==3 || CSC==5 || CSC==6)
				confidenceValue = LocalCapabilityAnalysis.getConfidenceValue("2");
			else if (CSC==7)
				confidenceValue = LocalCapabilityAnalysis.getConfidenceValue("3");
			else 
				throw new IllegalArgumentException("Current sensor configuration outside boundaries");
			

			if (attribute.getProperty().contains("measurement")) 
				environmentMap.put(entry.getKey(), Math.max(0.1, environmentParam - confidenceValue * stDeviation));
			else if (attribute.getProperty().contains("energy"))
				environmentMap.put(entry.getKey(), Math.max(0.1, environmentParam + confidenceValue * stDeviation));
			else 
				throw new IllegalArgumentException("attribute not recognised");
		}		
	}

	
	@Override
	protected void initEnvironment() {
		environmentMap.put("r1", Double.parseDouble(Utility.getProperty("r1")));
		environmentMap.put("r2", Double.parseDouble(Utility.getProperty("r2")));
		environmentMap.put("r3", Double.parseDouble(Utility.getProperty("r3")));
	}

}
