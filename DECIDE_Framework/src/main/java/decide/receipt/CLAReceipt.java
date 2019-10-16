
package decide.receipt;

import java.io.Serializable;
import java.util.List;
import java.util.regex.Pattern;
import org.apache.log4j.Logger;
//import ServerClient.FileReader;
import auxiliary.Utility;
import decide.StatusRobot;
import decide.capabilitySummary.CapabilitySummary;
import decide.capabilitySummary.CapabilitySummaryCollection;
import network.NetworkUser;
import network.ReceiverDECIDE;
import network.ReceiverDECIDENew;

import java.util.concurrent.atomic.AtomicReference;


public abstract class CLAReceipt implements Serializable, NetworkUser{
	
	/** peers list */	
	protected List<ReceiverDECIDENew> serversList;
	
	protected TimeWindow timeWindow = null;
	
	/** Logging system events*/
    final static Logger logger = Logger.getLogger(CLAReceipt.class);

	/** ID pattern | 
	 * {C1,[199.18332939896374, 1, 449.77566639554857][153.21894802773718, 1, 307.3191855388467][346.4243336264962, 1, 759.54477487418]}  
	 * 		--> C1*/
	private Pattern idPattern = Pattern.compile("\\{(.*?)\\,");
	
	/** Capability pattern
	 * 	{C1,[199.18332939896374, 1, 449.77566639554857][153.21894802773718, 1, 307.3191855388467][346.4243336264962, 1, 759.54477487418]}  
	 * 	--> [199.18332939896374, 1, 449.77566639554857][153.21894802773718, 1, 307.3191855388467][346.4243336264962, 1, 759.54477487418]
	 */
	private Pattern capabilityPattern = Pattern.compile("\\[(.*?)\\]");

	/** flag indicating whether a new capability summary has been received*/
	protected volatile boolean receivedNewCapabilitySummary = false;
	
	/** time window for waiting for new capability summaries*/
	protected final long TIME_WINDOW;
	
	/** An object that reports system operation mode */
	protected AtomicReference<StatusRobot> atomicOperationReference;
	
	/** capability summaries received from peers*/
	private CapabilitySummaryCollection capabilitySummaryCollection;
	
	/** Time stamp to record peer activity*/
	protected long receivedTimeStamp;
	
	
	/**
	 * Class constructor
	 */
	protected CLAReceipt() {
		//init parameters
		//this.messagesFromPeer 				= new LinkedHashMap<String,String>();
		this.receivedNewCapabilitySummary	= false;
		this.TIME_WINDOW					= Long.parseLong(Utility.getProperty("TIME_WINDOW"));
		//this.timeWindowPassed				= false;
	//	this.receivedCS						=  new CapabilitySummary();
		this.receivedTimeStamp				= 0;
		atomicOperationReference			= new AtomicReference<StatusRobot>(StatusRobot.STABLE);
		this.capabilitySummaryCollection	= null;
	//	this.capabilitySummaryMap			= new LinkedHashMap<String, CapabilitySummary>();
		
	}
	


	/**
	 * Return the list of servers, i.e., where I am listening to
	 */
	public List<ReceiverDECIDENew> getServersList() {
		return serversList;
	}


	/**
	 * Set the list of servers, i.e., where I am listening to
	 */
	public void setReceiverFromOtherDECIDE(List<ReceiverDECIDENew> serverList, CapabilitySummaryCollection capabilitySummaryCollection){
		this.serversList = serverList;
		//do initialisation
		for (ReceiverDECIDENew server : this. serversList){
			//assign the CLAReceipt handler
			server.setNetworkUser(this, 0);

			//start the receivers
			new Thread(server, server.toString()).start();
		}
	}
	
	/**
	 * Set capability summary collection object to trace peers' capabilities
	 */
	public void setCapabilitySummaryCollection(CapabilitySummaryCollection capabilitySummaryCollection){
		this.capabilitySummaryCollection = capabilitySummaryCollection;

	}
	
	public void removeCapabilitySummary(String id) {
		this.capabilitySummaryCollection.removePeerCapabilitySummary(id);
	}
	
	
	@Override
	public void receive(String serverAddress, Object message){
		capabilitySummaryCollection.addCapabilitySummary(serverAddress, (CapabilitySummary[])message);
		// I think this boolean flag has to be atomic
		if (!receivedNewCapabilitySummary){
			receivedNewCapabilitySummary = true;
			if(timeWindow != null)
				timeWindow.interrupt();	
			
			timeWindow = new TimeWindow();
			timeWindow.start();
			logger.info("[Initiating heartbeat trace]");	
		}
		//logger.info("[Received: "+serverAddress+"]");	
	}
	
	
	public abstract boolean execute(Object...args);


	public abstract CLAReceipt deepClone(Object ... args);
		
	public abstract boolean executeListeningThread();

	
	public AtomicReference<StatusRobot> getAtomicOperationReference() {
		return atomicOperationReference;
	}

	
	class TimeWindow extends Thread{
		protected TimeWindow(){
			
		}
		
		@Override
		public void run(){
			try {
				while(!this.isInterrupted()) {	
					Thread.sleep(TIME_WINDOW);
				
					if (executeListeningThread())
						this.interrupt();
				}
			} 
			catch (InterruptedException e) {
				logger.error(e.getStackTrace());
			}
		}
	}
	
	
	public boolean isKnownReceiver (String serverAddress) {
		return capabilitySummaryCollection.capabilitySummaryExists(serverAddress);
	}
}
