package network;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import org.apache.log4j.Logger;

import decide.DECIDE;

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
	
	/** Logging system events*/
    final static Logger logger = Logger.getLogger(MulticastTransmitter.class);
	
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
			logger.error(e.getStackTrace());
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
			logger.error(e.getStackTrace());
		}
	}
	
	/**
	 * Log system events to console or file
	 * @param String log
	 */
	private void logEvents(String parameter){

		if(logger.isDebugEnabled())
			logger.debug(parameter);
		else
			logger.info(parameter);

		//logger.error("This is error : " + parameter);
	}
	
	
	/** 
	 * Send an object to peers 
	 */
	public void send (Object ... args){
		try{
			ByteArrayOutputStream baos 	= new ByteArrayOutputStream();
			ObjectOutputStream    oas	= new ObjectOutputStream(baos);
			oas.writeObject(args);
			byte data[] = baos.toByteArray();
			
            // Create a packet that will contain the data (in the form of bytes) and send it.
            DatagramPacket msgPacket = new DatagramPacket(data, data.length, address, this.serverPort);
            
            datagramSocket.send(msgPacket);
            
            
            //System.out.println("Sending from ["+ serverAddress +":"+ serverPort +"] --> " + object.toString());
		}
		catch (IOException e){
			logger.error(e.getStackTrace());
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
            
            //System.out.println("Sending from ["+ serverAddress +":"+ serverPort +"] --> " + message);
            logger.info("["+ serverAddress+"][Send:" +message+"]");
		}
		catch (IOException e){
			logger.error(e.getStackTrace());
		}
	}
	
	 	
	
	public ClientDECIDE deepClone(){
		ClientDECIDE newHandler = new MulticastTransmitter(this);
		return newHandler;
	}
}
