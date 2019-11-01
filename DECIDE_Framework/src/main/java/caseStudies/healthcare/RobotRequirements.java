package caseStudies.healthcare;

import java.util.ArrayList;
import java.util.List;

import decide.capabilitySummary.CapabilitySummary;
import decide.component.requirements.RequirementType;
import decide.component.requirements.reqNew.GlobalConstraintNew;
import decide.component.requirements.reqNew.LocalConstraint;
import decide.component.requirements.reqNew.LocalObjectiveNew;
import decide.configuration.Configuration;


public class RobotRequirements {

	private List<GlobalConstraintNew> globalConstraints;
	private List<LocalConstraint> localConstraints;
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
		LocalConstraint localConst1 = new LocalConstraint(RequirementType.LOCAL_CONSTRAINT, "time-left", TLEFT) {
			@Override
			public boolean isSatisfied(Configuration configuration) {
				return (double)configuration.getVerificationResult("attr1") > (double)this.getThreshold();
//				DECIDEAttribute attribute = configuration.getAttributeByName("attr1");
//				return (double)attribute.getVerificationResult() > (double)this.getThreshold();
			}

			@Override
			public Number evaluate (Configuration configuration) {
//				DECIDEAttribute attribute = configuration.getAttributeByName("attr1");
//				return (double)attribute.getVerificationResult();
				return (double)configuration.getVerificationResult("attr1");
			}
		}; 
		
		localConstraints.add(localConst1);
	}
	
	
	private void initLocalObjectives() {
		//Local Objective 1: Maximise local utility
		LocalObjectiveNew localObj1 = new LocalObjectiveNew(RequirementType.COMPONENT_OBJECTIVE, "local-utility", true) {
			@Override
			public Object evaluate(Configuration configuration) {
//				DECIDEAttribute attribute = configuration.getAttributeByName("attr1");
//				return (double) attribute.getVerificationResult();
				return (double)configuration.getVerificationResult("attr1");
			}
		};				
	
		localObjectives.add(localObj1);
	}
	
	
	private void initGlobalConstraints() {
		double CMAX = 100;
		GlobalConstraintNew constraint1 = new GlobalConstraintNew(RequirementType.GLOBAL_CONSTRAINT, "maintainance_cost", CMAX) {
			@Override
//			"costRoom1, timeRoom1, costRoom2"
			public Number evaluate(List<CapabilitySummary> capabilitySummaries) {
				for (CapabilitySummary cs : capabilitySummaries) {
					double costRoom1 = (double) cs.getCapabilitySummaryElement("costRoom1");
					double timRoom1  = (double) cs.getCapabilitySummaryElement("timeRoom1");
				}
				return null;
			}

			@Override
			public boolean isSatisfied(List<CapabilitySummary> capabilitySummaries) {
				// TODO Auto-generated method stub
				return false;
			};
		};
	
		globalConstraints.add(constraint1);
	}

}
