package network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class MulticastServer implements Runnable{
	/** server address*/
	private String serverAddress;
	
	/** server port */
	private int serverPort;
	
	/** datagram socket */
	private DatagramSocket datagramSocket; 
	
	/** communicating address */
	private InetAddress address;
	
	
	/** Message to be sent*/
	private String msg;
	
	
	public MulticastServer(String serverAddress, int port) {
		this.serverAddress	= serverAddress;
		this.serverPort		= port;

		try{
			// Get the address that we are going to connect to.
			address = InetAddress.getByName(this.serverAddress);
			
			// Open a new DatagramSocket, which will be used to send the data.
			datagramSocket = new DatagramSocket(this.serverPort);
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	@Override
	public void run() {
		try{
            // Create a packet that will contain the data (in the form of bytes) and send it.
            DatagramPacket msgPacket = new DatagramPacket(msg.getBytes(), msg.getBytes().length, address, this.serverPort);
            
            datagramSocket.send(msgPacket);
            
            System.out.println("Server sent packet with msg: " + msg);
            Thread.sleep(500);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	 
	 public static void main(String[] args){

	 }
}
