package caseStudies.activityBot;

import java.util.List;
import org.apache.log4j.Logger;
import decide.Knowledge;
import decide.capabilitySummary.CapabilitySummary;
import decide.capabilitySummary.CapabilitySummaryCollection;
import decide.component.requirements.Requirement;
import decide.configuration.ConfigurationsCollection;
import decide.environment.Environment;
import decide.selection.Selection;

	public class ActivityBotSelection extends Selection{

		
		
		/** Logging system events*/
	    final static Logger logger = Logger.getLogger(ActivityBotSelection.class);
		
		public ActivityBotSelection() {
			super();
			
//			System.out.println(this.getClass().getName());
		}
		
		@Override
		public boolean execute(ConfigurationsCollection configurationsCollection, CapabilitySummaryCollection capabilitySummaryCollection, Environment environment )
		{
			logger.info("UUVSelection.execute()");
			// The selected configuration details
			
			if (capabilitySummaryCollection.findGlobalAllocation(configurationsCollection, environment))
			{
				CapabilitySummary [] CapabilitySummaries = ((ActivityBotCapabilitySummaryCollection)capabilitySummaryCollection).getOptimalConfiguration(); 
				ActivityBotCapabilitySummary activityBotCapabilitySummary = (ActivityBotCapabilitySummary)CapabilitySummaries[CapabilitySummaries.length-1];
				
				int contribution = activityBotCapabilitySummary.getContribution();
				double timeBound = activityBotCapabilitySummary.getTimeUpperBound();
				
				
				// Alter threshold object for the local control verification
				List<Requirement> globalReqsList = Knowledge.getGlobalRequirements();
				globalReqsList.get(0).setThreshold(contribution);
				Knowledge.geLocalRequirements().get(0).setThreshold(timeBound);
				
				return true;
			}
			
			return false;
			
			
		}

		
		/*
		@Override
		public boolean execute(CLAReceipt cla, ConfigurationsCollection configurationsCollection){
			logger.info(".execute()");
			// The selected configuration details
			
			if (configurationsCollection.findBestforSystemReqyirements(cla))
				return true;
			
			return false;
			
			
		}
*/
		
		@Override
		public Selection deepClone(Object... args) {
			// TODO Auto-generated method stub
			return null;
		}

	}

