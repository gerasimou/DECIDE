package _main;

import network.ClientSocketDECIDE;
import network.SocketReceiver;

public class mainServer {

	
	public static void main(String[] args) {
		
		System.out.println("Starting DECIDE");
		
		new Thread(new SocketReceiver(9990)).start();
	}
}
