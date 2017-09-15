package network;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
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
	
	
	/**
	 * Class constructor: create a new multicast transmitter
	 * @param serverAddress
	 * @param port
	 */
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
	
	
	
	/**
	 * Class <b>copy</b> constructor
	 * @param instance
	 */
	private MulticastTransmitter (MulticastTransmitter instance){
		this.serverAddress 	= instance.serverAddress;
		this.serverPort		= instance.serverPort;
		
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
	
	
	/** 
	 * Send an object to peers 
	 */
	public void send (Object object){
		try{
			ByteArrayOutputStream baos 	= new ByteArrayOutputStream();
			ObjectOutputStream    oas	= new ObjectOutputStream(baos);
			oas.writeObject(object);
			byte data[] = baos.toByteArray();
			
            // Create a packet that will contain the data (in the form of bytes) and send it.
            DatagramPacket msgPacket = new DatagramPacket(data, data.length, address, this.serverPort);
            
            datagramSocket.send(msgPacket);
            
            System.out.println("Sent to ["+ serverAddress +":"+ serverPort +"] --> " + object.toString());
		}
		catch (IOException e){
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Send a string message
	 * @param message
	 */
	public void send (String message){
		try{
            // Create a packet that will contain the data (in the form of bytes) and send it.
            DatagramPacket msgPacket = new DatagramPacket(message.getBytes(), message.getBytes().length, address, this.serverPort);
            
            datagramSocket.send(msgPacket);
            
            System.out.println("Sent to ["+ serverAddress +":"+ serverPort +"] --> " + message);
		}
		catch (IOException e){
			e.printStackTrace();
		}
	}
	
	 	
	
	public ClientDECIDE deepClone(){
		ClientDECIDE newHandler = new MulticastTransmitter(this);
		return newHandler;
	}
}
