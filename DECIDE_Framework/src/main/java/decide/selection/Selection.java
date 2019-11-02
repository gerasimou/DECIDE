package decide.selection;

import java.io.Serializable;

import decide.capabilitySummary.CapabilitySummaryCollection;
import decide.configuration.ConfigurationsCollection;


public abstract class Selection implements Serializable{
	
	// has been altered
	public abstract boolean execute(CapabilitySummaryCollection capabilitySummaryCollection);

}
