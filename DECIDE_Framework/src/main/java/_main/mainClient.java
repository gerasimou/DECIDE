package _main;

import network.ClientSocketDECIDE;
import network.SocketReceiver;

public class mainClient {

	
	public static void main(String[] args) {
		
		System.out.println("Starting DECIDE");
		
		for (int i=0; i<5; i++){
			System.out.println(i);
			new Thread(new ClientSocketDECIDE("127.0.0.1", 9990)).start();
		}
	}
	
}
