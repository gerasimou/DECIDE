package robot;

import decide.component.Component;
import decide.component.requirements.Requirement;
import decide.component.requirements.RequirementType;
import decide.configuration.Configuration;
import decide.environment.Environment;

public class RobotSimulator extends Component {

	public RobotSimulator() {
		super();
		
		setupGlobalRequirements();
		setupLocalRequirements();  
	}
	
	
	@Override
	protected void setupGlobalRequirements() {
		//R1: The n UUVs should take at least 600 measurements of sufficient accuracy per 60 seconds of mission time
		this.requirementsGlobalList.add(new Requirement(RequirementType.SYSTEM_REQUIREMENT, "R1", 600.0) {
			@Override
			public Object evaluate(Environment environment, Configuration ... configs) {
				double totalMeasurements = 0;
				for (Configuration config : configs){
					Double configMeasurements = (Double) ((RobotConfiguration)config).getMeasurements();
					totalMeasurements += configMeasurements;
				}
				return totalMeasurements;
			}

			@Override
			public Object checkSatisfaction(Object... args) {
				double totalMeasurements = 0;
				for (Object obj : args){
					totalMeasurements += (double)obj;
				}
				return (totalMeasurements >= (double)this.threshold ? true : false);
			}
		});

		
		//R2: At least two UUVs should have switched-on sensors at any time.
		this.requirementsGlobalList.add(new Requirement(RequirementType.SYSTEM_REQUIREMENT, "R2", null) {
			@Override
			public Object evaluate(Environment environment, Configuration ... configs) {
				int uuvWithSensorsOn = 0;
				for (Configuration config : configs){
					if ( ((RobotConfiguration)config).areSensorsOn() )
						uuvWithSensorsOn++;
				}
				return uuvWithSensorsOn;
			}

			@Override
			public Object checkSatisfaction(Object... args) {
				int uuvWithSensorsOn = 0;
				for (Object obj : args){
					uuvWithSensorsOn += (int)obj;
				}
				return (uuvWithSensorsOn >= 2 ? true : false);
			}
		});

		
		//R3: If R1– R2 are satisfied by multiple configurations, the system should use one of these
		//configurations that minimises the energy consumption (so that the mission can continue for longer)
		this.requirementsGlobalList.add(new Requirement(RequirementType.SYSTEM_COST, "R3", null) {
			@Override
			public Object evaluate(Environment environment, Configuration ... configs) {
				double totalEnergy = 0;
				for (Configuration config : configs){
					Double configEnergy = (Double) ((RobotConfiguration)config).getEnergy();
					totalEnergy += configEnergy;
				}
				return totalEnergy;
			}

			@Override
			public Object checkSatisfaction(Object... args) {
				return null;
			}
		});
	}

	
	
	@Override
	protected void setupLocalRequirements() {
		//R4: The energy consumption of its sensors should not exceed x Joules per 60 seconds of mission time.
		this.requirementsLocalList.add(new Requirement(RequirementType.COMPONENT_REQUIREMENT, "R4", 1000.0) {
			@Override
			public Object evaluate(Environment environment, Configuration ... configs) {
				double totalEnergy = 0;
				for (Configuration config : configs){
					Double configEnergy = (Double) ((RobotConfiguration)config).getEnergy();
					totalEnergy += configEnergy;
				}
				return totalEnergy;
			}
			
			@Override
			public Object checkSatisfaction(Object... args) {
				double totalEnergy = 0;
				for (Object obj : args){
					totalEnergy += (double) obj;
				}
				return (totalEnergy <= (double)this.threshold ? true : false);
			}
		});

		
//		//R5:The UUV should use only sensors whose measurements are accurate with probability at least 0.9
//		this.requirementsLocalList.add(new Requirement(RequirementType.LOWER_BOUNDED, "R5") {
//			@Override
//			public Object evaluate(Object... args) {
//				for (Object sensorReliability : args){
//					if ((double)sensorReliability < 0.9)
//						return false;
//				}
//				return true;
//			}
//		});

		
		//R6: If R1-R3 & R4 – R5 are satisfied by multiple configurations, the UUV should use one of these configurations 
		//that maximises its speed and minimises the local energy consumption , i.e., U = w_1 x e + w_2 x sp	
		this.requirementsLocalList.add(new Requirement(RequirementType.COMPONENT_COST, "R6", null) {
			@Override
			public Object evaluate(Environment environment, Configuration ... configs) {
				RobotConfiguration config = (RobotConfiguration)configs[0];
				return (1 * (double)config.getEnergy() + 100.0 / config.getSpeed());
			}
			
			@Override
			public Object checkSatisfaction(Object... args) {
				return null;
			}
		});
	}

}
