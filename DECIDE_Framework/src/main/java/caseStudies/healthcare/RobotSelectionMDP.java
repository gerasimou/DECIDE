package caseStudies.healthcare;

import decide.capabilitySummary.CapabilitySummaryCollectionNew;
import decide.configuration.ConfigurationsCollectionNew;
import decide.selection.SelectionNew;


public class RobotSelectionMDP extends SelectionNew {

	public RobotSelectionMDP() {
		super();
	}

	
	@Override
	public boolean execute(ConfigurationsCollectionNew configurationsCollection, CapabilitySummaryCollectionNew capabilitySummaryCollection) {
		//TODO: here we need to make the distribution of tasks based on the capability summaries
		//Here we need to invoke the MDP policy synthesis developed by Javier
		return false;
	}

}
