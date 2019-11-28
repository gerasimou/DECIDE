package caseStudies.healthcare;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import auxiliary.Utility;
import decide.Knowledge;

public class RobotKnowledge extends Knowledge{
	
	private static int NROOMST1;
	private static int NROOMST2; 

	private static int REMAININGROOMST1;
	private static int REMAININGROOMST2; 
	
	private static int MYROOMST1;
	private static int MYROOMST2;
	
	
	private static Map<String, LinkedList<RobotAssignment>> roomAllocations;
	private static String myAddress;
	private static String myLastServicedRoom;
	
	/** Logging system events*/
    final static Logger logger = LogManager.getLogger(RobotKnowledge.class);

	
	
	public static void initRobotKnowledge (){
		NROOMST1 			= Integer.parseInt(Utility.getProperty("NROOMST1"));
		NROOMST2 			= Integer.parseInt(Utility.getProperty("NROOMST2"));			
		REMAININGROOMST1 	= Integer.parseInt(Utility.getProperty("NROOMST1"));
		REMAININGROOMST2 	= Integer.parseInt(Utility.getProperty("NROOMST2"));
		MYROOMST1			= 0;
		MYROOMST2			= 0;
		roomAllocations		= new HashMap<String, LinkedList<RobotAssignment>>();
		myLastServicedRoom	= "-1";
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
	
	
	
	public static void setMyAddress (String serverAddress) {
		myAddress = serverAddress;
	}
	
	
	public static void setRoomAllocations (Map<String, LinkedList<RobotAssignment>> m_allocations) {
		roomAllocations = m_allocations;
//		List<RobotAssignment> myRooms = roomAllocations.get(myAddress);
	}
	
	
	public static boolean hasNullResponsibilities() {
		if (MYROOMST1 + MYROOMST2 < 1)
			return true;
		return false;
	}
	
	
	public static void updateRoomServiced(String robotAddress,  String roomID) {
		if (Integer.parseInt(roomID.strip()) < 0)
			return; 
		
		String address;
		if (robotAddress == null)
			address = myAddress;
		else
			address = robotAddress;
		
		LinkedList<RobotAssignment> robotRooms = roomAllocations.get(address);
		for (RobotAssignment room : robotRooms) {
			if ( (room.getRoomId().equals(roomID)) && 
					(!room.isServiced()) ){
				room.serviced();
				if (room.getRoomType() == "1")
					REMAININGROOMST1--;
				else
					REMAININGROOMST2--;
				
				if (address == myAddress) {
					if (room.getRoomType() == "1")
						MYROOMST1--;
					else
						MYROOMST2--;
					
					myLastServicedRoom = roomID;
				}
				
				logger.info("Robot completed room: " + roomID +"; T1:" + MYROOMST1 +", T2:"+ MYROOMST2 +" left!");
				logger.info("Remaining roooms:\tT1:" + REMAININGROOMST1+", T2:"+ REMAININGROOMST2);
			}
		}
	}
	
	
	public static String getMyLastServicedRoom() {
		return myLastServicedRoom;
	}


	
}
