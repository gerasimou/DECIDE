package decide.localControl;

import decide.StatusRobot;
import decide.configuration.ConfigurationsCollection;
import decide.environment.Environment;
import decide.evaluator.AttributeEvaluator;
import decide.evaluator.QV;

public class LocalControlHandler extends LocalControl{

	/**
	 * Class constructor
	 * @param qvInstance
	 */
	public LocalControlHandler(AttributeEvaluator propertyEvaluator) {
		super();
		this.setPropertyEvaluator(propertyEvaluator);
//		System.out.println(this.getClass().getName());		
	}
	
	/**
	 * Class constructor
	 * 
	 */
	public LocalControlHandler() {
		super();
		
//		System.out.println(this.getClass().getName());		
	}
	
	
	/**
	 * Class <b>copy</b> constructor
	 */
	private LocalControlHandler (LocalControlHandler instance) {
		super();
//		this.client	= instance.client.deepClone();
	}

	/**
	 * Monitor Component heartbeat
	 */
	@Override
	public void receive (String serverAddress, Object message){
		try {
		if (!receivedAliveMessage){
			receivedAliveMessage = true;
			if(this.atomicOperationReference.compareAndSet( StatusRobot.IDLE, StatusRobot.STABLE))
				logger.debug("ComponentOperationMode is STABLE");
				
			// check if thread sleeps, Why?
			if(timeWindow != null)
				interruptTimeWindow();	
			
			timeWindow = createNewTimeWindowInstance();
			initiateTimeWindowThread();
			logger.info("[Initiating Component heartbeat trace]");	
		}
	
		}
		catch(Exception e)
		{
			logger.error(e.getStackTrace(),e);
		}
		
		}
	@Override
	public void execute(ConfigurationsCollection modesCollection, Environment environment){
		System.err.println(this.getClass().getSimpleName()+".execute()");
		getPropertyEvaluator().run(modesCollection, environment, false);
	}
	
	
	
	public LocalControl deepClone(Object ... args){
		LocalControl newHandler = new LocalControlHandler(this);
		newHandler.setPropertyEvaluator((AttributeEvaluator) args[0]);
		return newHandler;
	}

	@Override
	public boolean executeListeningThread() {
		return true;
	}


}
