package example.uuv;

import java.util.Arrays;
import java.util.List;
import lombok.EqualsAndHashCode;
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
@EqualsAndHashCode(callSuper=true)
public class UUVCapabilitySummary extends CapabilitySummary{
	private int		CSC;
	private double 	measuremnets;
	private double 	powerConsumption;
	
	/**
	 * Class constructor: create a new UUVCapabilitySummary instance
	 * @param CSC
	 * @param measuremnets
	 * @param powerConsumption
	 */
	public UUVCapabilitySummary (int CSC, double numofMeasurements, double consumedPower){
		this.CSC							= CSC;
		this.measuremnets   				= numofMeasurements;
		this.powerConsumption				= consumedPower;
		
		
	}
	
	/**
	 * Class constructor: create a new UUVCapabilitySummary instance
	 * @param CapabilitySummary
	 */
	public UUVCapabilitySummary (UUVCapabilitySummary cs){
		super();
		this.CSC							= cs.getCSC();
		this.measuremnets   				= cs.getMeasuremnets();
		this.powerConsumption				= cs.getPowerConsumption();
		
		
	}
	
	
	/**
	 * Print the details of this ResultQV instance
	 */
	@Override
	public String toString() {
//	    String NEW_LINE = System.getProperty("line.separator");
		StringBuilder str = new StringBuilder();
	    str.append("["+this.CSC + ",");
	    str.append(measuremnets + ",");
	    str.append(powerConsumption + "]");
		return str.toString();
	}
	
	/**
	 * Clone the UUV Capability Summary
	 */
	@Override
	public CapabilitySummary deepClone(Object ... args) {
		CapabilitySummary capabilitySummary = new UUVCapabilitySummary(this);
		return capabilitySummary;
	}	
	
	public double getMeasuremnets() {
		return measuremnets;
	}


	public double getPowerConsumption() {
		return powerConsumption;
	}


	public int getCSC() {
		return CSC;
	}


	@Override
	public List<?> getCapabilitySummaryElements() {
		return Arrays.asList(new Object[]{CSC, measuremnets, powerConsumption});
	}
	
	public boolean areSensorsOn(){
		return CSC > 0 ? true : false;
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