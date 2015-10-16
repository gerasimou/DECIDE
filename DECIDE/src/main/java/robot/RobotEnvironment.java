package robot;

import java.util.Map;

import decide.environment.Environment;

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
}
