package caseStudies.healthcare.robot;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class JavaServer {
	public static void main(String args[]) throws Exception {
		String fromClient;
		String toClient;
		//List<Integer> toClient;
		int port = 8880;
		if (args!= null && args[0]!= "")
			port = Integer.parseInt(args[0]);
		ServerSocket server = new ServerSocket(port);
		System.out.println("wait for connection on port " + port);

		Random r = new Random();
		
		boolean run = true;
		Socket client = server.accept();
		System.out.println("got connection on port 8080");
		while(run) {
			BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
			PrintWriter out = new PrintWriter(client.getOutputStream(),true);

			fromClient = in.readLine();
			System.out.println("received: " + fromClient);

			/*if(fromClient.equals("bye")) {
				client.close();
				run = false;
				System.out.println("socket closed");
			}*/

			
			List<Integer> rooms = new ArrayList<>();
			rooms.add(3);
			rooms.add(1);
			rooms.add(4);
			
			int n = r.nextInt(5)+3;
			int n2 = r.nextInt(5)+9;
			int n3 = r.nextInt(10)+1;
			int n4 = r.nextInt(10)+1;
			
			String str = Integer.toString(n);
			String str2 = Integer.toString(n2);
			String str3 = Integer.toString(n3);
			String str4 = Integer.toString(n4);
			

			String s=str+","+str2;
			if (port == 8880) 
				//s = "1,2";l
				s=str+","+str2;
			else
				s = "2,3,4";
			
			 
			//double v = r.nextDouble();

			toClient = s;
			System.out.println("send " + toClient);
			out.println(toClient);
			out.flush();
			Thread.sleep(60000);
			
		}
		System.exit(0);
	}
}