package caseStudies.healthcare;

import auxiliary.Utility;
import decide.Knowledge;

public class RobotKnowledge extends Knowledge{
	
	private static int NROOMST1;
	private static int NROOMST2; 

	private static int REMAININGROOMST1;
	private static int REMAININGROOMST2; 

	
	public static void initRobotKnowledge (){
		NROOMST1 			= Integer.parseInt(Utility.getProperty("NROOMST1"));
		NROOMST2 			= Integer.parseInt(Utility.getProperty("NROOMST2"));			
		REMAININGROOMST1 	= Integer.parseInt(Utility.getProperty("NROOMST1"));
		REMAININGROOMST2 	= Integer.parseInt(Utility.getProperty("NROOMST2"));			
	}

	
	
	public static int getNROOMST1() {
		return NROOMST1;
	}
	
	public static int getNROOMST2() {
		return NROOMST2;
	}

	public static int getREMAININGROOMST1() {
		return REMAININGROOMST1;
	}
	
	public static int getREMAININGROOMST2() {
		return REMAININGROOMST2;
	}
	
	public static void resetRemainingRooms() {
		REMAININGROOMST1 = 0;
		REMAININGROOMST2 = 0;
	}


	public static void addToRemainingRooms(int roomType, int rooms) {
		if (roomType==1)
			REMAININGROOMST1 += rooms;
		else 
			REMAININGROOMST2 += rooms;
	}

	
}
