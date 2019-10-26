package caseStudies.uuvNew;

import java.util.ArrayList;
import java.util.List;

import decide.capabilitySummary.CapabilitySummaryNew;
import decide.component.requirements.RequirementType;
import decide.component.requirements.reqNew.GlobalConstraintNew;
import decide.component.requirements.reqNew.LocalConstraintNew;
import decide.component.requirements.reqNew.LocalObjectiveNew;
import decide.configuration.ConfigurationNew;


public class UUVRequirements {

	private List<GlobalConstraintNew> globalConstraints;
	private List<LocalConstraintNew> localConstraints;
	private List<LocalObjectiveNew> localObjectives;
	
	
	public UUVRequirements() {
		localConstraints = new ArrayList<>();
		initLocalConstraints();

		localObjectives= new ArrayList<>();
		initLocalObjectives();

		globalConstraints = new ArrayList<>();
		initGlobalConstraints();
		
	}
	
	
	private void initLocalConstraints() {
		//Local Constraint 1 - R4: The energy consumption of its sensors should not exceed x Joules per 60 seconds of mission time.
		double MAX_ENERGY = 1200;
		LocalConstraintNew localConst1 = new LocalConstraintNew(RequirementType.LOCAL_CONSTRAINT, "energy", MAX_ENERGY) {
			@Override
			public boolean isSatisfied(ConfigurationNew configuration) {
				return (double) configuration.getVerificationResult("energy") <= (double)this.getThreshold();
//				DECIDEAttribute attribute = configuration.getAttributeByName("attr2");
//				return (double)attribute.getVerificationResult() <= (double)this.getThreshold();
			}

			@Override
			public Number evaluate (ConfigurationNew configuration) {
				return (double) configuration.getVerificationResult("energy");
//				DECIDEAttribute attribute = configuration.getAttributeByName("attr2");
//				return (double)attribute.getVerificationResult();
			}
		}; 
		
		
		//Local Constraint 2 - R5:The UUV should use only sensors whose measurements are accurate with probability at least 0.9
		double MIN_ACCURACY = 0.9;
		LocalConstraintNew localConst2 = new LocalConstraintNew(RequirementType.LOCAL_CONSTRAINT, "sensor-accuracy", MIN_ACCURACY) {
			@Override
			public boolean isSatisfied(ConfigurationNew configuration) {
				return ((UUVConfigurationNew)configuration).evaluateSensorAccuracy((double)this.getThreshold());
			}

			@Override
			public Number evaluate (ConfigurationNew configuration) {
				return ((UUVConfigurationNew)configuration).getSensorAccuracy();
			}
		}; 
		
		localConstraints.add(localConst1);
		localConstraints.add(localConst2);
	}
	
	
	private void initLocalObjectives() {
		//Local Objective 1: Maximise local utility
		//R6: If R1-R3 & R4 – R5 are satisfied by multiple configurations, the UUV should use one of these configurations 
		//that maximises its speed and minimises the local energy consumption , i.e., U = w_1 x e + w_2 x sp	
		
		LocalObjectiveNew localObj1 = new LocalObjectiveNew(RequirementType.COMPONENT_OBJECTIVE, "local-utility", true) {
			@Override
			public Object evaluate(ConfigurationNew configuration) {
//				DECIDEAttribute attribute = configuration.getAttributeByName("attr2");
				double speed = ((UUVConfigurationNew)configuration).getSpeed();
				double attr2VerResult = (double) configuration.getVerificationResult("energy");
				return (1 * attr2VerResult + 100.0 / speed);
			}
		};						
		localObjectives.add(localObj1);
	}
	
	
	private void initGlobalConstraints() {
		//Global Constraint 1
		//R1: The n UUVs should take at least 1000 measurements of sufficient accuracy per 60 seconds of mission time, used to 1000
		final double MIN_MEASUREMENTS = 1000;
		GlobalConstraintNew constraint1 = new GlobalConstraintNew(RequirementType.GLOBAL_CONSTRAINT, "measurements", MIN_MEASUREMENTS) {

			@Override
			public Number evaluate(List<CapabilitySummaryNew> capabilitySummaries) {
				double totalMeasurements = 0;
				for (CapabilitySummaryNew cs : capabilitySummaries) {
					totalMeasurements  += (double) cs.getCapabilitySummaryElement("measurements");
				}
				return totalMeasurements;
			}

			@Override
			public boolean isSatisfied(List<CapabilitySummaryNew> capabilitySummaries) {
				double totalMeasurements = 0;
				for (CapabilitySummaryNew cs : capabilitySummaries) {
					totalMeasurements  += (double) cs.getCapabilitySummaryElement("measurements");
				}
				return totalMeasurements >= (double)this.getThreshold();
			};		
		};	
		
		
		//Global Constraint 2
		//R2: At least two UUVs should have switched-on sensors at any time.
		final int ACTIVE_UUVS = 2;
		GlobalConstraintNew constraint2 = new GlobalConstraintNew(RequirementType.GLOBAL_CONSTRAINT, "active-uuvs", ACTIVE_UUVS) {

			@Override
			public Number evaluate(List<CapabilitySummaryNew> capabilitySummaries) {
				int uuvWithSensorsOn = 0;
				for (CapabilitySummaryNew cs : capabilitySummaries) {
					uuvWithSensorsOn  += (int) cs.getCapabilitySummaryElement("csc") > 0 ? 1 : 0;
				} 
				return uuvWithSensorsOn;
			};		
			
			@Override
			public boolean isSatisfied(List<CapabilitySummaryNew> capabilitySummaries) {
				int uuvWithSensorsOn = 0;
				for (CapabilitySummaryNew cs : capabilitySummaries) {
					uuvWithSensorsOn  += (int) cs.getCapabilitySummaryElement("csc") > 0 ? 1 : 0;
				} 
				return uuvWithSensorsOn > (int)this.getThreshold();
			};		

		};	

		globalConstraints.add(constraint1);
		globalConstraints.add(constraint2);
	}

}
