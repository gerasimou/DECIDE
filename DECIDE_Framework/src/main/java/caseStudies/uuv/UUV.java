package caseStudies.uuv;

import decide.capabilitySummary.CapabilitySummary;
import decide.component.Component;
import decide.component.requirements.Requirement;
import decide.component.requirements.RequirementType;
import decide.configuration.Configuration;
import decide.environment.Environment;

public class UUV extends Component {

	public UUV() {
		super();
		
		setupGlobalRequirements();
		setupLocalRequirements();  
	}
	
	
	@Override
	protected void setupGlobalRequirements() {
		//R1: The n UUVs should take at least 1000 measurements of sufficient accuracy per 60 seconds of mission time, used to 1000
		this.requirementsGlobalList.add(new Requirement(RequirementType.SYSTEM_REQUIREMENT, "R1", 1000.0) {
			@Override
			public Object evaluate(Environment environment, Configuration ... configs) {
				double totalMeasurements = 0;
				for (Configuration config : configs){
					Double configMeasurements = (Double) ((UUVConfiguration)config).getMeasurements();
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
			
			@Override
			public Object evaluate(Environment environment, CapabilitySummary ... capabilitySummaries) {
				double totalMeasurements = 0;
				for (CapabilitySummary capabilitySummary : capabilitySummaries){
					Double capabilitySummaryMeasurements = (Double) ((UUVCapabilitySummary)capabilitySummary).getMeasuremnets();
					totalMeasurements += capabilitySummaryMeasurements;
				}
				return totalMeasurements;
			}
		});

		
		//R2: At least two UUVs should have switched-on sensors at any time.
		this.requirementsGlobalList.add(new Requirement(RequirementType.SYSTEM_REQUIREMENT, "R2", 2.0) {
			@Override
			public Object evaluate(Environment environment, Configuration ... configs) {
				int uuvWithSensorsOn = 0;
				for (Configuration config : configs){
					if ( ((UUVConfiguration)config).areSensorsOn() )
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
				return (uuvWithSensorsOn >= (double)this.threshold ? true : false);
			}
			
			@Override
			public Object evaluate(Environment environment, CapabilitySummary ... capabilitySummaries) {
				int uuvWithSensorsOn = 0;
				for (CapabilitySummary capabilitySummary : capabilitySummaries){
					if ( ((UUVCapabilitySummary)capabilitySummary).areSensorsOn() )
						uuvWithSensorsOn++;
				}
				return uuvWithSensorsOn;
			}
			
		});

		
		//R3: If R1– R2 are satisfied by multiple configurations, the system should use one of these
		//configurations that minimises the energy consumption (so that the mission can continue for longer)
		this.requirementsGlobalList.add(new Requirement(RequirementType.SYSTEM_COST, "R3", null) {
			@Override
			public Object evaluate(Environment environment, Configuration ... configs) {
				double totalEnergy = 0;
				for (Configuration config : configs){
					Double configEnergy = (Double) ((UUVConfiguration)config).getEnergy();
					totalEnergy += configEnergy;
				}
				return totalEnergy;
			}
			
			@Override
			public Object evaluate(Environment environment, CapabilitySummary ... capabilitySummaries) {
				double totalEnergy = 0;
				for (CapabilitySummary capabilitySummary : capabilitySummaries){
					Double capabilitySummaryEnergy = (Double) ((UUVCapabilitySummary)capabilitySummary).getPowerConsumption();
					totalEnergy += capabilitySummaryEnergy;
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
		this.requirementsLocalList.add(new Requirement(RequirementType.COMPONENT_REQUIREMENT, "R4", 1200.0) {
			@Override
			public Object evaluate(Environment environment, Configuration ... configs) {
				double totalEnergy = 0;
				for (Configuration config : configs){
					Double configEnergy = (Double) ((UUVConfiguration)config).getEnergy();
					totalEnergy += configEnergy;
				}
				return totalEnergy;
			}
			
			@Override
			public Object evaluate(Environment environment, CapabilitySummary ... capabilitySummaries) {
				return null;
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

		
		//R5:The UUV should use only sensors whose measurements are accurate with probability at least 0.9
		this.requirementsLocalList.add(new Requirement(RequirementType.COMPONENT_REQUIREMENT, "R5",90) {
			@Override
			public Object evaluate(Environment environment, Configuration ... configs) {
				
				boolean result = ((UUVConfiguration)configs[0]).evaluateSensorAccuracy((int)this.threshold);
				return result;
			}
			
			@Override
			public Object evaluate(Environment environment, CapabilitySummary ... capabilitySummaries) {
				return null;
			}
			
			@Override
			public Object checkSatisfaction(Object... args) {
				
				return (boolean)args[0];
			}
		});

		
		//R6: If R1-R3 & R4 – R5 are satisfied by multiple configurations, the UUV should use one of these configurations 
		//that maximises its speed and minimises the local energy consumption , i.e., U = w_1 x e + w_2 x sp	
		this.requirementsLocalList.add(new Requirement(RequirementType.COMPONENT_COST, "R6", null) {
			@Override
			public Object evaluate(Environment environment, Configuration ... configs) {
				UUVConfiguration config = (UUVConfiguration)configs[0];
				return (1 * (double)config.getEnergy() + 100.0 / config.getSpeed());
			}
			
			@Override
			public Object evaluate(Environment environment, CapabilitySummary ... capabilitySummaries) {
				return null;
			}
			
			@Override
			public Object checkSatisfaction(Object... args) {
				return null;
			}
		});
	}

}