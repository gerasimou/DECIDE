package caseStudies.healthcare.robot;

import java.io.IOException;
import java.util.Arrays;
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
		double p2iretry 	= 0.5 + (0.9 - 0.5) * rand.nextDouble();
		double speedi   	= 0.1 + (2.0 - 0.1) * rand.nextDouble();
		int[]  avTasks		= new int[] {1,2,3};
		boolean trapped		= false;
		String[] position	= new String[] {"1", "5", "10", "1", "true"};

		return p2iretry +","+ speedi +","+ Arrays.toString(avTasks) +","+ trapped +","+ Arrays.toString(position);
	}
}
