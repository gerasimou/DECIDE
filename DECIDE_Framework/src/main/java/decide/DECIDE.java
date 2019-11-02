package decide;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import auxiliary.Utility;
import decide.capabilitySummary.CapabilitySummaryCollection;
import decide.configuration.ConfigurationsCollection;
import decide.environment.Environment;
import decide.localAnalysis.LocalCapabilityAnalysis;
import decide.localControl.LocalControl;
import decide.receipt.CLAReceipt;
import decide.selection.Selection;
import network.ReceiverDECIDENew;
import network.TransmitterDECIDE;


public class DECIDE implements Cloneable, Serializable{
	
	/** DECIDE stages interfaces*/
	private LocalCapabilityAnalysis 	lca;
	private CLAReceipt				claReceipt;
	private LocalControl 			localControl;
	private Selection				selection;
	
//	/** QV handler */
//	private AttributeEvaluatorNew propertyEvaluator;
	
	/** configuration */
	private ConfigurationsCollection 	configurationsCollection;
	
	/** capability Summary collection  */
	private CapabilitySummaryCollection 	capabilitySummaryCollection;
	
	/** environment */
	private Environment					environment;
	
	/** Logging system events*/
    final static Logger logger = LogManager.getLogger(DECIDE.class);
	
    /** Keeping track of peers status*/
    private Map<ReceiverDECIDENew, Long> peerReceiversMap;
    
    private ReceiverDECIDENew robotReceiver;
    
    
    private HeartbeatDECIDE heartbeat;
	
    
	/**
 	 * Constructor requiring all components of DECIDE framework. 
 	 * If any is null, the default handler will be instantiated.
	 * @param lca
	 * @param claReceipt
	 * @param selection
	 * @param localControl
	 * @throws DecideException 
	 */
	public DECIDE(LocalCapabilityAnalysis lca, CLAReceipt claReceipt, Selection selection, LocalControl localControl,
			ConfigurationsCollection configurationsCollection, CapabilitySummaryCollection capabilitySummaries, Environment  environment) {
		try {
//			//if a PropertyEvaluator instance exists -> use it, otherwise instantiate a PrismQV (default) instance
//			if (lca!=null)
//				this.propertyEvaluator = lca.getAttributeEvaluator();
//			else if (localControl!=null)
//				this.propertyEvaluator = localControl.getAttributeEvaluator();
//			else
//				this.propertyEvaluator	= new PrismQVNew();
			
			//if CLAReceipt is null -> instantiate the default handler
			if (lca == null)
				throw new DecideException("No local capability analysis handled defined");
			else
				this.lca = lca;
	
			//if CLAReceipt is null -> instantiate the default handler
			if (claReceipt == null)
				throw new DecideException("No capability summary receipt handler handled defined");
			else
				this.claReceipt = claReceipt;
			
			//if Selection is null -> instantiate the default handler
			if (selection == null)
				throw new DecideException("No selection handled defined");
			else
				this.selection = selection;
			
			//if LocalControl is null -> instantiate the default handler
			if (localControl == null)
				throw new DecideException("No local control loop handled defined");
			else
				this.localControl = localControl;
			
			this.configurationsCollection		= configurationsCollection;
			this.capabilitySummaryCollection	= capabilitySummaries;
			this.environment					= environment;
			this.peerReceiversMap					= new ConcurrentHashMap<>();
			
			long hrTimeWindow = Long.parseLong(Utility.getProperty(DECIDEConstants.HEARTBEAT_TIME_WINDOW));
			this.heartbeat = new HeartbeatDECIDE(hrTimeWindow);
			
		}
		catch (DecideException e) {
			e.printStackTrace();
		}	
	}
	
	
	/** 
 	 * Default constructor: instantiates the default handlers
	 * @throws DecideException 
	 */
	public DECIDE(ConfigurationsCollection configurationsCollection, CapabilitySummaryCollection capabilitySummaries, Environment environment) throws DecideException{
		this(null, null, null, null, configurationsCollection, capabilitySummaries, environment);
	}

	
	/**
	 * Constructor accepting a <b>LocalCapabilityAnalysis</b> instance
	 * @param lca
	 * @throws DecideException 
	 */
	public DECIDE(LocalCapabilityAnalysis lca, ConfigurationsCollection configurationsCollection, CapabilitySummaryCollection capabilitySummaries, Environment environment) throws DecideException{
		this(lca, null, null, null, configurationsCollection, capabilitySummaries, environment);
	}
	
	
	/**
	 * Constructor accepting a <b>CLAReceipt</b> instance
	 * @param claReceipt
	 * @throws DecideException 
	 */
	public DECIDE(CLAReceipt claReceipt, ConfigurationsCollection configurationsCollection, 
					CapabilitySummaryCollection capabilitySummaries, Environment  environment) throws DecideException{
		this(null, claReceipt, null, null, configurationsCollection, capabilitySummaries, environment);
	}
	

	/**
	 * Constructor accepting a <b>Selection</b> instance
	 * @param selection
	 * @throws DecideException 
	 */
	public DECIDE(Selection selection, ConfigurationsCollection configurationsCollection, 
					CapabilitySummaryCollection capabilitySummaries, Environment  environment) throws DecideException{
		this(null, null, selection, null, configurationsCollection, capabilitySummaries, environment);
	}
	
	
	/**
	 * Constructor accepting a <b>LocalControl</b> instance
	 * @param localControl
	 * @throws DecideException 
	 */
	public DECIDE (LocalControl localControl, ConfigurationsCollection configurationsCollection, 
					 CapabilitySummaryCollection capabilitySummaries, Environment  environment) throws DecideException{
		this (null, null, null, localControl, configurationsCollection, capabilitySummaries, environment);
	}

	
	/**
	 * Run <b>DECIDE</b> protocol
	 */
	public void run(){
		long decideLoopTimeWindow = Long.parseLong(Utility.getProperty(DECIDEConstants.DECIDE_LOOP_TIME_WINDOW));
		//boolean initialRun = true;
		boolean solutionFound = false;
		
		try{
			while (true) {
				Thread.sleep(decideLoopTimeWindow); 
				
				
				//When there is a problem with the robot (i.e., a major change)
				if (localControl.checkStatus(StatusRobot.MAJOR_LOCAL_CHANGE)) {

					//flag the problem
					claReceipt.setStatus(StatusRobot.MAJOR_LOCAL_CHANGE);
					
					//execute local capability analysis step
					lca.execute(configurationsCollection, environment);
					
//					if(logger.isDebugEnabled())
						configurationsCollection.printAll();
				
					//share local capability analysis results with peers
					logger.info("Sending to peers " + Arrays.toString(configurationsCollection.getCapabilitySummariesArray())+"]");
					lca.shareCapabilitySummary(configurationsCollection.getCapabilitySummariesArray());
					
					//add my capability summary to the CS collection (no need to do it anymore in the selection class)
					capabilitySummaryCollection.put(lca.getTransmitterToOtherDECIDE().getServerAddress(), configurationsCollection.getCapabilitySummariesArray());
						
					//wait for some time for new CLAs from peers
					Thread.sleep(2000);	
				}
				
				
				//Assess Peers Health
				long TIME_NOW = System.currentTimeMillis();
		
				logger.info("Checking if a peer is stale " + TIME_NOW);
				for (ReceiverDECIDENew receiver :  peerReceiversMap.keySet()) {
					//if we haven't heard from this peer for 10s -> it has probably failed
					if (receiver.hasMajorChange(TIME_NOW)) {
						logger.info("Peer " + receiver.getServerAddress() + " is stale " + receiver.getTimeStamp());
						
						//1) remove its capability summary for the collection 
						claReceipt.removeCapabilitySummary(receiver.getServerAddress());
												
						//2) flag the problem to trigger a new CLA selection
						claReceipt.setStatus(StatusRobot.MAJOR_PEER_CHANGE);
					}
				}
				
				
				//If a new local capability summary exists (either from the robot or its peers) or a peer is absent
				if ((claReceipt.checkStatus(StatusRobot.MAJOR_PEER_CHANGE)) || 
					(claReceipt.checkStatus(StatusRobot.MAJOR_LOCAL_CHANGE))){
				
					logger.info("CLAMode "+claReceipt.getStatus()+"]");
										
					//Run selection algorithm to partition mission goals among peers					
					solutionFound = selection.execute(capabilitySummaryCollection);
					
					if(!solutionFound) {
						logger.info("Could not find feasible solution");
						localControl.setStatus(StatusRobot.IDLE);
					}
					else {
						logger.info("[Component "+Knowledge.getID()+ " has responsibilities]" + Knowledge.getResponsibilitiesToString());
						if (!localControl.checkStatus(StatusRobot.OFFLINE)) {
							localControl.setStatus(StatusRobot.STABLE);
							logger.info("[Component "+localControl.getStatus()+" NewCommand ");
						}
					}
					
					claReceipt.setStatus(StatusRobot.STABLE);
				}
				
				//If everything is OK
				if (localControl.checkStatus(StatusRobot.STABLE)) {//&&(localControl.isReceivedNewCommand()))  // Comments were placed for testing
					localControl.execute(configurationsCollection, environment);
				}
				
				
				//Assess Robot Health
				logger.info("Checking if robot is stale " + TIME_NOW);
				if (robotReceiver.hasMajorChange(TIME_NOW)) {
					logger.info("Robot " + robotReceiver.getServerAddress() + " is stale " + robotReceiver.getTimeStamp());
					
					//1) Reset robot's environment map
					localControl.robotIsStale();
					
					//2) flag the problem to trigger a new CLA selection
					localControl.setStatus(StatusRobot.MAJOR_LOCAL_CHANGE);
				}
			}
		}
		catch (Exception e){
			logger.error("[error] : " + e.getMessage(),e);
			
		}
	}
	
	/*
	 * decide.setRemoteClient(robotTransmitter);
			decide.setRemoteServer(robotReceiver);
	 */
	
	
	/**
	 * Set the DECIDE remote client, i.e., where DECIDE can transmit to the robot/component
	 * @param transmitter
	 */
	public void setTransmitterToRobot (TransmitterDECIDE transmitter){
		localControl.setTransmitterToRobot(transmitter);
	}
	
	
	/**
	 * Set the DECIDE listening server for remote client, i.e., where DECIDE can receive from the robot/component
	 * @param receiver
	 */
	public void setReceiverFromRobot (ReceiverDECIDENew receiver){
		localControl.setReceiverFromRobot(receiver);
		robotReceiver = receiver;
	}
	
	
	/**
	 * Set the DECIDE client, i.e., where DECIDE can transmit to other DECIDE
	 * @param client
	 */
	public void setTransmitterToOtherDECIDE(TransmitterDECIDE client){
		lca.setTransmitterToOtherDECIDE(client);
//		
		//start the heartbeat
		heartbeat.setHeartbeatTransmitterToOtherDECIDE(client);
		Thread t = new Thread(heartbeat, "Heartbeat");
		t.start();
	}
	
	
	/** 
	 * Set the DECIDE servers, i.e., where DECIDE can receive messages from other DECIDE
	 * @param receiversList
	 */
	public void setReceiversFromOtherDECIDEs(List<ReceiverDECIDENew> receiversList){
		claReceipt.setReceiversFromOtherDECIDEs(receiversList);
		for (ReceiverDECIDENew receiver : receiversList) {
			peerReceiversMap.put (receiver, receiver.getTimeStamp());
		}
	}
	

	/**
	 * Overloaded toString  method
	 */
	@Override
	public String toString(){
		return this.hashCode() +","+ lca +","+ claReceipt +","+ selection +","+ localControl;
	}


//	/**
//	 * Log system events to console or file
//	 * @param String log
//	 */
//	private void logEvents(String parameter){
//		if(logger.isDebugEnabled())
//			logger.debug("[debug] : " + parameter);
//		else
//			logger.info("[info] : " + parameter);
//
//		//logger.error("This is error : " + parameter);
//	}
	
//	public void run(){
//		long decideLoopTimeWindow = Long.parseLong(Utility.getProperty("DECIDE_LOOP_TIME_WINDOW"));
//		//boolean initialRun = true;
//		boolean solutionFound = false;
//		
//		try{
//			while (true) {
//				Thread.sleep(decideLoopTimeWindow); 
//				
//				
//				//When there is a problem with the robot (i.e., a major change)
//				if (localControl.checkStatus(StatusRobot.MAJOR_LOCAL_CHANGE)) {
////					|| localControl.getAtomicOperationReference().get() == StatusRobot.STARTUP) {
//					
//					switch (localControl.getStatus()) {
//						case MAJOR_LOCAL_CHANGE:{
//							localControl.setStatus(StatusRobot.STABLE);
//							claReceipt.setStatus(StatusRobot.MAJOR_LOCAL_CHANGE);
//							break;
//						}
////						case STARTUP:{
////							this.localControl.getAtomicOperationReference().set(StatusRobot.STABLE); // Used to be Offline, changed for testing purpose
////							this.claReceipt.setOperationMode(StatusRobot.MAJOR_PEER_CHANGE);
////							break;
////						}
//						default: {break;}
//					}
//					
//					lca.execute(configurationsCollection, environment);
//					
//					if(logger.isDebugEnabled())
//						configurationsCollection.printAll();
//
//				
//				// why resetting local control to stable and claReceipt to Major change mode, and this block never executed
////				if(this.localControl.getAtomicOperationReference().get() == OperationMode.MAJOR_CHANGE_MODE)
////				{
////					this.localControl.getAtomicOperationReference().set(OperationMode.STABLE_MODE);
////					this.claReceipt.getAtomicOperationReference().set(OperationMode.MAJOR_CHANGE_MODE);
////				}
//
//					if (localControl.checkStatus(StatusRobot.OFFLINE)) {
//						lca.shareCapabilitySummary(configurationsCollection.getCapabilitySummariesArray());
//						logger.debug("Sending to peers " + Arrays.toString(configurationsCollection.getCapabilitySummariesArray())+"]");
////						if(logger.isDebugEnabled())
////							logger.debug("[Send:" +Arrays.toString(configurationsCollection.getCapabilitySummariesArray())+"]");
//					}
//				}
//
////				System.out.println("\n\nPrinting best from each mode\n");
////				configurationsCollection.printBestFromMode();
//				
////				Thread.sleep(5000);
//				
//				if ((claReceipt.checkStatus(StatusRobot.MAJOR_PEER_CHANGE)) || 
//					(claReceipt.checkStatus(StatusRobot.MAJOR_LOCAL_CHANGE))){
//				
//					logger.debug("CLAMode "+claReceipt.getStatus()+"]");
//										
//					//Run selection algorithm to partition mission goals among peers					
//					solutionFound = selection.execute(configurationsCollection, capabilitySummaryCollection);
//					
//					if(!solutionFound) {
//						logger.debug("Component" + KnowledgeNew.getID() + "[idle]: Could not find feasible plan");
////						logEvents("Component" + KnowledgeNew.getID()+"[idle]: Could not find feasible plan");
//						localControl.setStatus(StatusRobot.IDLE);
//						localControl.setReceivedNewCommand(false);
//						// You could order attached component to stop action.
//					}
//					else {
//						logger.debug("[Component "+Knowledge.getID()+" has task]");
//						if (localControl.checkStatus(StatusRobot.OFFLINE)) {
//							localControl.compareAndSetStatus(StatusRobot.IDLE, StatusRobot.STABLE);
//							localControl.setReceivedNewCommand(true);
//							logger.debug("[Component "+localControl.getStatus()+" NewCommand "+localControl.isReceivedNewCommand()+"]");
//						}
//					}
//					
//					claReceipt.compareAndSetStatus(StatusRobot.MAJOR_PEER_CHANGE, StatusRobot.STABLE);
//				}
//				
//				if (localControl.checkStatus(StatusRobot.STABLE)) {//&&(localControl.isReceivedNewCommand()))  // Comments were placed for testing
//					localControl.execute(configurationsCollection, environment);
//					localControl.setReceivedNewCommand(false);
//
//				}
//		
//				// reset the operation mode to Stable
//				//if(claReceipt.getAtomicOperationReference().get() == OperationMode.MINOR_CHANGE_MODE)
//				//claReceipt.getAtomicOperationReference().set(OperationMode.STABLE_MODE);
////				localControl.execute(configurationsCollection, environment, false);
////					configurationsCollection.printAll();
//
////				System.out.println("\n\nPrinting best from each mode\n");
////				configurationsCollection.printBestFromMode();
//
////				selection.execute();
//			}
//		}
//		catch (Exception e){
//			logger.error("[error] : " + e.getMessage(),e);
//			
//		}
//	}
	
}
