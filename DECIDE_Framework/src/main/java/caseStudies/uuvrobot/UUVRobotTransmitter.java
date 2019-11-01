package caseStudies.uuvrobot;

import java.io.IOException;
import java.util.Random;

import network.ComponentTypeDECIDE;
import network.SocketTransmitterNew;

public class UUVRobotTransmitter extends SocketTransmitterNew implements Runnable{

	public UUVRobotTransmitter(String serverAddress, int port, ComponentTypeDECIDE networkType) {
		super(serverAddress, port, networkType);
	}

	
	@Override
	public void run() {
		Random rand = new Random();
		try {
			while (true) {
				String line; 
				if(inFromServer.ready()) {
					if((line = inFromServer.readLine()) != null) {
					
						System.out.println("Received:\t" + line);
						Thread.sleep(100);
						
						double r1 = rand.nextInt(500)/100.0;
						double r2 = rand.nextInt(400)/100.0;
						double r3 = rand.nextInt(400)/100.0;
						String msg = r1+","+r2+","+r3;
						
						send(msg);
					}
				}
			}
		} 
		catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}
}
