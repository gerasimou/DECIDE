package caseStudies.healthcare;


import auxiliary.Utility;
import decide.component.requirements.DECIDEAttribute;
import decide.configuration.Configuration;
import decide.environment.Environment;
import decide.localAnalysis.LocalCapabilityAnalysis;


public class RobotEnvironment extends Environment {

	public RobotEnvironment() {
		//Nothing to do here
	}

	
	@Override
	public String getEnvironmentModel() {
		StringBuilder envModelParams = new StringBuilder("\n\n//Environment Variables\n");
		
		//append p2iretry command with its value
		String key 		= "p2iretry";
		Double value	= (double)environmentMap.get(key);
		envModelParams.append("const double " + key +" = " + value +";\n");

		//append v_i command with its value
		String keyV 	= "v_i";
		Double valueV	= (double)environmentMap.get(keyV);
		envModelParams.append("const double " + keyV +" = " + valueV +";\n");

		//append d_i command with its value
		String keyD 	= "d_i";
		Double valueD	= (double)environmentMap.get(keyV);
		envModelParams.append("const double " + keyD +" = " + valueD +";\n");

		
		//return the model
		return envModelParams.toString();
	}

	
	@Override
	protected void adjustEnvironment (Configuration configuration, DECIDEAttribute attribute) {
		double stDeviation = 0.1;
		double p2iretryValue = (double) environmentMap.get("p2iretry");
		double confidenceValue = LocalCapabilityAnalysis.getConfidenceValue("1");
		environmentMap.put("p2iretry", Math.max(0.1, p2iretryValue + confidenceValue * stDeviation));
	}

	
	@Override
	protected void initEnvironment() {
		environmentMap.put("p2iretry", Double.parseDouble(Utility.getProperty("p2i_retry")));
		environmentMap.put("v_i",   Double.parseDouble(Utility.getProperty("v_i")));
		environmentMap.put("d_i",   Double.parseDouble(Utility.getProperty("d_i")));
		environmentMap.put("avTasks",  Utility.getProperty("avTasks").split(","));
		environmentMap.put("trapped",  Boolean.parseBoolean(Utility.getProperty("trapped")));
	}

}
