package caseStudies.uuv;

import java.util.List;

import decide.capabilitySummary.CapabilitySummary;
import decide.component.Component;
import decide.component.requirements.RequirementType;
import decide.component.requirements.reqNew.GlobalConstraintNew;
import decide.component.requirements.reqNew.GlobalObjectiveNew;
import decide.component.requirements.reqNew.LocalConstraint;
import decide.component.requirements.reqNew.LocalObjective;
import decide.configuration.Configuration;

public class UUV extends Component {

	public UUV() {
		super();
	}

	
	@Override
	protected void setupGlobalRequirements() {
		//Global Constraint 1
		//R1: The n UUVs should take at least 1000 measurements of sufficient accuracy per 60 seconds of mission time, used to 1000
		final double MIN_MEASUREMENTS = 1000;
		GlobalConstraintNew constraint1 = new GlobalConstraintNew(RequirementType.GLOBAL_CONSTRAINT, "measurements", MIN_MEASUREMENTS) {

			@Override
			public Number evaluate(List<CapabilitySummary> capabilitySummaries) {
				double totalMeasurements = 0;
				for (CapabilitySummary cs : capabilitySummaries) {
					totalMeasurements  += (double) cs.getCapabilitySummaryElement("measurements");
				}
				return totalMeasurements;
			}

			@Override
			public boolean isSatisfied(List<CapabilitySummary> capabilitySummaries) {
				double totalMeasurements = 0;
				for (CapabilitySummary cs : capabilitySummaries) {
					totalMeasurements  += (double) cs.getCapabilitySummaryElement("measurements");
				}
				return totalMeasurements >= (double)this.getThreshold();
			};		
		};	
		
		
		//Global Constraint 2
		//R2: At least two UUVs should have switched-on sensors at any time.
		final int ACTIVE_UUVS = 1;
		GlobalConstraintNew constraint2 = new GlobalConstraintNew(RequirementType.GLOBAL_CONSTRAINT, "active-uuvs", ACTIVE_UUVS) {

			@Override
			public Number evaluate(List<CapabilitySummary> capabilitySummaries) {
				int uuvWithSensorsOn = 0;
				for (CapabilitySummary cs : capabilitySummaries) {
					uuvWithSensorsOn  += (int) cs.getCapabilitySummaryElement("csc") > 0 ? 1 : 0;
				} 
				return uuvWithSensorsOn;
			};		
			
			@Override
			public boolean isSatisfied(List<CapabilitySummary> capabilitySummaries) {
				int uuvWithSensorsOn = 0;
				for (CapabilitySummary cs : capabilitySummaries) {
					uuvWithSensorsOn  += (int) cs.getCapabilitySummaryElement("csc") > 0 ? 1 : 0;
				} 
				return uuvWithSensorsOn >= (int)this.getThreshold();
			};		

		};	
		
		
		//Global Objective 1
		//R3: If R1– R2 are satisfied by multiple configurations, the system should use one of these
		//configurations that minimises the energy consumption (so that the mission can continue for longer)
		GlobalObjectiveNew objective1 = new GlobalObjectiveNew(RequirementType.SYSTEM_OBJECTIVE, "system-energy", false) {
			@Override
			public Object evaluate(List<CapabilitySummary> capabilitySummaries) {
				double totalEnergy = 0;
				for (CapabilitySummary cs : capabilitySummaries) {
					totalEnergy  += (double) cs.getCapabilitySummaryElement("energy");
				}
				return totalEnergy;
			}
		};

		requirementsGlobalList.add(constraint1);
		requirementsGlobalList.add(constraint2);
		requirementsGlobalList.add(objective1);
	}
	

	@Override
	protected void setupLocalRequirements() {
		//Local Constraint 1 - R4: The energy consumption of its sensors should not exceed x Joules per 60 seconds of mission time.
		double MAX_ENERGY = 1000;
		LocalConstraint localConst1 = new LocalConstraint(RequirementType.LOCAL_CONSTRAINT, "energy", MAX_ENERGY) {
			@Override
			public boolean isSatisfied(Configuration configuration) {
				return (double) configuration.getVerificationResult("measurements") <= (double)this.getThreshold();
//				DECIDEAttribute attribute = configuration. getAttributeByName("attr1");
//				return (double)attribute.getVerificationResult() <= (double)this.getThreshold();
			}

			@Override
			public Number evaluate (Configuration configuration) {
				return (double)configuration.getVerificationResult("energy");
//				DECIDEAttribute attribute = configuration.getAttributeByName("attr1");
//				return (double)attribute.getVerificationResult();
			}
		}; 
		
		
		//Local Constraint 2 - R5:The UUV should use only sensors whose measurements are accurate with probability at least 0.9
		double MIN_ACCURACY = 0.9;
		LocalConstraint localConst2 = new LocalConstraint(RequirementType.LOCAL_CONSTRAINT, "sensor-accuracy", MIN_ACCURACY) {
			@Override
			public boolean isSatisfied(Configuration configuration) {
				return ((UUVConfiguration)configuration).evaluateSensorAccuracy((double)this.getThreshold());
			}

			@Override
			public Number evaluate (Configuration configuration) {
				return ((UUVConfiguration)configuration).getSensorAccuracy();
			}
		}; 
		
		
		//Local Objective 1: Maximise local utility
		//R6: If R1-R3 & R4 – R5 are satisfied by multiple configurations, the UUV should use one of these configurations 
		//that maximises its speed and minimises the local energy consumption , i.e., U = w_1 x e + w_2 x sp	
		
		LocalObjective localObj1 = new LocalObjective(RequirementType.COMPONENT_OBJECTIVE, "local-utility", true) {
			@Override
			public Object evaluate(Configuration configuration) {
//				DECIDEAttribute attribute = configuration.getAttributeByName("attr1");
				double speed = ((UUVConfiguration)configuration).getSpeed();
				double attr1VerResult = (double)configuration.getVerificationResult("energy");
				return (1 * attr1VerResult + 100.0 / speed);
			}
		};						
		
		requirementsLocalList.add(localConst1);
		requirementsLocalList.add(localConst2);
		requirementsLocalList.add(localObj1);
	}

}
