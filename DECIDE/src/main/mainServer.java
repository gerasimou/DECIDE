package main;

import network.ClientSocketDECIDE;
import network.ServerSocketDECIDE;

public class mainServer {

	
	public static void main(String[] args) {
		
		System.out.println("Starting DECIDE");
		
		new Thread(new ServerSocketDECIDE(9990)).start();
	}
}
