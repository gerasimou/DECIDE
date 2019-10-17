package _main;

import network.ComponentTypeDECIDE;
import network.SocketReceiverNew;

public class mainServer {

	
	public static void main(String[] args) {
		
		System.out.println("Starting DECIDE");
		
		new Thread(new SocketReceiverNew("127.0.0.1", 9990, ComponentTypeDECIDE.ROBOT)).start();
	}
}
