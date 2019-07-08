package caseStudies.healthcare;

import java.util.ArrayList;
import java.util.List;

import decide.capabilitySummary.CapabilitySummaryNew;
import decide.component.requirements.DECIDEAttribute;
import decide.component.requirements.RequirementType;
import decide.component.requirements.reqNew.GlobalConstraintNew;
import decide.component.requirements.reqNew.LocalConstraintNew;
import decide.component.requirements.reqNew.LocalObjectiveNew;
import decide.configuration.ConfigurationNew;


public class RobotRequirements {

	private List<GlobalConstraintNew> globalConstraints;
	private List<LocalConstraintNew> localConstraints;
	private List<LocalObjectiveNew> localObjectives;
	
	
	public RobotRequirements() {
		localConstraints = new ArrayList<>();
		initLocalConstraints();

		localObjectives= new ArrayList<>();
		initLocalObjectives();

		globalConstraints = new ArrayList<>();
		initGlobalConstraints();
		
	}
	
	
	private void initLocalConstraints() {
		//Local Constraint 1: Time left is less than the time required
		double TLEFT = 100;
		LocalConstraintNew localConst1 = new LocalConstraintNew(RequirementType.LOCAL_CONSTRAINT, "time-left", TLEFT) {
			@Override
			public boolean isSatisfied(ConfigurationNew configuration) {
				DECIDEAttribute attribute = configuration.getAttributeByName("attr1");
				return (double)attribute.getVerificationResult() > (double)this.getThreshold();
			}

			@Override
			public Number evaluate (ConfigurationNew configuration) {
				DECIDEAttribute attribute = configuration.getAttributeByName("attr1");
				return (double)attribute.getVerificationResult();
			}
		}; 
		
		localConstraints.add(localConst1);
	}
	
	
	private void initLocalObjectives() {
		//Local Objective 1: Maximise local utility
		LocalObjectiveNew localObj1 = new LocalObjectiveNew(RequirementType.COMPONENT_OBJECTIVE, "local-utility", true) {
			@Override
			public Object evaluate(ConfigurationNew configuration) {
				DECIDEAttribute attribute = configuration.getAttributeByName("attr1");
				return (double) attribute.getVerificationResult();
			}
		};				
	
		localObjectives.add(localObj1);
	}
	
	
	private void initGlobalConstraints() {
		double CMAX = 100;
		GlobalConstraintNew constraint1 = new GlobalConstraintNew(RequirementType.GLOBAL_CONSTRAINT, "maintanance_cost", CMAX) {
			@Override
//			"costRoom1, timeRoom1, costRoom2"
			public Object evaluate(List<CapabilitySummaryNew> capabilitySummaries) {
				for (CapabilitySummaryNew cs : capabilitySummaries) {
					double costRoom1 = (double) cs.getCapabilitySummaryElement("costRoom1");
					double timRoom1  = (double) cs.getCapabilitySummaryElement("timeRoom1");
				}
				return null;
			};
		};
	
		globalConstraints.add(constraint1);
	}

}
