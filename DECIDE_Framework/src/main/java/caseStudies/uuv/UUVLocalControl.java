package caseStudies.uuv;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import auxiliary.Utility;
import decide.OperationMode;
import decide.configuration.Configuration;
import decide.configuration.ConfigurationsCollection;
import decide.configuration.Mode;
import decide.environment.Environment;
import decide.evaluator.AttributeEvaluator;
import decide.localControl.LocalControl;
import network.PeerStatus;


public class UUVLocalControl extends LocalControl{
	
	/** Logging system events*/
    final static Logger logger = Logger.getLogger(UUVLocalControl.class);
    
    /** Local map stores received UUV sensor readings*/
    Map<String, Object> receivedEnvironmentMap;
    
    volatile String pushedMessage = null; 
    
    final int [] modResult = new int []{0,1,3}; 
    
	/**
	 * Class constructor
	 * @param qvInstance
	 */
	public UUVLocalControl(AttributeEvaluator propertyEvaluator) {
		super();
		receivedEnvironmentMap = new ConcurrentHashMap<>();
		initEnvironment();
		this.attributeEvaluator = propertyEvaluator;
	}

	
	/**
	 * Class constructor
	 */
	public UUVLocalControl() {
		super();
//		System.out.println(this.getClass().getName());		
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
				logger.info("[Initiating UUV heartbeat trace]");	
			}

			// update environment map based on received UUV sensor readings, 
			String [] receivedReadings = serverMessage.split(",");
			logger.info("[UUVSensorReading: "+serverMessage+"]");
		
			if(receivedReadings.length !=3) {
				logger.error("Format error UUV sensor reading");
			}
			else {	//Update environment map based on received UUV sensor reading.
				for(int i=1;i<=3;i++)
				receivedEnvironmentMap.put("r"+i, Double.parseDouble(receivedReadings[i-1].replaceAll("\\s+","")));	
//				while(pushedMessage == null)
//					Thread.sleep(2000);
//				serverMessage = pushedMessage;
//				pushedMessage = null;
			}
		}
		catch(Exception e) {
			logger.error(e.getStackTrace(),e);
		}			
	}
	
	
	/**
	 * Class <b>copy</b> constructor
	 */
	private UUVLocalControl (UUVLocalControl instance) {
		super();
//		this.client	= instance.client.deepClone();
	}

	
	@Override
	public void execute(ConfigurationsCollection configurationsCollection, Environment environment){
		logger.info("execute UUVLocalControl");
		
		//Update environment map based on received UUV sensor reading
		receivedEnvironmentMap.forEach((k, v) -> environment.updateEnvironmentElement(k, v));
		
		//Get environment parameters input
		((UUVEnvironment)environment).simulateEnvironment();
		
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
//		StringBuilder capabilitySummary = new StringBuilder("{" + Knowledge.getID() + ",");
//		CapabilitySummary cs = new CapabilitySummary();
		while ( (mode=configurationsCollection.getNextMode()) != null){
			Configuration bestConfig 		= mode.getBestConfiguration();
			if(bestConfig != null) {
				configurationsCollection.insertOptimalConfiguration(bestConfig.toString(), bestConfig);
			}
		}
		
		if(configurationsCollection.getOptimalConfigurationMapSize() > 0) {
			if(configurationsCollection.findOptimalLocalConfiguration()) {
				UUVConfiguration config = (UUVConfiguration)configurationsCollection.getOptimalConfiguration();
				String configMessage="";
				int csc = config.getCSC();

				configMessage = csc +"," + config.getSpeed();
//				configMessage += String.valueOf(config.getSpeed());
				this.receiver.setReplyMessage(configMessage);
				logger.info("[Send UUV:"+configMessage);				
			}
		}
		else
			this.atomicOperationReference.set(OperationMode.MAJOR_LOCAL_CHANGE_MODE);	
	}
	
	protected void initEnvironment(){
		Random rand = new Random(System.currentTimeMillis());
		receivedEnvironmentMap.put("r1", Double.parseDouble(Utility.getProperty("r1")) + (rand.nextGaussian()*0.166)+ 0.5);
		receivedEnvironmentMap.put("r2", Double.parseDouble(Utility.getProperty("r2")) + (rand.nextGaussian()*0.166)+ 0.5);
		receivedEnvironmentMap.put("r3", Double.parseDouble(Utility.getProperty("r3")) + (rand.nextGaussian()*0.166)+ 0.5);
	}
	
	
	@Override
	public boolean executeListeningThread() {
		try {
			
			long timestamp = 0;
			
			Thread.sleep(TIME_WINDOW+20000);
				
			receivedTimeStamp = System.currentTimeMillis();
				
			timestamp = receiver.getTimeStamp();
				
			if(timestamp > 0) { 
				if(logger.isDebugEnabled())
					logger.debug("[PEER:"+receiver.getServerAddress()+",Current TS:"+
							receivedTimeStamp+",PreviousTS:"+timestamp+",Latency:"+(receivedTimeStamp-timestamp)+"]");
				else
					logger.info("[PEER:"+receiver.getServerAddress()+",Latency:"+(receivedTimeStamp-timestamp)+"]");
				
				if ((receivedTimeStamp - timestamp) > (TIME_WINDOW+60000)) {
					receiver.setTimeStamp(0);
					receiver.getAtomicPeerStatus().set(PeerStatus.MISSING);
					//server.getCapabilitySummary().concurrentConfigurationsMap.clear();
					atomicOperationReference.set(OperationMode.OFFLINE);
					receivedAliveMessage = false;
					logger.debug("[UUV Offline]");
					//loop = false;
					// here changed for testing
					return true;
				}
			
				if(receiver.getAtomicPeerStatus().get()==PeerStatus.NEW_JOIN) {

				    	receiver.getAtomicPeerStatus().set(PeerStatus.ALIVE);
				    	// commented for testing
				    	//atomicOperationReference.set(OperationMode.MAJOR_CHANGE_MODE)
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
		LocalControl newHandler = new UUVLocalControl(this);
		newHandler.setPropertyEvaluator((AttributeEvaluator)args[0]);
		return newHandler;
	}
}
