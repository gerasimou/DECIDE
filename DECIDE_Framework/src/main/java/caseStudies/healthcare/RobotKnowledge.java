package caseStudies.healthcare;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
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
	
	private static List<String> REMAININGROOMST1List; 
	private static List<String> REMAININGROOMST2List;
	
	private static int MYROOMST1;
	private static int MYROOMST2;
	
	
	private static Map<String, LinkedList<RobotAssignment>> roomAllocations;
	private static String myAddress;
	private static String myLastServicedRoom;
	private static String myAllocatedRooms;
	
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
		myAllocatedRooms	= "";
		
		
		REMAININGROOMST1List = new ArrayList<>();
		for (int i=0; i<REMAININGROOMST1; i++) {
			REMAININGROOMST1List.add(i+"");
		}
		
		REMAININGROOMST2List = new ArrayList<>();
		for (int i=0; i<REMAININGROOMST2; i++) {
			REMAININGROOMST2List.add(REMAININGROOMST1+ i+"");
		}
		
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
		roomAllocations = new HashMap<String, LinkedList<RobotAssignment>> ();
		
		int T1List = 0;
		int T2List = 0;
		
		for (String key : m_allocations.keySet()) {
			LinkedList<RobotAssignment> allocation = m_allocations.get(key);
			
			LinkedList<RobotAssignment> newAssignment = new LinkedList<RobotAssignment>();
			
			for (RobotAssignment assignment: allocation) {
				if (assignment.getRoomType().equals("1")) {
					newAssignment.add(new RobotAssignment(REMAININGROOMST1List.get(T1List++), assignment.getCapabilityId(), assignment.getRoomType()));
				}
				else {
					newAssignment.add(new RobotAssignment(REMAININGROOMST2List.get(T2List++), assignment.getCapabilityId(), assignment.getRoomType()));
				}
			}
			
			roomAllocations.put(key, newAssignment);
		}
		
		
		//construct my rooms key
		myAllocatedRooms = "";
		int myRoomsT1 = 0;
		int myRoomsT2 = 0;
		LinkedList<RobotAssignment> myAllocation= roomAllocations.get(myAddress);
		
		if (myAllocation != null) {
			int myRooms = myAllocation.size();
			for (int i=0; i<myRooms; i++) {
				RobotAssignment myAssignment = myAllocation.get(i);
				
				//construct the string of my allocated rooms
				myAllocatedRooms += myAssignment.getRoomId();
				if (i < myRooms-1)
					myAllocatedRooms += ",";
				
				if (myAssignment.getRoomType().equals("1"))
					myRoomsT1++;
				else
					myRoomsT2++;
			}
		}

		//update my rooms
		RobotKnowledge.setMyRooms(myRoomsT1, myRoomsT2);	
	}
	
	
	public static String getMyAllocatedRoomsString() {
		return myAllocatedRooms;
	}
	
	public static boolean hasNullResponsibilities() {
		if (MYROOMST1 + MYROOMST2 < 1)
			return true;
		return false;
	}
	
	
	public static void updateRoomServiced(String robotAddress,  String roomID) {
		if (Integer.parseInt(roomID) < 0)
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
				if (room.getRoomType().equals("1")) {
					REMAININGROOMST1--;
					
//					int index = - 1;
//					int r = Integer.parseInt(roomID);
					REMAININGROOMST1List.remove(roomID);
				}
				else {
					REMAININGROOMST2--;
					REMAININGROOMST2List.remove(roomID);
				}
				
				if (address == myAddress) {
					if (room.getRoomType().equals("1"))
						MYROOMST1--;
					else
						MYROOMST2--;
					
					myLastServicedRoom = roomID;
				}
				
				logger.info("Robot completed room: " + roomID +"\t My remaining rooms [T1:" + MYROOMST1 +", T2:"+ MYROOMST2 +"]");
				logger.info("All remaining roooms:\t[T1:" + REMAININGROOMST1+", T2:"+ REMAININGROOMST2 +"]");
			}
		}
	}
	
	
	public static String getMyLastServicedRoom() {
		return myLastServicedRoom;
	}


	
}
