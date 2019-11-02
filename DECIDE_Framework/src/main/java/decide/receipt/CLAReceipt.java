
package decide.receipt;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import auxiliary.Utility;
import decide.DECIDEConstants;
import decide.StatusComponent;
import decide.StatusRobot;
import decide.capabilitySummary.CapabilitySummaryCollection;
import decide.capabilitySummary.CapabilitySummary;
import network.NetworkUser;
import network.ReceiverDECIDENew;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;


public abstract class CLAReceipt implements Serializable, NetworkUser{
	
	/** peers list */	
	protected Map<String, ReceiverDECIDENew> receiversMap;
	
	/** Logging system events*/
    final static Logger logger = LogManager.getLogger(CLAReceipt.class);

	/** flag indicating whether a new capability summary has been received*/
	protected volatile boolean receivedNewCapabilitySummary = false;
	
	/** time window for waiting for new capability summaries*/
	protected final long TIME_WINDOW;
	
	/** An object that reports system operation mode */
	protected AtomicReference<StatusRobot> robotStatus;
	
	/** capability summaries received from peers*/
	private CapabilitySummaryCollection capabilitySummaryCollection;
	
	
	/**
	 * Class constructor
	 */
	protected CLAReceipt(CapabilitySummaryCollection capabilitySummaryCollection) {
		//init parameters
		this.receivedNewCapabilitySummary	= false;
		this.TIME_WINDOW					= Long.parseLong(Utility.getProperty(DECIDEConstants.CLA_TIME_WINDOW));
//		this.receivedTimeStamp				= 0;
		this.robotStatus					= new AtomicReference<StatusRobot>(StatusRobot.MAJOR_LOCAL_CHANGE);
		this.capabilitySummaryCollection	= capabilitySummaryCollection;		
		this.receiversMap					= new ConcurrentHashMap<>();
	}
	

	/**
	 * Return the list of servers, i.e., where I am listening to
	 */
	public Collection<ReceiverDECIDENew> getServersList() {
		return receiversMap.values();
	}


	/**
	 * Set the list of servers, i.e., where I am listening to
	 */
	public void setReceiversFromOtherDECIDEs(List<ReceiverDECIDENew> receiversList){
//		this.serversList = serverList;
		//do initialisation
		for (ReceiverDECIDENew receiver : receiversList){
			receiversMap.put(receiver.getServerAddress(), receiver);
			//assign the CLAReceipt handler
			receiver.setNetworkUser(this, 0);

			//start the receivers
			new Thread(receiver, receiver.toString()).start();
		}
	}
	
	
	public void removeCapabilitySummary(String id) {
		capabilitySummaryCollection.removePeerCapabilitySummary(id);
	}
	
	
	@Override
	public void receive(String serverAddress, Object message){
		//1) Add (or update) the capability summary in the collection
		capabilitySummaryCollection.addCapabilitySummary(serverAddress, (CapabilitySummary[])message);
		
		//2) Flag that a major peer change has occurred  
		robotStatus.set(StatusRobot.MAJOR_PEER_CHANGE);
		
		//3) If the server is offline or missing has encountered a major problem, set its flag to alive
		ReceiverDECIDENew receiver = receiversMap.get(serverAddress);
		receiver.setAtomicPeerStatus(StatusComponent.ALIVE);
		
		//Only for test purpose
		StringBuilder bestSolutionStr = new StringBuilder();
		for (CapabilitySummary cs : (CapabilitySummary[])message) {
			bestSolutionStr.append(cs);
		}
		logger.info("Received from " + serverAddress +" "+ bestSolutionStr);
		
	}
	
	
	public abstract boolean execute(Object...args);


	public StatusRobot getStatus() {
		return robotStatus.get();
	}

	public void setStatus(StatusRobot mode) {
		robotStatus.set(mode);
	}
	
	public boolean checkStatus (StatusRobot mode) {
		return (robotStatus.get() == mode); 
	}
	
	public boolean compareAndSetStatus (StatusRobot expected, StatusRobot update) {
		return robotStatus.compareAndSet(expected, update);
	}
	
	
	public boolean isKnownReceiver (String serverAddress) {
		return capabilitySummaryCollection.capabilitySummaryExists(serverAddress);
	}
		
}