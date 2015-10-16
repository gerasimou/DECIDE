package robot;

import java.util.Map;

import decide.environment.Environment;
import decide.localAnalysis.LocalCapabilityAnalysis;

public class RobotEnvironment extends Environment {

	public RobotEnvironment() {
		initRobotEnvironment();
	}
	
	
	private void initRobotEnvironment(){
		environmentMap.put("r1", "5");
		environmentMap.put("r2", "4");
		environmentMap.put("r3", "4");
	}
	

	@Override
	public String getModel() {
		StringBuilder model = new StringBuilder("\n\n//Environment Variables\n");

		for (Map.Entry<String, Object> entry : environmentMap.entrySet()){
			model.append("const double " + entry.getKey() +" = " + entry.getValue() +";\n");
		}
		return model.toString();
	}	
	

	@Override
	protected double estimateEnvironment(Object ... args){
		int CSC 			= Integer.parseInt(args[0].toString());
		int propertyNum		= Integer.parseInt(args[1].toString());
		double sensorRate 	= Double.parseDouble(args[2].toString());
		
		double stDeviation = 0.3;
		
		double confidenceValue = -1;
		if (CSC==1 || CSC==2 || CSC==4){
			confidenceValue = LocalCapabilityAnalysis.getConfidenceValue("1");
		}
		else if (CSC==3 || CSC==5 || CSC==6){
			confidenceValue = LocalCapabilityAnalysis.getConfidenceValue("2");
		}
		else if (CSC==7){
			confidenceValue = LocalCapabilityAnalysis.getConfidenceValue("3");
		}
		else 
			throw new IllegalArgumentException("Current sensor configuration outside boundaries");
		
		
		if (propertyNum==0){
			return Math.max(0.1, sensorRate - confidenceValue * stDeviation);
		}
		else if (propertyNum==1){
			return Math.max(0.1, sensorRate + confidenceValue * stDeviation);			
		}
		else 
			throw new IllegalArgumentException("Property index outside boundaries");
	}

}
