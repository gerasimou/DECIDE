package caseStudies.healthcare;

import auxiliary.Utility;
import decide.configuration.Configuration;
import decide.environment.Environment;
import decide.localAnalysis.LocalCapabilityAnalysis;

public class RobotEnvironment extends Environment {

	public RobotEnvironment() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getModel() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void adjustEnvironment(Configuration configuration, int property) {
		double stDeviation = 0.1;
		double p2iretryValue = (double) environmentMap.get("p2iretry");
		double confidenceValue = LocalCapabilityAnalysis.getConfidenceValue("1");
		environmentMap.put("p2iretry", Math.max(0.1, p2iretryValue + confidenceValue * stDeviation));
	}

	@Override
	protected void initEnvironment() {
		environmentMap.put("p2iretry", Double.parseDouble(Utility.getProperty("p2i_retry")));
		environmentMap.put("speedi",   Double.parseDouble(Utility.getProperty("speed_i")));
		environmentMap.put("avTasks",  Utility.getProperty("avTasks"));
		environmentMap.put("trapped",  Boolean.parseBoolean(Utility.getProperty("trapped")));
	}

}
