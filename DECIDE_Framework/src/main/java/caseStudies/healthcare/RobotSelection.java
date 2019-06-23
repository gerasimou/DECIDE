package caseStudies.healthcare;

import decide.capabilitySummary.CapabilitySummaryCollection;
import decide.configuration.ConfigurationsCollection;
import decide.selection.SelectionNew;


public class RobotSelection extends SelectionNew {

	public RobotSelection() {
		super();
	}

	
	@Override
	public boolean execute(ConfigurationsCollection configurationsCollection, CapabilitySummaryCollection capabilitySummaryCollection) {
		//TODO: here we need to make the distribution of tasks based on the capability summaries
		return false;
	}

	
	@Override
	public SelectionNew deepClone(Object... args) {
		return null;
	}

}
