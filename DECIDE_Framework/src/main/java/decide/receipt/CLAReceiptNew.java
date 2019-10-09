
package decide.receipt;

import java.io.Serializable;
import java.util.List;
import java.util.regex.Pattern;
import org.apache.log4j.Logger;
//import ServerClient.FileReader;
import auxiliary.Utility;
import decide.OperationMode;
import decide.capabilitySummary.CapabilitySummaryCollectionNew;
import decide.capabilitySummary.CapabilitySummaryNew;
import network.NetworkUser;
import network.ReceiverDECIDENew;
import java.util.concurrent.atomic.AtomicReference;


public abstract class CLAReceiptNew implements Serializable, NetworkUser{
	
	/** peers list */	
	protected List<ReceiverDECIDENew> serversList;
	
	protected TimeWindow timeWindow = null;
	
	/** Logging system events*/
    final static Logger logger = Logger.getLogger(CLAReceiptNew.class);

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
	protected AtomicReference<OperationMode> atomicOperationReference;
	
	/** capability summaries received from peers*/
	private CapabilitySummaryCollectionNew capabilitySummaryCollection;
	
	/** Time stamp to record peer activity*/
	protected long receivedTimeStamp;
	
	
	/**
	 * Class constructor
	 */
	protected CLAReceiptNew(CapabilitySummaryCollectionNew capabilitySummaryCollection) {
		//init parameters
		this.receivedNewCapabilitySummary	= false;
		this.TIME_WINDOW					= Long.parseLong(Utility.getProperty("CLA_TIME_WINDOW"));
		this.receivedTimeStamp				= 0;
		atomicOperationReference			= new AtomicReference<OperationMode>(OperationMode.STABLE_MODE);
		this.capabilitySummaryCollection	= capabilitySummaryCollection;		
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
	public void setReceiverFromOtherDECIDE(List<ReceiverDECIDENew> serverList){
		this.serversList = serverList;
		//do initialisation
		for (ReceiverDECIDENew server : this. serversList){
			//assign the CLAReceipt handler
			server.setNetworkUser(this, 0);

			//start the receivers
			new Thread(server, server.toString()).start();
		}
	}
	
	
	public void removeCapabilitySummary(String id) {
		this.capabilitySummaryCollection.removePeerCapabilitySummary(id);
	}
	
	
	@Override
	public void receive(String serverAddress, Object message){
		capabilitySummaryCollection.addCapabilitySummary(serverAddress, (CapabilitySummaryNew[])message);
		// I think this boolean flag has to be atomic
//		if (!receivedNewCapabilitySummary){
//			receivedNewCapabilitySummary = true;
//			if(timeWindow != null)
//				timeWindow.interrupt();	
//			
//			timeWindow = new TimeWindow();
//			timeWindow.start();
//			logger.info("[Initiating heartbeat trace]");	
//		}
		
		StringBuilder bestSolutionStr = new StringBuilder();
		for (CapabilitySummaryNew cs : (CapabilitySummaryNew[])message) {
			bestSolutionStr.append(cs);
		}
		logger.info("Received from " + serverAddress +" "+ bestSolutionStr);
		
		atomicOperationReference.set(OperationMode.MAJOR_CHANGE_MODE);
	}
	
	
	public abstract boolean execute(Object...args);


	public abstract CLAReceiptNew deepClone(Object ... args);
		
	
	public abstract boolean executeListeningThread();

	
//	public AtomicReference<OperationMode> geatAtomicOperationReference() {
//		return atomicOperationReference;
//	}

	public OperationMode getOperationMode() {
		return atomicOperationReference.get();
	}

	public void setOperationMode(OperationMode mode) {
		atomicOperationReference.set(mode);
	}
	
	public boolean checkOperationMode (OperationMode mode) {
		return (atomicOperationReference.get() == mode); 
	}
	
	public boolean compareAndSetOperationMode (OperationMode expected, OperationMode update) {
		return atomicOperationReference.compareAndSet(expected, update);
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

	
	/**
	 * Set capability summary collection object to trace peers' capabilities
	 */
	@Deprecated
	private void setCapabilitySummaryCollection(CapabilitySummaryCollectionNew capabilitySummaryCollection){
		this.capabilitySummaryCollection = capabilitySummaryCollection;

	}
		
}
