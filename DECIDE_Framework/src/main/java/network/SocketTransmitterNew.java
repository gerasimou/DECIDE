package network;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.Socket;
import java.util.Random;

import org.apache.log4j.Logger;

public class SocketTransmitterNew implements Runnable, Serializable {

	/** Class logger*/
	private final static Logger logger = Logger.getLogger(SocketTransmitterNew.class);
	
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
	public SocketTransmitterNew(String serverAddress, int port) {
		this.serverAddress	= serverAddress;
		this.serverPort		= port;
		
		init();
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
			System.exit(-1);
//			logger.error("Exception", e);
		}
	}
	
		
	@Override
	public void run() {
//		try {
//			outToServer.println("From Client " + num++);
//			outToServer.flush();
//			Thread.sleep(5000);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//		finally{
//			outToServer.println("Final message" + num++);
//			outToServer.flush();
//			outToServer.close();
//		}
		Random rand = new Random();
		try {
			while (true) {
				String line; 
				if(inFromServer.ready()) {
					if((line = inFromServer.readLine()) != null) {
	//					line = inFromServer.readLine();
	//				}
					
						System.out.println("Received:\t" + line);
						Thread.sleep(15000);
						
						double r1 = rand.nextInt(500)/100.0;
						double r2 = rand.nextInt(400)/100.0;
						double r3 = rand.nextInt(400)/100.0;
						String msg = r1+","+r2+","+r3;
						System.out.println("Sending:\t"+msg);
						outToServer.println(msg);// "From Client " + num++);
						outToServer.flush();
					}
				}
			}
		} 
		catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}

}
