package network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class MulticastTransmitter implements ClientDECIDE{
	/** server address*/
	private String serverAddress;
	
	/** server port */
	private int serverPort;
	
	/** datagram socket */
	private DatagramSocket datagramSocket; 
	
	/** communicating address */
	private InetAddress address;
	
	
	/** Message to be sent*/
	private String msg = "message";
	
	
	public MulticastTransmitter(String serverAddress, int port) {
		this.serverAddress	= serverAddress;
		this.serverPort		= port;

		try{
			// Get the address that we are going to connect to.
			address = InetAddress.getByName(this.serverAddress);
			
			// Open a new DatagramSocket, which will be used to send the data.
			datagramSocket = new DatagramSocket();
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
//	@Override
//	public void run() {
//		try{
//            // Create a packet that will contain the data (in the form of bytes) and send it.
//            DatagramPacket msgPacket = new DatagramPacket(msg.getBytes(), msg.getBytes().length, address, this.serverPort);
//            
//            datagramSocket.send(msgPacket);
//            
//            System.out.println("Transmitter sent packet with msg: " + msg +"("+serverPort+")");
//            Thread.sleep(3000);
//		} 
//		catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
	
	
	public void send (String message){
		try{
            // Create a packet that will contain the data (in the form of bytes) and send it.
            DatagramPacket msgPacket = new DatagramPacket(message.getBytes(), message.getBytes().length, address, this.serverPort);
            
            datagramSocket.send(msgPacket);
            
            System.out.println("Transmitter sent packet with msg: " + message +"("+serverPort+")");
		}
		catch (IOException e){
			e.printStackTrace();
		}
	}
	
	 
//	 public static void main(String[] args){
//		 new Thread(new MulticastTransmitter("224.224.224.221", 8881)).start();
//	 }
}
