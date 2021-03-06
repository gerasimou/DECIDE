package decide.localControl;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import auxiliary.Utility;
import decide.DECIDEConstants;
import decide.StatusRobot;
import decide.configuration.ConfigurationsCollection;
import decide.environment.Environment;
import network.NetworkUser;
import network.ReceiverDECIDE;
import network.TransmitterDECIDE;


public abstract class LocalControl implements Serializable, NetworkUser{
	
	/** DECIDE peers */
	protected TransmitterDECIDE transmitter;
	
	/** peers list */	
	protected ReceiverDECIDE  receiver;
	
	protected Thread receiverThread;
	
//	/** monitor heartbeat frequency*/
//	protected TimeWindow timeWindow = null;
//	
//	/** ID pattern | 
//	 * {C1,[199.18332939896374, 1, 449.77566639554857][153.21894802773718, 1, 307.3191855388467][346.4243336264962, 1, 759.54477487418]}  
//	 * 		--> C1*/
//	private Pattern idPattern = Pattern.compile("\\{(.*?)\\,");
//	
//	/** Capability pattern
//	 * 	{C1,[199.18332939896374, 1, 449.77566639554857][153.21894802773718, 1, 307.3191855388467][346.4243336264962, 1, 759.54477487418]}  
//	 * 	--> [199.18332939896374, 1, 449.77566639554857][153.21894802773718, 1, 307.3191855388467][346.4243336264962, 1, 759.54477487418]
//	 */
//	private Pattern capabilityPattern = Pattern.compile("\\[(.*?)\\]");

	/** flag indicating whether a new capability summary has been received*/
	protected volatile boolean receivedAliveMessage = false;
	
//	/** flag indicating whether a new command has been received*/
//	private boolean receivedNewCommand = false;
//	
//	/** flag indicating whether the time window passed and a new selection needs to be carried out*/
//	protected boolean timeWindowPassed = false;

	/** time window for waiting for new capability summaries*/
	protected final long TIME_WINDOW;
	
	/** An object that reports system operation mode */
	protected AtomicReference<StatusRobot> robotStatus;
	
	
	/** Time stamp to record peer activity*/
	protected long receivedTimeStamp;
	
//	private Thread serverThread;
	
	/** Logging system events*/
    final static Logger logger = LogManager.getLogger(LocalControl.class);
	
//	/** Property Evaluator handler */
//	protected AttributeEvaluatorNew attributeEvaluator;
	
    /** Local map stores received information from robot*/
	protected Map<String, Object> receivedEnvironmentMap;
	
	protected boolean receivedEnvironmentMapUpdated;

	
	
	
	/**
	 * Class constructor
	 */
	protected LocalControl(){
		this.receivedAliveMessage	= false;
		this.TIME_WINDOW			= Long.parseLong(Utility.getProperty(DECIDEConstants.LOCAL_CONTROL_TIME_WINDOW));
		this.receivedTimeStamp		= 0;
		this.robotStatus			= new AtomicReference<StatusRobot>(StatusRobot.MAJOR_LOCAL_CHANGE);
	}

	
//	/**
//	 * Return the QV instance
//	 * @return
//	 */
//	public AttributeEvaluatorNew getAttributeEvaluator(){
//		return this.attributeEvaluator;
//	}
	
	
	/**
	 * Assign this DECIDE instance client, i.e., where it can transmit
	 * @param client
	 */
	public void setTransmitterToRobot(TransmitterDECIDE client){
		this.transmitter = client;
	}
	
	
	/**
	 * Assign this DECIDE instance server, i.e., where it can transmit
	 * @param receiver
	 */
	public void setReceiverFromRobot(ReceiverDECIDE  receiver){
		this.receiver = receiver;
		this.receiver.setNetworkUser(this, 0);

		//start the receiver
		receiverThread = new Thread(receiver, receiver.toString());
		receiverThread.start();
	}
	
	
	public ReceiverDECIDE getReceiverFromRobot () {
		return this.receiver;
	}
	
	
	/**
	 * Share capability summary with peers
	 */
//	public void sendCommand(Object command){
//		this.transmitter.send(command);
//	}
	
	

	
//	public void setPropertyEvaluator(AttributeEvaluatorNew propertyEvaluator) {
//		this.attributeEvaluator = propertyEvaluator;
//	}
	
	
	public StatusRobot getStatus() {
		return robotStatus.get();
	}

	
	public void setStatus(StatusRobot mode) {
		robotStatus.set(mode);
	}
	
	
	public boolean checkStatus (StatusRobot mode) {
		return (robotStatus.get() == mode); 
	}
	
	
	public void resetReceiverThread () {
		
	}
	
	
	/**
	 * <b>Abstact</b> execute action
	 * @param args
	 */
	public abstract void execute(ConfigurationsCollection modesCollection, Environment environment);
	
	
	public abstract void robotIsStale(); 
}