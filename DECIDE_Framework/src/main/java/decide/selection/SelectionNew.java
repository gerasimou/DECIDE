package decide.selection;

import java.io.Serializable;

import decide.capabilitySummary.CapabilitySummaryCollection;
import decide.capabilitySummary.CapabilitySummaryCollectionNew;
import decide.configuration.ConfigurationsCollection;
import decide.configuration.ConfigurationsCollectionNew;
import decide.environment.Environment;
import decide.localAnalysis.LocalCapabilityAnalysis;
import decide.receipt.CLAReceipt;

public abstract class SelectionNew implements Serializable{
	
	// has been altered
	public abstract boolean execute(ConfigurationsCollectionNew configurationsCollection, CapabilitySummaryCollectionNew capabilitySummaryCollection);
	
	public abstract SelectionNew deepClone(Object ... args);

}
