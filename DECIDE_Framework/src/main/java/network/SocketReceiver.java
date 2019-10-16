package network;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;

import org.apache.log4j.Logger;

import decide.StatusComponent;

public class SocketReceiver extends ReceiverDECIDE{
	
	/** Class logger*/
	private final static Logger logger = Logger.getLogger(SocketReceiver.class);

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
	public SocketReceiver(int port){
		this.serverPort = port;
		try {
			
	        // Create a new Multicast socket (that will allow other sockets/programs to join it as well.
	        this.serverSocket = new ServerSocket(serverPort);
	        
	                 
        }
        catch (IOException e) {
			logger.error(e.getStackTrace().toString());
		}
	}
	/*
	 * do {
                br1 = new BufferedReader(new InputStreamReader(System.in));
                pr1 = new PrintWriter(socket.getOutputStream(), true);
                in = br1.readLine();
                pr1.println(in);
            } while (!in.equals("END"));
        } else {
            do {
                br2 = new BufferedReader(new   InputStreamReader(socket.getInputStream()));
                out = br2.readLine();
                System.out.println("Server says : : : " + out);
            } while (!out.equals("END"));
	 */
	
	public void run() {			
		try {
			//server is listening
			logger.debug("[listening to UUV]:" + serverSocket.getLocalPort());
			
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
	
	private void initiateCommunication(Socket server)
	{
		String message;
		int messageHashCode = 0;
		
		logger.info("[UUV Connected]:" + serverSocket.getLocalPort());
		try {
			do {
				if(atomicPeerStatus.get()==StatusComponent.MISSING) {
					atomicPeerStatus.set(StatusComponent.NEW_JOIN);
				}
		
		
				inFromClient = new BufferedReader(new InputStreamReader(server.getInputStream()));
		
		        writer = new PrintWriter(server.getOutputStream());
				//new Server(server).start();
        
		        if (this.getReplyMessage().hashCode() != messageHashCode && this.getReplyMessage() != "" ) {
					messageHashCode = this.getReplyMessage().hashCode();
					writer.println(this.getReplyMessage());
					writer.flush();
				}
        
		        if(inFromClient.ready()) {
		        	
			        	if((message = inFromClient.readLine()) != null) {
			        		logger.debug("Received from:UUV"+ message+",[Status: "+atomicPeerStatus.get()+"]");
						
			        		this.setTimeStamp(System.currentTimeMillis());
				
			        		String serverAddress = serverSocket.getInetAddress().getHostAddress();
			        		this.localControl.receive(serverAddress, message);
	
			        	}
        
		        }// end if(inFromClient.ready())

		        Thread.sleep(15000);
			}// end do
			while(true);
		}// end try
		catch (IOException e) {
			logger.error("Exception", e);
			e.printStackTrace();	
		} 
		catch (InterruptedException e) {
			// TODO Auto-generated catch block
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
