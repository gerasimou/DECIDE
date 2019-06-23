package decide.selection;

import java.io.Serializable;

import decide.capabilitySummary.CapabilitySummaryCollection;
import decide.configuration.ConfigurationsCollection;
import decide.environment.Environment;
import decide.localAnalysis.LocalCapabilityAnalysis;
import decide.receipt.CLAReceipt;

public abstract class SelectionNew implements Serializable{
	
	// has been altered
	public abstract boolean execute(ConfigurationsCollection configurationsCollection, CapabilitySummaryCollection capabilitySummaryCollection);
	
	public abstract SelectionNew deepClone(Object ... args);

}
