package caseStudies.healthcare;

import decide.capabilitySummary.CapabilitySummaryNew;

public class RobotCapabilitySummary extends CapabilitySummaryNew {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Class constructor
	 */
	public RobotCapabilitySummary (double timeRoom1, double costRoom1, double timeRoom2, double costRoom2, double delay) {
		put("costRoom1", costRoom1);
		put("timeRoom1", timeRoom1);
		put("costRoom2", costRoom2);
		put("timeRoom2", timeRoom2);
		put("delay", 	 delay);		
	}
	
	/**
	 * Print the details of this ResultQV instance
     * @return 
	 */
	@Override
	public String toString() {
//	    String NEW_LINE = System.getProperty("line.separator");
		StringBuilder str = new StringBuilder();
	    str.append("[").append(get("costRoom1") ).append(",");
	    str.append(get("timeRoom1") ).append(",");
	    str.append(get("costRoom2") ).append(",");
	    str.append(get("timeRoom2") ).append(",");
		return str.append(get("delay") ).append("]").toString();
	}
	
	

}
