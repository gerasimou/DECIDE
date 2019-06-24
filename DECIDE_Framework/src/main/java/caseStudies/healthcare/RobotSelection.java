package caseStudies.healthcare;

import decide.capabilitySummary.CapabilitySummaryCollection;
import decide.capabilitySummary.CapabilitySummaryCollectionNew;
import decide.configuration.ConfigurationsCollection;
import decide.configuration.ConfigurationsCollectionNew;
import decide.selection.SelectionNew;


public class RobotSelection extends SelectionNew {

	public RobotSelection() {
		super();
	}

	
	@Override
	public boolean execute(ConfigurationsCollectionNew configurationsCollection, CapabilitySummaryCollectionNew capabilitySummaryCollection) {
		//TODO: here we need to make the distribution of tasks based on the capability summaries
		return false;
	}

	
	@Override
	public SelectionNew deepClone(Object... args) {
		return null;
	}

}
