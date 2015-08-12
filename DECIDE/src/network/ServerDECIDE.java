package network;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import org.apache.log4j.Logger;

public class ServerDECIDE implements Runnable{
	
	/** Class logger*/
	private final static Logger logger = Logger.getLogger(ServerDECIDE.class);

	/** server handler*/
	private ServerSocket serverSocket;
		
	
	/**
	 * Class constructor
	 * @param port number
	 */
	public ServerDECIDE(int port){
		try {
			this.serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			logger.error("Exception", e);
		}
	}

	
	public void run() {			
		try {
			//server is listening
			System.out.println("Server ready: listening on port " + serverSocket.getLocalPort());
			
			while (true){
				Socket server  = serverSocket.accept();
				new Server(server).start();
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
	
	
	private static class Server extends Thread{
		/** Server socket */
		private Socket server;
		
		/** reader */
		private BufferedReader inFromClient; 
		
		/** writer */
		private PrintWriter outToClient;

		
		public Server(Socket server){
			this.server = server;
		}
		
		
		public void run(){
			try{
				//client connected to server
				System.out.println("Connected to " + server.getRemoteSocketAddress());
				
				//prepare reader and writer
				inFromClient 	= new BufferedReader(new InputStreamReader(server.getInputStream()));
				outToClient 	= new PrintWriter(new BufferedWriter(new OutputStreamWriter(server.getOutputStream())),true); 					
				
				while (true){
					String input = inFromClient.readLine();
					if (input == null){
						break;
					}
					System.out.println("Received: " + input);
				}
			}
			catch (IOException e) {
				logger.error("Exception", e);
				e.printStackTrace();
			}
		}
	}
}
