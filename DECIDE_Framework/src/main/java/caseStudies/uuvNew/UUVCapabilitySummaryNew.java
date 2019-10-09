package caseStudies.uuvNew;

import decide.capabilitySummary.CapabilitySummaryNew;

public class UUVCapabilitySummaryNew extends CapabilitySummaryNew {
	
	/**
	 * Class constructor
	 */
	public UUVCapabilitySummaryNew (int csc, double measurements, double energy) {
		put("csc", csc);
		put("measurements", measurements);
		put("energy", energy);
	}

}
