package network;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.Socket;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import decide.StatusComponent;

public class SocketTransmitterNew extends TransmitterDECIDE implements Serializable {

	/** Class logger*/
	private final static Logger logger = LogManager.getLogger(SocketTransmitterNew.class);
	
	/** client socket*/
	private Socket socket;
	
	/** reader */
	protected BufferedReader inFromServer; 
	
	/** writer */
	protected PrintWriter outToServer;
	
	
	
	/**
	 * Constructor: create a new client DECIDE instance
	 * @param serverAddress
	 * @param port
	 */
	public SocketTransmitterNew(String serverAddress, int port, ComponentTypeDECIDE networkType) {
    	super(serverAddress, port, networkType);

		
//		init();
	}

	
	/**
	 * Initialises this instance
	 */
	public void init(){
		try {
			this.socket 	= new Socket(serverAddress, serverPort);
			
			inFromServer 	= new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
			outToServer		= new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())),true); 		
			
			setAtomicPeerStatus(StatusComponent.ALIVE);
		} 
		catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
//			logger.error("Exception", e);
		}
	}


	@Override
	public void send(Object object) {
		System.out.println("Sending:\t"+ object);
		outToServer.println(object);// "From Client " + num++);
		outToServer.flush();
		
    	setTimeStamp(System.currentTimeMillis());
	}

}
