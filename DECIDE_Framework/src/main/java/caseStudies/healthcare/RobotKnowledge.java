package caseStudies.healthcare;

import auxiliary.Utility;
import decide.Knowledge;

public class RobotKnowledge extends Knowledge{
	
	private static int NROOMST1;
	private static int NROOMST2; 

	private static int REMAININGROOMST1;
	private static int REMAININGROOMST2; 
	
	private static int MYROOMST1;
	private static int MYROOMST2;
	
	
	
	public static void initRobotKnowledge (){
		NROOMST1 			= Integer.parseInt(Utility.getProperty("NROOMST1"));
		NROOMST2 			= Integer.parseInt(Utility.getProperty("NROOMST2"));			
		REMAININGROOMST1 	= Integer.parseInt(Utility.getProperty("NROOMST1"));
		REMAININGROOMST2 	= Integer.parseInt(Utility.getProperty("NROOMST2"));
		MYROOMST1			= 0;
		MYROOMST2			= 0;
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
	
	
	public static int getMyRoomsT1 () {
		return MYROOMST1;
	}

	
	public static int getMyRoomsT2 () {
		return MYROOMST2;
	}
	
	public static void setMyRooms(int roomsT1, int roomsT2) {
		MYROOMST1 = roomsT1;
		MYROOMST2 = roomsT2;
	}

	
}
