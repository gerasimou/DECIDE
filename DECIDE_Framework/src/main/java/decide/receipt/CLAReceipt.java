
package decide.receipt;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import auxiliary.Utility;
import network.ServerDECIDE;

public abstract class CLAReceipt implements Serializable{
	
	/** peers list */	
	protected List<ServerDECIDE> serversList;

	/** Message map from peers */
	protected Map<String, String> messagesFromPeers;

	/** ID pattern | 
	 * {C1,[199.18332939896374, 1, 449.77566639554857][153.21894802773718, 1, 307.3191855388467][346.4243336264962, 1, 759.54477487418]}  
	 * 		--> C1*/
	private Pattern idPattern = Pattern.compile("\\{(.*?)\\,");
	
	/** Capability pattern
	 * 	{C1,[199.18332939896374, 1, 449.77566639554857][153.21894802773718, 1, 307.3191855388467][346.4243336264962, 1, 759.54477487418]}  
	 * 	--> [199.18332939896374, 1, 449.77566639554857][153.21894802773718, 1, 307.3191855388467][346.4243336264962, 1, 759.54477487418]
	 */
	private Pattern capabilityPattern = Pattern.compile("\\[(.*?)\\]");

	/** flag indicating whether a new capability summary has been receivedr*/
	private boolean receivedNewCapabilitySummary;
	
	/** flag indicating whether the time window passed and a new selection needs to be carried out*/
	protected boolean timeWindowPassed;

	/** time window for waiting for new capability summaries*/
	private final long TIME_WINDOW;
	

	/**
	 * Class constructor
	 */
	protected CLAReceipt() {
		//init parameters
		this.messagesFromPeers 				= new LinkedHashMap<String,String>();
		this.receivedNewCapabilitySummary	= false;
		this.TIME_WINDOW					= Long.parseLong(Utility.getProperty("TIME_WINDOW"));
		this.timeWindowPassed				= false;
	}

	
	/**
	 * Set the list of servers, i.e., where I am listening to
	 */
	public void setServersList(List<ServerDECIDE> serverList){
		this.serversList = serverList;
		//do initialisation
		for (ServerDECIDE server : this. serversList){
			//assign the CLAReceipt handler
			server.setCLAReceipt(this);
			//start the receivers
			new Thread(server, server.toString()).start();
		}
	}
	

	public void receive(String msg){
		//if this is the first new CS received --> wait for TIME_WINDOW ms 
		if (!receivedNewCapabilitySummary){
			receivedNewCapabilitySummary = true;
			new TimeWindow();
		}
			
		Matcher idMatcher = idPattern.matcher(msg);
		while (idMatcher.find()){
			System.out.println(idMatcher.group(1));
		}

		Matcher capabilityMatcher = capabilityPattern.matcher(msg);
		while (capabilityMatcher.find()){
			System.out.println(capabilityMatcher.group(1));
		}
	}
	
	
	public boolean execute(Object...args){
//		System.out.println(this.getClass().getSimpleName()+".execute()");
		return this.timeWindowPassed;
	}

	public abstract CLAReceipt deepClone(Object ... args);
		
	
	
	class TimeWindow extends Thread{
		protected TimeWindow(){
			this.start();
		}
		
		@Override
		public void run(){
			try {
				Thread.sleep(TIME_WINDOW);
				//time window has passed so a new selection can be carried out
				timeWindowPassed 			 = true;
				receivedNewCapabilitySummary = false;
			} 
			catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
