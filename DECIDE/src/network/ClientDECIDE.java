package network;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

import org.apache.log4j.Logger;

public class ClientDECIDE implements Runnable {

	/** Class logger*/
	private final static Logger logger = Logger.getLogger(ClientDECIDE.class);
	
	/** client socket*/
	private Socket socket;
	
	/** reader */
	private BufferedReader inFromServer; 
	
	/** writer */
	private PrintWriter outToServer;

	/** server address*/
	private String serverAddress;
	
	/** server port */
	private int serverPort;
	
	static int num = 0;
	
	
	/**
	 * Constructor: create a new client DECIDE instance
	 * @param serverAddress
	 * @param port
	 */
	public ClientDECIDE(String serverAddress, int port) {
		this.serverAddress	= serverAddress;
		this.serverPort		= port;
	}

	
	/**
	 * Initialises this instance
	 */
	public void init(){
		try {
			this.socket 	= new Socket(serverAddress, serverPort);
			
			inFromServer 	= new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
			outToServer		= new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())),true); 		
			
		} catch (IOException e) {
			e.printStackTrace();
//			logger.error("Exception", e);
		}
	}
	
	@Override
	public void run() {
		try {
			outToServer.println("From Client " + num++);
			Thread.sleep(5000);
			outToServer.flush();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
