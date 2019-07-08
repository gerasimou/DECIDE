package decide.selection;

import java.io.Serializable;

import decide.capabilitySummary.CapabilitySummaryCollectionNew;
import decide.configuration.ConfigurationsCollectionNew;


public abstract class SelectionNew implements Serializable{
	
	// has been altered
	public abstract boolean execute(ConfigurationsCollectionNew configurationsCollection, CapabilitySummaryCollectionNew capabilitySummaryCollection);

}
