package decide.selection;

import java.io.Serializable;

import decide.capabilitySummary.CapabilitySummaryCollectionNew;
import decide.configuration.ConfigurationsCollectionNew;
import org.apache.log4j.Logger;


public abstract class SelectionNew implements Serializable{
	/** Logging system events*/
    final public static Logger logger = Logger.getLogger(SelectionNew.class);
	
	// has been altered
	public abstract boolean execute(ConfigurationsCollectionNew configurationsCollection, CapabilitySummaryCollectionNew capabilitySummaryCollection);

}
