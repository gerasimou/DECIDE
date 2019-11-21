package caseStudies.healthcare.robot;

import java.io.IOException;
import java.util.Random;

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
		
		String robotName		= "/tb3_0/";
		String[] robotPosition	= new String[] {"3.44133806453583", "-3.587325617481197"};
		double speedi   	= 0.1 + (2.0 - 0.1) * rand.nextDouble();
		boolean trapped		= false;
		double p2iretry 	= 0.5 + (0.9 - 0.5) * rand.nextDouble();
		String[] status 	= new String[] {"1", "8", "7", "2", "true"};
		double distance		= 10;
		
//		return robotName +","+ robotPosition +","+ speedi +","+ trapped +","+ p2iretry +","+ status +","+ distance;
		return "'/tb3_0/', [3.430234107709467, -3.6194399941842024], 0, [False], [0], ['', False], 9.979822729765134";
	}
}
