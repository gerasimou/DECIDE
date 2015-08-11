package main;

import network.ClientDECIDE;
import network.ServerDECIDE;

public class mainServer {

	
	public static void main(String[] args) {
		
		System.out.println("Starting DECIDE");
		
		new Thread(new ServerDECIDE(9990)).start();
	}
}
