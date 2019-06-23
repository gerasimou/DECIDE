package caseStudies.healthcare;

import decide.capabilitySummary.CapabilitySummaryNew;

public class RobotCapabilitySummary extends CapabilitySummaryNew {

	
	/**
	 * Class constructor
	 */
	public RobotCapabilitySummary (double timeRoom1, double costRoom1, double timeRoom2, double costRoom2, double delay) {
		capabilitySummaryElementsMap.put("costRoom1", costRoom1);
		capabilitySummaryElementsMap.put("timeRoom1", timeRoom1);
		capabilitySummaryElementsMap.put("costRoom2", costRoom2);
		capabilitySummaryElementsMap.put("timeRoom2", timeRoom2);
		capabilitySummaryElementsMap.put("delay", 	 delay);		
	}
	
	
	

}
