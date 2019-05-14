package decide.selection;

import decide.capabilitySummary.CapabilitySummaryCollection;
import decide.configuration.ConfigurationsCollection;
import decide.environment.Environment;
import decide.receipt.CLAReceipt;

public class SelectionHandler extends Selection{

	
	
	public SelectionHandler() {
		super();
		
//		System.out.println(this.getClass().getName());
	}
	
	@Override
	public boolean execute(ConfigurationsCollection configurationsCollection, CapabilitySummaryCollection capabilitySummaryCollection, Environment environment ){
		System.out.println(this.getClass().getName()+".execute()");
		// The selected configuration details
		return false;

	}

	
	@Override
	public Selection deepClone(Object... args) {
		// TODO Auto-generated method stub
		return null;
	}

}
