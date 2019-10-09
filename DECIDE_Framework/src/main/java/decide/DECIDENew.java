package decide;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;

import auxiliary.Utility;
import decide.capabilitySummary.CapabilitySummaryCollectionNew;
import decide.capabilitySummary.CapabilitySummaryNew;
import decide.configuration.ConfigurationsCollectionNew;
import decide.environment.EnvironmentNew;
import decide.evaluator.AttributeEvaluatorNew;
import decide.localAnalysis.LocalCapabilityAnalysisNew;
import decide.localControl.LocalControlNew;
import decide.qv.prism.PrismQVNew;
import decide.receipt.CLAReceiptNew;
import decide.selection.SelectionNew;
import network.TransmitterDECIDE;
import network.ReceiverDECIDENew;


public class DECIDENew implements Cloneable, Serializable{
	
	/** DECIDE stages interfaces*/
	private LocalCapabilityAnalysisNew 	lca;
	private CLAReceiptNew				claReceipt;
	private LocalControlNew 				localControl;
	private SelectionNew					selection;
	private CapabilitySummaryNew			capabilitySummary;
	
	/** QV handler */
	private AttributeEvaluatorNew propertyEvaluator;

	
	/** configuration */
	private ConfigurationsCollectionNew 	configurationsCollection;
	
	/** capability Summary collection  */
	private CapabilitySummaryCollectionNew 	capabilitySummaryCollection;
	
	/** environment */
	private EnvironmentNew					environment;
	
	
	
	/** Logging system events*/
    final static Logger logger = Logger.getLogger(DECIDENew.class);
	
	
	/** 
	 * Default constructor: instantiates the default handlers
	 * @throws DecideException 
	 */
	public DECIDENew(ConfigurationsCollectionNew configurationsCollection, CapabilitySummaryCollectionNew capabilitySummaries, EnvironmentNew environment) throws DecideException{
		this(null, null, null, null, configurationsCollection, capabilitySummaries, environment);
		this.configurationsCollection	= configurationsCollection;
		this.environment					= environment;
		this.capabilitySummaryCollection	= capabilitySummaries;		
	}
	
	
	/** 
	 * Default constructor: instantiates the default handlers
	 */
	public DECIDENew(ConfigurationsCollectionNew configurationsCollection, CapabilitySummaryCollectionNew capabilitySummaries, EnvironmentNew environment, 
			Class<? extends LocalCapabilityAnalysisNew> localClass, Class<? extends CLAReceiptNew> claReciptClass ,Class<? extends SelectionNew> selectionClass,
			Class<? extends LocalControlNew> localControlClass){
		try {
			this.propertyEvaluator	= new PrismQVNew();
			this.lca = localClass.newInstance();
			this.lca.setPropertyEvaluator(propertyEvaluator);
			
			this.claReceipt = claReciptClass.newInstance();
			this.selection	= selectionClass.newInstance();
			
			this.localControl	= localControlClass.newInstance();
			this.localControl.setPropertyEvaluator(propertyEvaluator);
			
			this.configurationsCollection		= configurationsCollection;
			this.environment						= environment;
			this.capabilitySummaryCollection		= capabilitySummaries;
		} 
		catch(IllegalAccessException iAE) {
			logger.error("Illegal Access Exception",iAE);
		}
		catch(InstantiationException iE) {
			logger.error("Instantiation Exception",iE);
		}
	}
	
	
	/**
 	 * Constructor requiring all components of DECIDE framework. 
 	 * If any is null, the default handler will be instantiated.
	 * @param lca
	 * @param claReceipt
	 * @param selection
	 * @param localControl
	 * @throws DecideException 
	 */
	public DECIDENew(LocalCapabilityAnalysisNew lca, CLAReceiptNew claReceipt, SelectionNew selection, LocalControlNew localControl,
			ConfigurationsCollectionNew configurationsCollection, CapabilitySummaryCollectionNew capabilitySummaries, EnvironmentNew  environment) {
		try {
			//if a PropertyEvaluator instance exists -> use it, otherwise instantiate a PrismQV (default) instance
			if (lca!=null)
				this.propertyEvaluator = lca.getAttributeEvaluator();
			else if (localControl!=null)
				this.propertyEvaluator = localControl.getAttributeEvaluator();
			else
				this.propertyEvaluator	= new PrismQVNew();
			
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
			this.capabilitySummaryCollection		= capabilitySummaries;
			this.environment						= environment;	
		}
		catch (DecideException e) {
			e.printStackTrace();
		}
		
	}
	
	
	/**
	 * Constructor accepting a <b>LocalCapabilityAnalysis</b> instance
	 * @param lca
	 * @throws DecideException 
	 */
	public DECIDENew(LocalCapabilityAnalysisNew lca, ConfigurationsCollectionNew configurationsCollection, CapabilitySummaryCollectionNew capabilitySummaries, EnvironmentNew environment) throws DecideException{
		this(lca, null, null, null, configurationsCollection, capabilitySummaries, environment);
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
	
	
	/**
	 * Constructor accepting a <b>CLAReceipt</b> instance
	 * @param claReceipt
	 * @throws DecideException 
	 */
	public DECIDENew(CLAReceiptNew claReceipt, ConfigurationsCollectionNew configurationsCollection, 
					CapabilitySummaryCollectionNew capabilitySummaries, EnvironmentNew  environment) throws DecideException{
		this(null, claReceipt, null, null, configurationsCollection, capabilitySummaries, environment);
	}
	

	/**
	 * Constructor accepting a <b>Selection</b> instance
	 * @param selection
	 * @throws DecideException 
	 */
	public DECIDENew(SelectionNew selection, ConfigurationsCollectionNew configurationsCollection, 
					CapabilitySummaryCollectionNew capabilitySummaries, EnvironmentNew  environment) throws DecideException{
		this(null, null, selection, null, configurationsCollection, capabilitySummaries, environment);
	}
	
	
	/**
	 * Constructor accepting a <b>LocalControl</b> instance
	 * @param localControl
	 * @throws DecideException 
	 */
	public DECIDENew (LocalControlNew localControl, ConfigurationsCollectionNew configurationsCollection, 
					 CapabilitySummaryCollectionNew capabilitySummaries, EnvironmentNew  environment) throws DecideException{
		this (null, null, null, localControl, configurationsCollection, capabilitySummaries, environment);
	}
	
	
	/**
	 * Class <b>copy</b> constructor
	 * @param decide
	 */
	private DECIDENew (DECIDENew decide){
		this.propertyEvaluator	= decide.propertyEvaluator.deepClone();
		this.lca 				= decide.lca.deepClone();// (LocalCapabilityAnalysis) Utility.deepCopy(decide.lca);
		this.claReceipt			= decide.claReceipt.deepClone();
		this.selection			= (SelectionNew)Utility.deepCopy(decide.selection);
		this.localControl		= decide.localControl.deepClone(this.propertyEvaluator);//(LocalControl)Utility.deepCopy(decide.localControl);
	}

	


	/**
	 * Run <b>DECIDE</b> protocol
	 */
	public void run(){
		long decideLoopTimeWindow = Long.parseLong(Utility.getProperty("DECIDE_LOOP_TIME_WINDOW"));
		//boolean initialRun = true;
		boolean solutionFound = false;
		
		try{
			while (true) {
				Thread.sleep(decideLoopTimeWindow); 
				
				if(	localControl.getAtomicOperationReference().get() == OperationMode.MAJOR_LOCAL_CHANGE_MODE ||
					localControl.getAtomicOperationReference().get() == OperationMode.STARTUP) {
					
					switch (localControl.getAtomicOperationReference().get()) {
						case MAJOR_LOCAL_CHANGE_MODE:{
							localControl.getAtomicOperationReference().set(OperationMode.STABLE_MODE);
							claReceipt.setOperationMode(OperationMode.MAJOR_CHANGE_MODE);
							break;
						}
						case STARTUP:{
							this.localControl.getAtomicOperationReference().set(OperationMode.STABLE_MODE); // Used to be Offline, changed for testing purpose
							this.claReceipt.setOperationMode(OperationMode.MAJOR_CHANGE_MODE);
							break;
						}
						default: {break;}
					}
					
					lca.execute(configurationsCollection, environment);
					
					if(logger.isDebugEnabled())
						configurationsCollection.printAll();

				
				// why resetting local control to stable and claReceipt to Major change mode, and this block never executed
//				if(this.localControl.getAtomicOperationReference().get() == OperationMode.MAJOR_CHANGE_MODE)
//				{
//					this.localControl.getAtomicOperationReference().set(OperationMode.STABLE_MODE);
//					this.claReceipt.getAtomicOperationReference().set(OperationMode.MAJOR_CHANGE_MODE);
//				}
				// share CapabilitySummary periodically (heart beat message)
					if(localControl.getAtomicOperationReference().get() != OperationMode.OFFLINE) {
						lca.shareCapabilitySummary(configurationsCollection.getCapabilitySummariesArray());
						logger.debug("Sending to peers " + Arrays.toString(configurationsCollection.getCapabilitySummariesArray())+"]");
//						if(logger.isDebugEnabled())
//							logger.debug("[Send:" +Arrays.toString(configurationsCollection.getCapabilitySummariesArray())+"]");
					}
				}

//				System.out.println("\n\nPrinting best from each mode\n");
//				configurationsCollection.printBestFromMode();
				
//				Thread.sleep(5000);
				
				if ((claReceipt.checkOperationMode(OperationMode.MAJOR_CHANGE_MODE))){//||(localControl.getAtomicOperationReference().get() == OperationMode.MAJOR_LOCAL_CHANGE_MODE))
				
					logger.debug("CLAMode "+claReceipt.getOperationMode()+"]");
					
					claReceipt.compareAndSetOperationMode(OperationMode.MAJOR_CHANGE_MODE, OperationMode.STABLE_MODE);
					
					//Run selection algorithm to partition mission goals among peers					
					solutionFound = selection.execute(configurationsCollection, capabilitySummaryCollection);
					
					if(!solutionFound) {
						logger.debug("Component" + KnowledgeNew.getID() + "[idle]: Could not find feasible plan");
//						logEvents("Component" + KnowledgeNew.getID()+"[idle]: Could not find feasible plan");
						localControl.getAtomicOperationReference().set(OperationMode.IDLE);
						localControl.setReceivedNewCommand(false);
						// You could order attached component to stop action.
					}
					else {
						logger.debug("[Component "+Knowledge.getID()+" has task]");
						if(localControl.getAtomicOperationReference().get() != OperationMode.OFFLINE) {
							localControl.getAtomicOperationReference().compareAndSet(OperationMode.IDLE, OperationMode.STABLE_MODE);
							localControl.setReceivedNewCommand(true);
							logger.debug("[Component "+localControl.getAtomicOperationReference().get()+" NewCommand "+localControl.isReceivedNewCommand()+"]");
						}
					}
				}
				
				if ((localControl.getAtomicOperationReference().get() == OperationMode.STABLE_MODE)) {//&&(localControl.isReceivedNewCommand()))  // Comments were placed for testing
					localControl.execute(configurationsCollection, environment);
					localControl.setReceivedNewCommand(false);

				}
		
				// reset the operation mode to Stable
				//if(claReceipt.getAtomicOperationReference().get() == OperationMode.MINOR_CHANGE_MODE)
				//claReceipt.getAtomicOperationReference().set(OperationMode.STABLE_MODE);
//				localControl.execute(configurationsCollection, environment, false);
//					configurationsCollection.printAll();

//				System.out.println("\n\nPrinting best from each mode\n");
//				configurationsCollection.printBestFromMode();

//				selection.execute();
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
	public void setTransmitterToComponent (TransmitterDECIDE transmitter){
		localControl.setTransmitter(transmitter);
	}
	
	
	/**
	 * Set the DECIDE listening server for remote client, i.e., where DECIDE can receive from the robot/component
	 * @param receiver
	 */
	public void setReceiverFromComponent (ReceiverDECIDENew receiver){
		localControl.setReceiver(receiver);
	}
	
	
	/**
	 * Set the DECIDE client, i.e., where DECIDE can transmit to other DECIDE
	 * @param client
	 */
	public void setTransmitterToOtherDECIDE(TransmitterDECIDE client){
		lca.setTransmitterToOtherDECIDE(client);
	}
	
	
	/** 
	 * Set the DECIDE servers, i.e., where DECIDE can receive messages from other DECIDE
	 * @param serverList
	 */
	public void setReceiverFromOtherDECIDE(List<ReceiverDECIDENew> serverList){
		claReceipt.setReceiverFromOtherDECIDE(serverList);
	}
	
	
	/**
	 * Clone <b>this</b> DECIDE object 
	 */
	public DECIDENew clone(){
		try {
			return (DECIDENew) super.clone();
		}
		catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		System.exit(-1);
		return null;
	}
	
	
	/**
	 * <b>Deep clone</b> of this DECIDE object
	 * @return
	 */
	public DECIDENew deepClone(){ 
		return new DECIDENew(this);
	}

	
	/**
	 * Overloaded toString  method
	 */
	@Override
	public String toString(){
		return this.hashCode() +","+ lca +","+ claReceipt +","+ selection +","+ localControl;
	}

	// Newly added, for testing purpose
	public ConfigurationsCollectionNew getConfigurationsCollection() {
		return configurationsCollection;
	}

	// Newly added, for testing purpose
	public LocalCapabilityAnalysisNew getLca() {
		return lca;
	}

	// Newly added, for testing purpose
	public CapabilitySummaryNew getCapabilitySummary() {
		return capabilitySummary;
	}

	// Newly added, for testing purpose
	public void setCapabilitySummary(CapabilitySummaryNew capabilitySummary) {
		this.capabilitySummary = capabilitySummary;
	}


	
	
}
