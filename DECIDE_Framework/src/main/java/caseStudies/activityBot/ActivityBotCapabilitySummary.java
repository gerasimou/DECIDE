package caseStudies.activityBot;

import java.util.Arrays;
import java.util.List;
import decide.Knowledge;
import decide.capabilitySummary.CapabilitySummary;
import decide.component.requirements.Requirement;
import decide.component.requirements.RequirementType;



/**
 * class that represents a data structure for storing an UUVC apabilitySummary 
 * <b>this is application specific</b>
 * @author s.yonbawi
 * 
 *
 */

public class ActivityBotCapabilitySummary extends CapabilitySummary{
	private int		contribution;
	private double 	utility;
	private double 	powerConsumption;
	private double	timeUpperBound;
	
	/**
	 * Class constructor: create a new ActivityBotCapabilitySummary instance
	 * @param robotContribution
	 * @param robotUtility
	 * @param robotConsumedPower
	 */
	public ActivityBotCapabilitySummary (int robotContribution, double robotUtility, double consumedPower, double timeBound){
		this.contribution							= robotContribution;
		this.utility   								= robotUtility;
		this.powerConsumption						= consumedPower;
		this.timeUpperBound							= timeBound;
		
	}
	
	/**
	 * Class constructor: create a new UUVCapabilitySummary instance
	 * @param CapabilitySummary
	 */
	public ActivityBotCapabilitySummary (ActivityBotCapabilitySummary cs){
		super();
		this.contribution							= cs.getContribution();
		this.utility   								= cs.getUtility();
		this.powerConsumption						= cs.getPowerConsumption();
		this.timeUpperBound							= cs.getTimeUpperBound();
		
	}
	
	
	/**
	 * Print the details of this ResultQV instance
	 */
	@Override
	public String toString() {
//	    String NEW_LINE = System.getProperty("line.separator");
		StringBuilder str = new StringBuilder();
	    str.append("["+this.contribution + ",");
	    str.append(this.utility + ",");
	    str.append(this.powerConsumption + ",");
	    str.append(this.timeUpperBound + "]");
		return str.toString();
	}
	
	/**
	 * Clone the UUV Capability Summary
	 */
	@Override
	public CapabilitySummary deepClone(Object ... args) {
		CapabilitySummary capabilitySummary = new ActivityBotCapabilitySummary(this);
		return capabilitySummary;
	}	
	
	public double getUtility() {
		return utility;
	}


	public double getPowerConsumption() {
		return powerConsumption;
	}
	public double getTimeUpperBound() {
		return timeUpperBound;
	}

	public int getContribution() {
		return contribution;
	}


	@Override
	public List<?> getCapabilitySummaryElements() {
		return Arrays.asList(new Object[]{contribution, utility, powerConsumption});
	}
	

	@Override
	public double getBound() {
		List<Requirement> globalReqsList = Knowledge.getGlobalRequirements();
		for (Requirement requirement : globalReqsList){
			if (requirement.getType() == RequirementType.SYSTEM_COST)				
				return(double)getGlobalRequirementsResults().get(requirement.getID());
		}								
		
		
		return 0;
	}

	
}