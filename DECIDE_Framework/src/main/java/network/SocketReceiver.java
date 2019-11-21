package network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import decide.StatusComponent;


public class SocketReceiver extends ReceiverDECIDE{
	
	/** Class logger*/
	private final static Logger logger = LogManager.getLogger(SocketReceiver.class);

	/** server handler*/
	private ServerSocket serverSocket;
	
	/** Buffer reader */
	private BufferedReader inFromClient; 
	
	private PrintWriter writer;

	
	/** Buffer containing the data received */
	private final int BUFFER_SIZE = 256;
	
	
	/**
	 * Class constructor
	 * @param port number
	 */
	public SocketReceiver(String ipAddress, int port, ComponentTypeDECIDE networkType){
    	super(ipAddress, port, networkType);
		
		try {
	        // Create a new Server socket (that will allow other sockets/programs to join it as well.
	        this.serverSocket = new ServerSocket(port);//, 1, InetAddress.getByName(ipAddress));      
        }
        catch (IOException e) {
			logger.error(e.getStackTrace().toString());
		}
	}

	
	public void run() {			
		try {
			//server is listening
			logger.info("Waiting for component at port " + serverSocket.getLocalPort());
			
			Socket server  = serverSocket.accept();
			
			logger.info("Component connected at port " + serverSocket.getLocalPort());

            setAtomicPeerStatus(StatusComponent.ALIVE);

			inFromClient = new BufferedReader(new InputStreamReader(server.getInputStream()));
			
	        writer = new PrintWriter(server.getOutputStream());
            
//			initiateCommunication(server);
	        execute();
			// just clear configuration map if peer is absent
		}
		catch (IOException e) {
			logger.error("Exception", e);
			e.printStackTrace();
			
		} 
		finally{
			try {
				serverSocket.close();
			} 
			catch (IOException e) {
				logger.error("Exception", e);
			}
		}
	}
	
	
	private void execute() {
		try {
			String message;
			long timestamp		= Long.MIN_VALUE;

			do {
				if ( (this.getTimeStamp() != timestamp) && (this.getReplyMessage() != "") && (getStatus()>=1) ) {
					logger.info("Sending to robot: " + this.getReplyMessage() + "]");

					timestamp = this.getTimeStamp();
					writer.println(this.getReplyMessage());
					writer.flush();
	        		setStatus(-1);
				}
				
		        if(inFromClient.ready()) {
		        	if((message = inFromClient.readLine()) != null) {
		        		logger.info("Received from robot"+ message+",[Status: " + getAtomicPeerStatus() + "]");
					
		        		setTimeStamp(System.currentTimeMillis());
			
		        		String serverAddress = serverSocket.getInetAddress().getHostAddress();
		        		networkUser.receive(serverAddress, message);
		        	} 
		        }
				
			}
			while(true);
		}
		catch (Exception e) {
			logger.error("Exception", e);
			e.printStackTrace();	
		} 
		finally{
			try {
				serverSocket.close();
			} 
			catch (IOException e) {
				logger.error("Exception", e);
			}
		}

	}
	
	
//	private void initiateCommunication(Socket server) {
//		String message;
////		int messageHashCode = 0;
//		long timestamp		= Long.MIN_VALUE;
//		
////		logger.info("Component connected at port " + serverSocket.getLocalPort());
//		try {
//			if(getAtomicPeerStatus()==StatusComponent.MISSING) {
//				setAtomicPeerStatus(StatusComponent.NEW_JOIN);
//			}
//	
////			inFromClient = new BufferedReader(new InputStreamReader(server.getInputStream()));
////	
////	        writer = new PrintWriter(server.getOutputStream());
//			//new Server(server).start();
//
//			do {
//
////		        if (this.getReplyMessage().hashCode() != messageHashCode && this.getReplyMessage() != "" ) {
//				if ( (this.getTimeStamp() != timestamp) && (this.getReplyMessage() != "") && (getStatus()==1) ) {
////					System.out.println("Sending\t" + this.getReplyMessage() +"("+this.getTimeStamp() +","+ timestamp +")") ;
//					logger.info("[Sending to UUV: " + this.getReplyMessage() + "]");
//
////					messageHashCode = this.getReplyMessage().hashCode();
//					timestamp = this.getTimeStamp();
//					writer.println(this.getReplyMessage());
//					writer.flush();
//				}
//        
//		        if(inFromClient.ready()) {
//		        	if((message = inFromClient.readLine()) != null) {
//		        		logger.info("Received from UUV"+ message+",[Status: " + getAtomicPeerStatus() + "]");
//					
//		        		this.setTimeStamp(System.currentTimeMillis());
//			
//		        		String serverAddress = serverSocket.getInetAddress().getHostAddress();
//		        		networkUser.receive(serverAddress, message);
//		        		setStatus(-1);
//		        	} 
//		        }// end if(inFromClient.ready())
//
////		        Thread.sleep(15000);
//			}// end do
//			while(true);
//		}// end try
//		catch (Exception e) {
//			logger.error("Exception", e);
//			e.printStackTrace();	
//		} 
//		finally{
//			try {
//				serverSocket.close();
//			} 
//			catch (IOException e) {
//				logger.error("Exception", e);
//			}
//		}
//	}
	
}
