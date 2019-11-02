package caseStudies.healthcare;

import decide.component.Component;
import decide.component.requirements.RequirementType;
import decide.component.requirements.reqNew.LocalObjective;
import decide.configuration.Configuration;

public class Robot extends Component {

	public Robot() {
		super();
	}

	
	@Override
	protected void setupGlobalRequirements() {
		//NULL: The global requirements are checked by the MDP policy synthesis engine
		//@see RobotSelectionMDP
	}

	
	@Override
	protected void setupLocalRequirements() {
		//Local Objective 1: Maximise local utility
		//R4: Subject to requirement R3 being satisfied, the robot shall maximise the local utility achieved through performing task T3 in its allocated rooms.
		LocalObjective localObj = new LocalObjective(RequirementType.COMPONENT_OBJECTIVE, "local-utility", true) {
			
			@Override
			public Object evaluate(Configuration configuration) {
				return (double)configuration.getVerificationResult("utility");
			}
		};
		
		requirementsLocalList.add(localObj);
	}

}
