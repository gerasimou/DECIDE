package decide.selection;

import java.io.Serializable;

import decide.capabilitySummary.CapabilitySummaryCollection;
import decide.configuration.ConfigurationsCollection;
import decide.environment.Environment;


public abstract class Selection implements Serializable{
	
	// has been altered
	public abstract boolean execute(ConfigurationsCollection configurationsCollection, CapabilitySummaryCollection capabilitySummaryCollection, Environment environment );
	
	public abstract Selection deepClone(Object ... args);

}
