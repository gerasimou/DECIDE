package caseStudies.healthcare;

import decide.capabilitySummary.CapabilitySummaryCollectionNew;
import decide.capabilitySummary.CapabilitySummaryNew;
import decide.configuration.ConfigurationsCollectionNew;
import decide.selection.SelectionNew;
import decide.selection.mdp.MDPAdversaryGeneration;
import decide.selection.mdp.TextFileHandler;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import caseStudies.uuvNew.UUVCapabilitySummaryCollectionNew;



public class RobotSelectionMDP extends SelectionNew {

	private MDPAdversaryGeneration m_mdp_gen;
	private HashMap<String, LinkedList<RobotAssignment>> m_allocations;
	
	public RobotSelectionMDP(String[] ppArgs, String modelExportFile, String propertiesFile, String advExportFile) {
		super();
		m_mdp_gen = new MDPAdversaryGeneration(ppArgs, modelExportFile, propertiesFile, advExportFile);
	}

	public void shutDown() {
		m_mdp_gen.shutDown();
	}
	
	// NOTE: only rooms of type 2 have associated utility for the optional task
	public String encodeUtility(String robotId, int capabilityIndex, Double value) {
		return ("["+robotId+"c"+Integer.toString(capabilityIndex)+"t2] true : "+Double.toString(value)+"; ");
	}

	public String encodeUtilities(RobotCapabilitySummaryCollection c) {
		String res="// Start of utility collection -------------- \n";
		res += "rewards\"utility\"\n";
		
		Map<String, CapabilitySummaryNew[]> capabilities = (Map<String, CapabilitySummaryNew[]>) c.getCapabilitySummaries();
		for (Map.Entry<String, CapabilitySummaryNew[]> entry : capabilities.entrySet()) {
			CapabilitySummaryNew[] robotCapabilities = entry.getValue();
			for (int i=0; i<robotCapabilities.length;i++) {
				Double utility = 1.0;//(Double)robotCapabilities[i].getCapabilitySummaryElement("utility");
				res += encodeUtility(entry.getKey(),i+1,utility)+"\n";
			}
		}
		res +="endrewards\n";
		res +="// End of utility collection -------------- \n";
		return res;
	}
	
	public String encodeCapabilityElement(String label, String robotId, int capabilityIndex, int roomType, Double value) {
		String roomTypeString = roomType < 0 ? "" : "t"+Integer.toString(roomType);			
		return "const "+label+robotId+"c"+Integer.toString(capabilityIndex)+roomTypeString+"="+Integer.toString(value.intValue())+"; ";
	}
	
	public String encodeCapabilities(RobotCapabilitySummaryCollection c) {

		String res="// Start of capability summary collection -------------- \n";
		Map<String, CapabilitySummaryNew[]> capabilities = (Map<String, CapabilitySummaryNew[]>) c.getCapabilitySummaries();
		
		for (Map.Entry<String, CapabilitySummaryNew[]> entry : capabilities.entrySet()) {
			CapabilitySummaryNew[] robotCapabilities = entry.getValue();
			for (int i=0; i<robotCapabilities.length;i++) {
				Double timeRoom1 = (Double)robotCapabilities[i].getCapabilitySummaryElement("timeRoom1");
				Double costRoom1 = (Double)robotCapabilities[i].getCapabilitySummaryElement("costRoom1");
				Double timeRoom2 = (Double)robotCapabilities[i].getCapabilitySummaryElement("timeRoom2");
				Double costRoom2 = (Double)robotCapabilities[i].getCapabilitySummaryElement("costRoom2");
				Double delay = (Double)robotCapabilities[i].getCapabilitySummaryElement("delay");
				res += encodeCapabilityElement("time",entry.getKey(),i+1,1,timeRoom1) + "\t"+
					   encodeCapabilityElement("cost",entry.getKey(),i+1,1,costRoom1) + "\t"+
					   encodeCapabilityElement("time",entry.getKey(),i+1,2,timeRoom2) + "\t"+
					   encodeCapabilityElement("cost",entry.getKey(),i+1,2,costRoom2) + "\t"+
					   encodeCapabilityElement("delay",entry.getKey(),i+1,-1,delay) + "\n";					
				
			}
		}
		res +="// End of capability summary collection -------------- \n";
		return res;
	}
	
	public String preprocessAllocationModel (String[] args, RobotCapabilitySummaryCollection c) {
		String res = m_mdp_gen.preprocess(args);
		res += encodeUtilities(c);
		res += encodeCapabilities(c);
		return res;
	}
	
	public void exportAllocationModel(String code, String fileName) {
		TextFileHandler fh = new TextFileHandler(fileName);
		fh.exportFile(code);	
	}

	
	/**
     * 
     * @return Set of room allocations per robot
     */
    public HashMap<String,LinkedList<RobotAssignment>> generateAllocations(ArrayList<String> plan){
    	HashMap<String, LinkedList<RobotAssignment>> robotRoomMap = new HashMap<String, LinkedList<RobotAssignment>>();
    	int c=0;
    
    	for (int i=0; i<plan.size();i++) {
    		String a = plan.get(i);
    		String robot = a.substring(1,2);
    		String capability = a.substring(3,4);
    		String roomType = a.substring(5,6);   	
    		robotRoomMap.putIfAbsent(robot, new LinkedList<RobotAssignment>());
    		robotRoomMap.get(robot).add(new RobotAssignment(Integer.toString(c), capability, roomType));
    			c++;
    	}
    	return robotRoomMap;
    }
	
    public HashMap<String,LinkedList<RobotAssignment>> getAllocations(){
    	return m_allocations;
    }
    
    public ArrayList<String> getPlan(){
    	return m_mdp_gen.getPlan();
    }
    
//	@Override
	public boolean execute(ConfigurationsCollectionNew configurationsCollection, CapabilitySummaryCollectionNew capabilitySummaryCollection) {
		// TODO: This method is here just to avoid compilation errors, for the time being
		
		
		//Generate 
		List<String> keysList = new ArrayList<>();
		Set<String> keys = capabilitySummaryCollection.keySet();
		for (String key : keys) {
			keysList.add(key); 
		}
		keysList.sort(Comparator.comparing( String::toString ));
		

		CapabilitySummaryCollectionNew capabilitySummaryCollectionOrdered = new RobotCapabilitySummaryCollection();
		Map<String, Integer> robotIDIPMap = new HashMap<>();
		int robotID = 1;
		for (String key : keys) {
			robotIDIPMap.put(key, robotID);
			capabilitySummaryCollectionOrdered.put ("r"+robotID, capabilitySummaryCollection.get(key));
			robotID++;
		}
		
		
		return execute(capabilitySummaryCollectionOrdered);
	}
	
	public boolean execute(CapabilitySummaryCollectionNew capabilitySummaryCollection) {
		//TODO: here we need to make the distribution of tasks based on the capability summaries
		//Here we need to invoke the MDP policy synthesis developed by Javier
		String allocationModelCode = preprocessAllocationModel( m_mdp_gen.getM_pp_in_args(), (RobotCapabilitySummaryCollection)capabilitySummaryCollection);
		exportAllocationModel(allocationModelCode, m_mdp_gen.getM_out_model_file());
		m_mdp_gen.run();
		m_allocations = generateAllocations(m_mdp_gen.getAdv());
		return false;
	}

	// Class test
	public static void main(String[] args)
	{
		
	// Create a capability analysis collection to extract info for Prism model
		
		RobotCapabilitySummaryCollection col= new RobotCapabilitySummaryCollection();
		
		RobotCapabilitySummary[] r1 = new RobotCapabilitySummary[2];
		r1[0] =  new RobotCapabilitySummary(10, 10, 3, 3, 5);//, 9);
		r1[1] =  new RobotCapabilitySummary(11, 12, 4, 5, 3);//, 8);

		RobotCapabilitySummary[] r2 = new RobotCapabilitySummary[2];
		r2[0] =  new RobotCapabilitySummary(5, 4, 10, 11, 5);//, 98);
		r2[1] =  new RobotCapabilitySummary(3, 3, 11, 12, 3);//, 89);
		
		
		col.addCapabilitySummary("r1", r1);
		col.addCapabilitySummary("r2", r2);
		
		
	// Create a Robot MDP selection object to run a test
		
//		String workPath = "/Users/javier/Desktop/haiq-bin-old/dist-bin/GlobalAlloc-v3/";
		String workPath = "models/healthcare/global/";
		String[] ppArgs = {workPath+"gallocsp.pp", "2", "2", "2"}; //#robots, #capabilities (p3_full discretisation), #room types
		String allocationModelFile = workPath+"allocmodel.prism";
		String propsFile = workPath+"gallocsp.props";
		String advFile = workPath+"adv.tra";
		RobotSelectionMDP sel = new RobotSelectionMDP(ppArgs, allocationModelFile, propsFile, advFile);
		sel.execute(col);
		
//        System.out.println(sel.getPlan().toString());
		System.out.println(sel.getAllocations().toString());
		sel.shutDown();
		
		
	}
	
}




