
package decide.receipt;

import java.io.Serializable;
import java.util.List;
import java.util.regex.Pattern;
import org.apache.log4j.Logger;
//import ServerClient.FileReader;
import auxiliary.Utility;
import decide.OperationMode;
import decide.capabilitySummary.CapabilitySummaryCollection;
import network.ReceiverDECIDE;
import java.util.concurrent.atomic.AtomicReference;


public abstract class CLAReceipt implements Serializable{
	
	/** peers list */	
	protected List<ReceiverDECIDE> serversList;
	
	protected TimeWindow timeWindow = null;
	
	/** Logging system events*/
    final static Logger logger = Logger.getLogger(CLAReceipt.class);

	/** Message map from peers */
	//protected Map<String, String> messagesFromPeer;
	
	/** Map storing the CapabilitySummary for this peer**/
	//protected Map<String, CapabilitySummary> capabilitySummaryMap;

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
	
	/** flag indicating whether the time window passed and a new selection needs to be carried out*/
	//protected boolean timeWindowPassed = false;

	/** time window for waiting for new capability summaries*/
	protected final long TIME_WINDOW;
	
	/** An object that reports system operation mode */
	protected AtomicReference<OperationMode> atomicOperationReference;
	
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
		atomicOperationReference			= new AtomicReference<OperationMode>(OperationMode.STABLE_MODE);
		this.capabilitySummaryCollection	= null;
	//	this.capabilitySummaryMap			= new LinkedHashMap<String, CapabilitySummary>();
		
	}
	


	/**
	 * Return the list of servers, i.e., where I am listening to
	 */
	public List<ReceiverDECIDE> getServersList() {
		return serversList;
	}


	/**
	 * Set the list of servers, i.e., where I am listening to
	 */
	public void setServersList(List<ReceiverDECIDE> serverList, CapabilitySummaryCollection capabilitySummaryCollection){
		this.serversList = serverList;
		//do initialisation
		for (ReceiverDECIDE server : this. serversList){
			//assign the CLAReceipt handler
			server.setCLAReceipt(this, capabilitySummaryCollection, 0);

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
	
	
	public void receive(String serverAddress){
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

	
	public AtomicReference<OperationMode> getAtomicOperationReference() {
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
}
