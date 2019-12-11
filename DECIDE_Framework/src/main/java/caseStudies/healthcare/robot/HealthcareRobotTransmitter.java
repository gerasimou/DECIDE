package caseStudies.healthcare.robot;

import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

import caseStudies.healthcare.RobotKnowledge;
import network.ComponentTypeDECIDE;
import network.SocketTransmitter;

public class HealthcareRobotTransmitter extends SocketTransmitter implements Runnable{

	Random rand = new Random();

	public HealthcareRobotTransmitter(String serverAddress, int port, ComponentTypeDECIDE networkType) {
		super(serverAddress, port, networkType);
	}

	
	@Override
	public void run() {
		try {
			while (true) {
				String line; 
				if(inFromServer.ready()) {
					if((line = inFromServer.readLine()) != null) {
					
						System.out.println("Received:\t" + line);
						Thread.sleep(100);
						
						String msg = getMsg();
						
						send(msg);
					}
				}
			}
		} 
		catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	
	public String getMsg() {
		//rangeMin + (rangeMax - rangeMin) * r.nextDouble();
		int[]  avTasks		= new int[] {1,2,3};
		
//		String robotName		= "/tb3_0/";
//		String[] robotPosition	= new String[] {"3.44133806453583", "-3.587325617481197"};
//		double speedi   	= 0.1 + (2.0 - 0.1) * rand.nextDouble();
//		boolean trapped		= false;
//		double p2iretry 	= 0.5 + (0.9 - 0.5) * rand.nextDouble();
//		String[] status 	= new String[] {"1", "8", "7", "2", "true"};
//		double distance		= 10;
//		return robotName +","+ robotPosition +","+ speedi +","+ trapped +","+ p2iretry +","+ status +","+ distance;
		
		String v1 = "['/tb3_1/', 2.7725125362708534, -4.170963103309504, 0.2199999988079071, False, 0.6, '0,3.01,-4.41,1.0', True, 2.5546115735901993]";
		
		return "'/tb3_0/', [3.430234107709467, -3.6194399941842024], 0, [False], [0], ['', False], 9.979822729765134";
	}
	
	
	public static void main (String[] args) {
		String message = "['/tb3_1/', 2.7725125362708534, -4.170963103309504, 0.2199999988079071, False, 0.6, '0,3.01,-4.41,1.0', True, 2.5546115735901993]";

		String msg 	= ((String)message).replace("[", "");
		msg	 		= ((String)msg).replace("]", "");
		String [] receivedMsg =  msg.split(",");
		String robotName 		= receivedMsg[0];
		String position[]		= new String[] {receivedMsg[1], receivedMsg[2]};
		String speed			= receivedMsg[3];
		String trapped			= receivedMsg[4];
		String piRetry			= receivedMsg[5];
		String roomId			= receivedMsg[6].replaceFirst("'", "");
		String roomX			= receivedMsg[7];
		String roomY			= receivedMsg[8];
		String roomType			= receivedMsg[9].replaceFirst("'", "");
		String roomServiced		= receivedMsg[10].strip();
		String distance			= receivedMsg[11];
		
		System.out.println(message);
		System.out.println("Received from robot: " + robotName + "\t"+ Arrays.toString(position) + "\t"+ speed + "\t"+ trapped + "\t"+ piRetry + "\t"+ roomServiced + "\t"+ distance);

		System.out.println(Boolean.parseBoolean(roomServiced));
		
		//if the room has been serviced
		if (Boolean.parseBoolean(roomServiced)) {
			RobotKnowledge.updateRoomServiced(null, roomId);
		}

	}
}
