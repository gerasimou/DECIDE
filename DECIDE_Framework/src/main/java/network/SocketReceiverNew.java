package network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import org.apache.log4j.Logger;


public class SocketReceiverNew extends ReceiverDECIDENew{
	
	/** Class logger*/
	private final static Logger logger = Logger.getLogger(SocketReceiverNew.class);

	/** server handler*/
	private ServerSocket serverSocket;
	
	/** Buffer reader */
	private BufferedReader inFromClient; 
	
	private PrintWriter writer;

	/** server port */
	private int serverPort;
	
	/** Buffer containing the data received */
	byte[] buf;
	private final int BUFFER_SIZE = 256;
	
	
	/**
	 * Class constructor
	 * @param port number
	 */
	public SocketReceiverNew(String ipAddress, int port){
		this.serverPort 	= port;
		this.serverAddress	= ipAddress;
		try {
	        // Create a new Server socket (that will allow other sockets/programs to join it as well.
	        this.serverSocket = new ServerSocket(port, 1, InetAddress.getByName(ipAddress));      
        }
        catch (IOException e) {
			logger.error(e.getStackTrace().toString());
		}
	}

	
	public void run() {			
		try {
			//server is listening
			logger.info("Waiting for component at port " + serverSocket.getLocalPort());
			
			while (true){
				Socket server  = serverSocket.accept();
				
				initiateCommunication(server);
				// just clear configuartion map if peer is absent
			}			
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
	
	
	private void initiateCommunication(Socket server) {
		String message;
//		int messageHashCode = 0;
		long timestamp		= Long.MIN_VALUE;
		
		logger.info("Component connected at port " + serverSocket.getLocalPort());
		try {
			if(atomicPeerStatus.get()==PeerStatus.MISSING) {
				atomicPeerStatus.set(PeerStatus.NEW_JOIN);
			}
	
			inFromClient = new BufferedReader(new InputStreamReader(server.getInputStream()));
	
	        writer = new PrintWriter(server.getOutputStream());
			//new Server(server).start();

			do {

//		        if (this.getReplyMessage().hashCode() != messageHashCode && this.getReplyMessage() != "" ) {
				if ( (this.getTimeStamp() != timestamp) && (this.getReplyMessage() != "") && (getStatus()==1) ) {
//					System.out.println("Sending\t" + this.getReplyMessage() +"("+this.getTimeStamp() +","+ timestamp +")") ;
					logger.info("[Sending to UUV: " + this.getReplyMessage() + "]");

//					messageHashCode = this.getReplyMessage().hashCode();
					timestamp = this.getTimeStamp();
					writer.println(this.getReplyMessage());
					writer.flush();
				}
        
		        if(inFromClient.ready()) {
		        	if((message = inFromClient.readLine()) != null) {
		        		logger.debug("Received from UUV"+ message+",[Status: " + atomicPeerStatus.get() + "]");
					
		        		this.setTimeStamp(System.currentTimeMillis());
			
		        		String serverAddress = serverSocket.getInetAddress().getHostAddress();
		        		networkUser.receive(serverAddress, message);
		        		setStatus(-1);
		        	} 
		        }// end if(inFromClient.ready())

//		        Thread.sleep(15000);
			}// end do
			while(true);
		}// end try
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
	
}
