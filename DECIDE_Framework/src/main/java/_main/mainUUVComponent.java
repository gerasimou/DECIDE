package _main;

import network.SocketTransmitterNew;

public class mainUUVComponent {

	public static void main(String[] args) {
		
		System.out.println("Starting DECIDE Socket Client");

		String address 	= "127.0.0.1";
		int port		= 9201;

		SocketTransmitterNew client = new SocketTransmitterNew(address, port);
		Thread t = new Thread(client, "UUV Client");
		
		t.start();
	}

}
