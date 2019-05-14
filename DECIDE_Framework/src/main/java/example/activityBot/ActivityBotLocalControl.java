package example.activityBot;

import org.apache.log4j.Logger;
import auxiliary.Utility;
import decide.OperationMode;
import decide.configuration.Configuration;
import decide.configuration.ConfigurationsCollection;
import decide.configuration.Mode;
import decide.environment.Environment;
import decide.evaluator.PropertyEvaluator;
import decide.localControl.LocalControl;
import example.uuv.UUVEnvironment;
import example.uuv.UUVLocalControl;
import network.PeerStatus;

public class ActivityBotLocalControl extends LocalControl{

	int number_of_steps 		= Integer.parseInt(Utility.getProperty("NUMBERofSTEPS"));
	String commandType 		= Utility.getProperty("COMMAND_TYPE");
	String initialPoint 		= Utility.getProperty("INITIAL_POINT");
	String numberOfSteps 	= Utility.getProperty("NUMBERofSTEPS");
	String clockwise 		= Utility.getProperty("CLOCKWISE");
	
	/** Logging system events*/
    final static Logger logger = Logger.getLogger(ActivityBotLocalControl.class);
    
	/**
	 * Class constructor
	 * @param qvInstance
	 */
	public ActivityBotLocalControl(PropertyEvaluator propertyEvaluator) {
		super();
		this.setPropertyEvaluator(propertyEvaluator);
//		System.out.println(this.getClass().getName());		
	}
	/**
	 * Class constructor
	 */
	public ActivityBotLocalControl() {
		super();
//		System.out.println(this.getClass().getName());		
	}
	
	/**
	 * Class <b>copy</b> constructor
	 */
	private ActivityBotLocalControl (ActivityBotLocalControl instance) {
		super();
//		this.client	= instance.client.deepClone();
	}

	/**
	 * Monitor UUV heartbeat
	 */
	@Override
	public void receive(String serverMessage){

		try {
		if (!receivedAliveMessage){
			receivedAliveMessage = true;
			if(this.atomicOperationReference.compareAndSet( OperationMode.OFFLINE, OperationMode.STABLE_MODE))
				logger.debug("ComponentOperationMode is STABLE");
				
			// check if thread sleeps, Why?
			if(timeWindow != null)
				interruptTimeWindow();	
			
			timeWindow = createNewTimeWindowInstance();
			initiateTimeWindowThread();
			logger.info("[Initiating Robot heartbeat trace]");	
		}
	
		}
		catch(Exception e)
		{
			logger.error(e.getStackTrace(),e);
		}
		
		}
	
	@Override
	public void execute(ConfigurationsCollection configurationsCollection, Environment environment, Object...args){

		logger.info("[ExecuteLocalControl]");
		//Get environment parameters input
				((ActivityBotEnvironment)environment).simulateEnvironment();
				
				// Clear optimal configuration map
				configurationsCollection.clearOptimalConfigurationMap();
				Mode mode = null;
				
				//Step 1) Carry out DECIDE-based quantitative verification
				getPropertyEvaluator().run(configurationsCollection, environment, false);
				
				// for debug 
				configurationsCollection.printAll();
				
				//Step 2) Find the best result per mode (configuration subset)
				configurationsCollection.findBestPerModeforLocalControl(environment);
				
				//Step 3) Assemble capability summary
//				StringBuilder capabilitySummary = new StringBuilder("{" + Knowledge.getID() + ",");
//				CapabilitySummary cs = new CapabilitySummary();
				while ( (mode=configurationsCollection.getNextMode()) != null){
					Configuration bestConfig 		= mode.getBestConfiguration();
					if(bestConfig != null)
					{
						configurationsCollection.insertOptimalConfiguration(bestConfig.toString(), bestConfig);
					}
//					CapabilitySummary cs = new UUVCapabilitySummary(((UUVConfiguration)bestConfig).getCSC(),
//							(double)((UUVConfiguration)bestConfig).getMeasurements(), (double)((UUVConfiguration)bestConfig).getEnergy());
//					configurationsCollection.insertCapabilitySummary("CSC"+String.valueOf(((UUVConfiguration)bestConfig).getCSC()), cs);
				}
				if(configurationsCollection.getOptimalConfigurationMapSize() > 0)
				{
					configurationsCollection.findOptimalLocalConfiguration();
				}
		
		/*
		OptimalConfiguration oc = modesCollection.getOptimalConfiguration();
		boolean assigndTrack = oc.calculateAssignedWorkload(number_of_steps);
		
		if(!assigndTrack)
			{ // Robot is offline cann't contribute
			logger.error("[No track info can be calculated]");
			this.atomicOperationReference.set(OperationMode.OFFLINE);
			System.exit(0);
			}
		
		String command = commandType+","+initialPoint+","+numberOfSteps+","+oc.getInitialPoint()+","
	        	 +oc.getDestPoint()+","+clockwise;
		sendCommand(command);
		//return this.receivedAliveMessage;*/
	}
	
	@Override
	public boolean executeListeningThread() {
		try {
			
			long timestamp = 0;
			
			
				
				Thread.sleep(TIME_WINDOW+20000);
				
			receivedTimeStamp = System.currentTimeMillis();
			
			
				timestamp = server.getTimeStamp();
				if(timestamp > 0)
				{ 
					if(logger.isDebugEnabled())
						logger.debug("[PEER:"+server.getServerAddress()+",Current TS:"+
								receivedTimeStamp+",PreviousTS:"+timestamp+",Latency:"+(receivedTimeStamp-timestamp)+"]");
					else
						logger.info("[PEER:"+server.getServerAddress()+",Latency:"+(receivedTimeStamp-timestamp)+"]");
					
					
					
					if((receivedTimeStamp - timestamp) > (TIME_WINDOW+60000))
					{
						
						server.setTimeStamp(0);
						server.getAtomicPeerStatus().set(PeerStatus.MISSING);
						//server.getCapabilitySummary().concurrentConfigurationsMap.clear();
						atomicOperationReference.set(OperationMode.OFFLINE);
						receivedAliveMessage = false;
						logger.debug("[Robot Offline]");
						//loop = false;
						// here changed for testing
						return true;
						
					}
				
				
				if(server.getAtomicPeerStatus().get()==PeerStatus.NEW_JOIN) {

			    	server.getAtomicPeerStatus().set(PeerStatus.ALIVE);
			    	atomicOperationReference.set(OperationMode.MAJOR_CHANGE_MODE);

			}
			
				
				
				}

		
		} 
		catch (InterruptedException e) {
			logger.error(e.getStackTrace());
			logger.error("[LocalControl listening thread interrupted!]");
		}
		catch (Exception e) {
			logger.error(e.getStackTrace());
		}
		return false;
	}
	
	public LocalControl deepClone(Object ... args){
		LocalControl newHandler = new ActivityBotLocalControl(this);
		newHandler.setPropertyEvaluator((PropertyEvaluator)args[0]);
		return newHandler;
	}


}
