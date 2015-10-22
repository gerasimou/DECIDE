package robot;

import java.util.List;
import java.util.Map;

import auxiliary.Utility;
import decide.configuration.Configuration;
import decide.environment.Environment;
import decide.localAnalysis.LocalCapabilityAnalysis;

public class RobotEnvironment extends Environment {

	public RobotEnvironment() {
		initRobotEnvironment();
	}
	
	
	private void initRobotEnvironment(){
		environmentMap.put("r1", Utility.getProperty("r1"));
		environmentMap.put("r2", Utility.getProperty("r2"));
		environmentMap.put("r3", Utility.getProperty("r3"));
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
	protected void adjustEnvironment(Configuration configuration, int property){
		
		@SuppressWarnings("unchecked")
		List<Object> configurationElements = (List<Object>) configuration.getConfigurationElements();
				
		int CSC 					= (int)configurationElements.get(0);
		
		
		for (Map.Entry<String, Object> entry : environmentMap.entrySet()){
			
			double environmentParam 	= Double.parseDouble(entry.getValue().toString());
			
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
			
			
			if (property==0){
				environmentMap.put(entry.getKey(), Math.max(0.1, environmentParam - confidenceValue * stDeviation));
			}
			else if (property==1){
				environmentMap.put(entry.getKey(), Math.max(0.1, environmentParam + confidenceValue * stDeviation));
			}
			else 
				throw new IllegalArgumentException("Property index outside boundaries");
		}
	}

}
