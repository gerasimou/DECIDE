package caseStudies.uuvNew;

import decide.capabilitySummary.CapabilitySummaryNew;

public class UUVCapabilitySummaryNew extends CapabilitySummaryNew {
	
	/**
	 * Class constructor
	 */
	public UUVCapabilitySummaryNew (int csc, double measurements, double energy) {
		capabilitySummaryElementsMap.put("csc", csc);
		capabilitySummaryElementsMap.put("measurements", measurements);
		capabilitySummaryElementsMap.put("energy", energy);
	}

}
