package decide.selection;

import java.io.Serializable;

import decide.capabilitySummary.CapabilitySummaryCollection;
import decide.configuration.ConfigurationsCollection;


public abstract class SelectionNew implements Serializable{
	
	// has been altered
	public abstract boolean execute(ConfigurationsCollection configurationsCollection, CapabilitySummaryCollection capabilitySummaryCollection);

}
