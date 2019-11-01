package caseStudies.uuvrobot;

import network.ComponentTypeDECIDE;

public class mainUUVrobot {

	public static void main(String[] args) {
		
		System.out.println("Starting DECIDE Socket Client");

		String address 	= "127.0.0.1";
		int port		= 9201;

		UUVRobotTransmitter client = new UUVRobotTransmitter(address, port, ComponentTypeDECIDE.ROBOT);
		Thread t = new Thread(client, "UUV Client");
		
		t.start();
	}

}
