package _main;

import network.ComponentTypeDECIDE;
import network.SocketReceiver;

public class mainServer {

	
	public static void main(String[] args) {
		
		System.out.println("Starting DECIDE");
		
		new Thread(new SocketReceiver("127.0.0.1", 9990, ComponentTypeDECIDE.ROBOT)).start();
	}
}
