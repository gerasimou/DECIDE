package caseStudies.healthcare.robot;

import network.ComponentTypeDECIDE;

public class mainHealthcareRobot {

	public static void main(String[] args) {
		
		System.out.println("Starting DECIDE Socket Client");

		String address 	= "127.0.0.1";
		int port		= 9201;

		HealthcareRobotTransmitter client = new HealthcareRobotTransmitter(address, port, ComponentTypeDECIDE.ROBOT);
		Thread t = new Thread(client, "UUV Client");
		
		t.start();
		
//		for (int i=0; i<10; i++) {
//			System.out.println(client.getMsg());
//		}
	}

}
