package caseStudies.healthcare;

import decide.capabilitySummary.CapabilitySummary;

public class RobotCapabilitySummary extends CapabilitySummary {

	
	/**
	 * Class constructor
	 */
	public RobotCapabilitySummary (double timeRoom1, double costRoom1, double timeRoom2, double costRoom2, double delay) {//, double utility) {
		put("costRoom1", costRoom1);
		put("timeRoom1", timeRoom1);
		put("costRoom2", costRoom2);
		put("timeRoom2", timeRoom2);
		put("delay", 	 delay);	
//		put("utility",	 utility);
	}
	
	
	

}
