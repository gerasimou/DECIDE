package example.uuv;

import java.util.List;

import org.apache.log4j.Logger;

import decide.Knowledge;
import decide.capabilitySummary.CapabilitySummary;
import decide.capabilitySummary.CapabilitySummaryCollection;
import decide.component.requirements.Requirement;
import decide.configuration.Configuration;
import decide.configuration.ConfigurationsCollection;
import decide.environment.Environment;
import decide.selection.Selection;

public class UUVSelection extends Selection {
	
	/** Logging system events*/
    final static Logger logger = Logger.getLogger(UUVSelection.class);
	
	public UUVSelection() {
		super();
		
//		System.out.println(this.getClass().getName());
	}
	
	@Override
	public boolean execute(ConfigurationsCollection configurationsCollection, CapabilitySummaryCollection capabilitySummaryCollection, Environment environment )
	{
		logger.info("UUVSelection.execute()");
		// The selected configuration details
		
		if (capabilitySummaryCollection.findGlobalAllocation(configurationsCollection, environment))
		{
			CapabilitySummary [] CapabilitySummaries = ((UUVCapabilitySummaryCollection)capabilitySummaryCollection).getOptimalConfiguration(); 
			UUVCapabilitySummary uuvCapabilitySummary = (UUVCapabilitySummary)CapabilitySummaries[CapabilitySummaries.length-1];
			double [] cla = new double [3];
			cla[0] = uuvCapabilitySummary.getMeasuremnets();
			cla[1] = uuvCapabilitySummary.getCSC() > 0 ? 1 : 0;
			cla[2] = uuvCapabilitySummary.getPowerConsumption();
			
			// Alter threshold object for the local control verification
			List<Requirement> globalReqsList = Knowledge.getGlobalRequirements();
			for(int i=0; i<2;i++)
				globalReqsList.get(i).setThreshold(cla[i]);
			Knowledge.geLocalRequirements().get(0).setThreshold(cla[2]);
			
			return true;
		}
		
		return false;
		
		
	}

	
	@Override
	public Selection deepClone(Object... args) {
		// TODO Auto-generated method stub
		return null;
	}

}
