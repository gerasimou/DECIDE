package decide;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;

import auxiliary.Utility;
import decide.capabilitySummary.CapabilitySummary;
import decide.capabilitySummary.CapabilitySummaryCollection;
import decide.configuration.ConfigurationsCollection;
import decide.environment.Environment;
import decide.evaluator.PropertyEvaluator;
import decide.localAnalysis.LocalCapabilityAnalysis;
import decide.localAnalysis.LocalCapabilityAnalysisHandler;
import decide.localControl.LocalControl;
import decide.localControl.LocalControlHandler;
import decide.qv.prism.PrismQV;
import decide.receipt.CLAReceipt;
import decide.receipt.CLAReceiptHandler;
import decide.selection.Selection;
import decide.selection.SelectionHandler;
import network.ClientDECIDE;
import network.ServerDECIDE;
import network.ServerDatagramSocket;

public class DECIDE implements Cloneable, Serializable{
	
	/** DECIDE stages interfaces*/
	private LocalCapabilityAnalysis 		lca;
	private CLAReceipt					claReceipt;
	private LocalControl 				localControl;
	private Selection					selection;
	private CapabilitySummary			capabilitySummary;
	
	/** QV handler */
	private PropertyEvaluator propertyEvaluator;

	
	/** configuration */
	private ConfigurationsCollection 	configurationsCollection;
	
	/** capability Summary collection  */
	private CapabilitySummaryCollection 	capabilitySummaryCollection;
	
	/** environment */
	private Environment					environment;
	
	
	
	/** Logging system events*/
    final static Logger logger = Logger.getLogger(DECIDE.class);
	
	
	/** 
	 * Default constructor: instantiates the default handlers
	 */
	public DECIDE(ConfigurationsCollection configurationsCollection, CapabilitySummaryCollection capabilitySummaries, Environment environment){
		this(null, null, null, null, configurationsCollection, capabilitySummaries, environment);
		this.configurationsCollection	= configurationsCollection;
		this.environment					= environment;
		this.capabilitySummaryCollection	= capabilitySummaries;		
	}
	
	
	/** 
	 * Default constructor: instantiates the default handlers
	 */
	public DECIDE(ConfigurationsCollection configurationsCollection, CapabilitySummaryCollection capabilitySummaries, Environment environment, 
			Class<? extends LocalCapabilityAnalysis> localClass, Class<? extends CLAReceipt> claReciptClass ,Class<? extends Selection> selectionClass,
			Class<? extends LocalControl> localControlClass){
		try {
			this.propertyEvaluator	= new PrismQV();
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
	 */
	public DECIDE(LocalCapabilityAnalysis lca, CLAReceipt claReceipt, Selection selection, LocalControl localControl,
			ConfigurationsCollection configurationsCollection, CapabilitySummaryCollection capabilitySummaries, Environment  environment){
		
		//if a PropertyEvaluator instance exists -> use it, otherwise instantiate a PrismQV (default) instance
		if (lca!=null)
			this.propertyEvaluator = lca.getPropertyEvaluator();
		else if (localControl!=null)
			this.propertyEvaluator = localControl.getPropertyEvaluator();
		else
			this.propertyEvaluator	= new PrismQV();
		
		//if CLAReceipt is null -> instantiate the default handler
		if (lca == null)
			this.lca = new LocalCapabilityAnalysisHandler(propertyEvaluator);
		else
			this.lca = lca;

		//if CLAReceipt is null -> instantiate the default handler
		if (claReceipt == null)
			this.claReceipt = new CLAReceiptHandler();
		else
			this.claReceipt = claReceipt;
		
		//if Selection is null -> instantiate the default handler
		if (selection == null)
			this.selection = new SelectionHandler();
		else
			this.selection = selection;
		
		//if LocalControl is null -> instantiate the default handler
		if (localControl == null)
			this.localControl = new LocalControlHandler(propertyEvaluator);
		else
			this.localControl = localControl;
		
		this.configurationsCollection		= configurationsCollection;
		this.capabilitySummaryCollection		= capabilitySummaries;
		this.environment						= environment;
	}
	
	
	/**
	 * Constructor accepting a <b>LocalCapabilityAnalysis</b> instance
	 * @param lca
	 */
	public DECIDE(LocalCapabilityAnalysis lca, ConfigurationsCollection configurationsCollection, CapabilitySummaryCollection capabilitySummaries, Environment  environment){
		this(lca, null, null, null, configurationsCollection, capabilitySummaries, environment);
	}
	
	
	
	/**
	 * Log system events to console or file
	 * @param String log
	 */
	private void logEvents(String parameter){

		if(logger.isDebugEnabled())
			logger.debug("[debug] : " + parameter);
		else
			logger.info("[info] : " + parameter);

		//logger.error("This is error : " + parameter);
	}
	/**
	 * Constructor accepting a <b>CLAReceipt</b> instance
	 * @param claReceipt
	 */
	public DECIDE(CLAReceipt claReceipt, ConfigurationsCollection configurationsCollection, CapabilitySummaryCollection capabilitySummaries, Environment  environment){
		this(null, claReceipt, null, null, configurationsCollection, capabilitySummaries, environment);
	}
	

	/**
	 * Constructor accepting a <b>Selection</b> instance
	 * @param selection
	 */
	public DECIDE(Selection selection, ConfigurationsCollection configurationsCollection, CapabilitySummaryCollection capabilitySummaries, Environment  environment){
		this(null, null, selection, null, configurationsCollection, capabilitySummaries, environment);
	}
	
	
	/**
	 * Constructor accepting a <b>LocalControl</b> instance
	 * @param localControl
	 */
	public DECIDE (LocalControl localControl, ConfigurationsCollection configurationsCollection, CapabilitySummaryCollection capabilitySummaries, Environment  environment){
		this (null, null, null, localControl, configurationsCollection, capabilitySummaries, environment);
	}
	
	
	/**
	 * Class <b>copy</b> constructor
	 * @param decide
	 */
	private DECIDE (DECIDE decide){
		this.propertyEvaluator	= decide.propertyEvaluator.deepClone();
		this.lca 				= decide.lca.deepClone(this.propertyEvaluator);// (LocalCapabilityAnalysis) Utility.deepCopy(decide.lca);
		this.claReceipt			= decide.claReceipt.deepClone();
		this.selection			= (Selection)Utility.deepCopy(decide.selection);
		this.localControl		= decide.localControl.deepClone(this.propertyEvaluator);//(LocalControl)Utility.deepCopy(decide.localControl);
	}

	


	/**
	 * Run <b>DECIDE</b> protocol
	 */
	public void run(){
		long delay = Long.parseLong(Utility.getProperty("DELAY", "10000"));
		//boolean initialRun = true;
		boolean temp = false;
		
		try{
			while (true){
				Thread.sleep(delay); 
				
				if(this.localControl.getAtomicOperationReference().get() == OperationMode.MAJOR_LOCAL_CHANGE_MODE || this.localControl.getAtomicOperationReference().get() == OperationMode.STARTUP)
				{
					switch (localControl.getAtomicOperationReference().get())
					{
					case MAJOR_LOCAL_CHANGE_MODE:
					this.localControl.getAtomicOperationReference().set(OperationMode.STABLE_MODE);
					this.claReceipt.getAtomicOperationReference().set(OperationMode.MAJOR_CHANGE_MODE);
					break;
					case STARTUP:
					this.localControl.getAtomicOperationReference().set(OperationMode.STABLE_MODE); // Used to be Offline, changed for testing purpose
					this.claReceipt.getAtomicOperationReference().set(OperationMode.MAJOR_CHANGE_MODE);
					break;
					default: break;
					}
					
					lca.execute(configurationsCollection, environment, true);
					
					if(logger.isDebugEnabled())
					configurationsCollection.printAll();

				}
				// why resetting local control to stable and claReceipt to Major change mode, and this block never executed
//				if(this.localControl.getAtomicOperationReference().get() == OperationMode.MAJOR_CHANGE_MODE)
//				{
//					this.localControl.getAtomicOperationReference().set(OperationMode.STABLE_MODE);
//					this.claReceipt.getAtomicOperationReference().set(OperationMode.MAJOR_CHANGE_MODE);
//				}
				// share CapabilitySummary periodically (heart beat message)
				if(!(this.localControl.getAtomicOperationReference().get() == OperationMode.OFFLINE))
				{
					lca.shareCapabilitySummary(configurationsCollection.getCapabilitySummariesArray());
					if(logger.isDebugEnabled())
					logger.debug("[Send:" +Arrays.toString(configurationsCollection.getCapabilitySummariesArray())+"]");
				}

//				System.out.println("\n\nPrinting best from each mode\n");
//				configurationsCollection.printBestFromMode();
				
				Thread.sleep(5000);
				
				if((claReceipt.getAtomicOperationReference().get() == OperationMode.MAJOR_CHANGE_MODE))//||(localControl.getAtomicOperationReference().get() == OperationMode.MAJOR_LOCAL_CHANGE_MODE))
				{
					logger.debug("[ClaMode "+claReceipt.getAtomicOperationReference().get()+"]");
//					
					
					claReceipt.getAtomicOperationReference().compareAndSet(OperationMode.MAJOR_CHANGE_MODE, OperationMode.STABLE_MODE);
					
//					
				temp = selection.execute(this.configurationsCollection, this.capabilitySummaryCollection, this.environment);
				if(!temp)
				{
					logEvents("Component"+Knowledge.getID()+"[idle]: Could not find feasible plan");
					localControl.getAtomicOperationReference().set(OperationMode.IDEL);
					localControl.setReceivedNewCommand(false);
					// You could order attached component to stop action.
	
				}
				else
				{
					logger.debug("[Component"+Knowledge.getID()+" has task]");
					if(localControl.getAtomicOperationReference().get() != OperationMode.OFFLINE)
					{
					localControl.getAtomicOperationReference().compareAndSet(OperationMode.IDEL, OperationMode.STABLE_MODE);
					localControl.setReceivedNewCommand(true);
					logger.debug("[Component "+localControl.getAtomicOperationReference().get()+" NewCommand "+localControl.isReceivedNewCommand()+"]");
					
					
					}
					
				}
					
				
				}
				
				if((localControl.getAtomicOperationReference().get() == OperationMode.STABLE_MODE))//&&(localControl.isReceivedNewCommand()))  // Comments were placed for testing
				{
					
					localControl.execute(configurationsCollection,environment,false);
					localControl.setReceivedNewCommand(false);

				}
				
				/*
				 * FileReader.command[arrayflag][1]+","+FileReader.command[arrayflag][2]+","+FileReader.command[arrayflag][3]+","+FileReader.command[arrayflag][4]+","
        	 +FileReader.command[arrayflag][5]+","+FileReader.command[arrayflag][6];
        	 //C,6,10,2,4,1
        	  * COMMAND_TYPE=C
INITIAL_POINT=6
NUMBERofSTEPS=10
POINT_1=2
POINT_2=4
CLOCKWISE=1
				 */
		
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
	 * Set the DECIDE remote client, i.e., where DECIDE can transmit
	 * @param client
	 */
	public void setRemoteClient(ClientDECIDE client){
		
		localControl.assignClient(client);
	}
	
	/**
	 * Set the DECIDE listening server for remote client, i.e., where DECIDE can receive
	 * @param server
	 */
	public void setRemoteServer(ServerDECIDE server){
		
		localControl.assignServer(server);
	}
	
	/**
	 * Set the DECIDE client, i.e., where DECIDE can transmit
	 * @param client
	 */
	public void setClient(ClientDECIDE client){
		lca.assignClient(client);
	}
	
	
	/** 
	 * Set the DECIDE servers, i.e., where DECIDE can receive messages
	 * @param serverList
	 */
	public void setServersList(List<ServerDECIDE> serverList){
		claReceipt.setServersList(serverList, capabilitySummaryCollection);
	}
	
	
	/**
	 * Clone <b>this</b> DECIDE object 
	 */
	public DECIDE clone(){
		try {
			return (DECIDE) super.clone();
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
	public DECIDE deepClone(){ 
		return new DECIDE(this);
	}

	
	/**
	 * Overloaded toString  method
	 */
	@Override
	public String toString(){
		return this.hashCode() +","+ lca +","+ claReceipt +","+ selection +","+ localControl;
	}

	// Newly added, for testing purpose
	public ConfigurationsCollection getConfigurationsCollection() {
		return configurationsCollection;
	}

	// Newly added, for testing purpose
	public LocalCapabilityAnalysis getLca() {
		return lca;
	}

	// Newly added, for testing purpose
	public CapabilitySummary getCapabilitySummary() {
		return capabilitySummary;
	}

	// Newly added, for testing purpose
	public void setCapabilitySummary(CapabilitySummary capabilitySummary) {
		this.capabilitySummary = capabilitySummary;
	}


	
	
}
