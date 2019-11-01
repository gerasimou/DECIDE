package caseStudies.uuv;

import decide.capabilitySummary.CapabilitySummary;

public class UUVCapabilitySummary extends CapabilitySummary {
	
	/**
	 * Class constructor
	 */
	public UUVCapabilitySummary (int csc, double measurements, double energy) {
		put("csc", csc);
		put("measurements", measurements);
		put("energy", energy);
	}

}
