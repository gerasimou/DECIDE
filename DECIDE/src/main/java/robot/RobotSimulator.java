package robot;

import decide.component.Component;
import decide.component.requirements.Constraint;
import decide.component.requirements.Requirement;

public class RobotSimulator extends Component {

	public RobotSimulator() {
		super();
		
		setupGlobalRequirements();
		setupLocalRequirements();  
	}
	
	
	@Override
	protected void setupGlobalRequirements() {
		//R1: The n UUVs should take at least 600 measurements of sufficient accuracy per 60 seconds of mission time
		this.requirementsGlobalList.add(new Constraint("R1", 600) {
			@Override
			public Object evaluate(Object... args) {
				double totalMeasurements = 0;
				for (Object measuremenets : args){
					totalMeasurements += (double)measuremenets;
				}
				if (totalMeasurements >= this.threshold.doubleValue())
					return true;
				return false;
			}
		});

		//R2: At least two UUVs should have switched-on sensors at any time.
		this.requirementsGlobalList.add(new Requirement("R2") {
			@Override
			public Object evaluate(Object... args) {
				boolean uuv1ON	= (boolean)args[0];
				boolean uuv2ON	= (boolean)args[1];
				boolean uuv3ON	= (boolean)args[2];
				return ((uuv1ON && uuv2ON) || (uuv1ON && uuv3ON) || (uuv2ON && uuv3ON));
			}
		});

		//R3: If R1– R2 are satisfied by multiple configurations, the system should use one of these
		//configurations that minimises the energy consumption (so that the mission can continue for longer)
		this.requirementsGlobalList.add(new Requirement("R3") {
			@Override
			public Object evaluate(Object... args) {
				return null;
			}
		});
	}

	@Override
	protected void setupLocalRequirements() {
		//R4: The energy consumption of its sensors should not exceed x Joules per 60 seconds of mission time.
		this.requirementsLocalList.add(new Requirement("R4") {
			@Override
			public Object evaluate(Object... args) {
//				ResultDECIDE result 			= null;//(ResultQV)args[0];
//				double energyConsumption	= result.getReq2Result();
//				if (energyConsumption > 1000)
//					return false;
				return true;
			}
		});

		//R5:The UUV should use only sensors whose measurements are accurate with probability at least 0.9
		this.requirementsLocalList.add(new Requirement("R5") {
			@Override
			public Object evaluate(Object... args) {
				for (Object sensorReliability : args){
					if ((double)sensorReliability < 0.9)
						return false;
				}
				return true;
			}
		});

		//R6: If R1-R3 & R4 – R5 are satisfied by multiple configurations, the UUV should use one of these configurations 
		//that maximises its speed and minimises the local energy consumption , i.e., U = w_1 x e + w_2 x sp	
		
	}

}
