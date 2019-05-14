package decide.localControl;

import decide.OperationMode;
import decide.configuration.ConfigurationsCollection;
import decide.environment.Environment;
import decide.evaluator.PropertyEvaluator;
import decide.evaluator.QV;

public class LocalControlHandler extends LocalControl{

	/**
	 * Class constructor
	 * @param qvInstance
	 */
	public LocalControlHandler(PropertyEvaluator propertyEvaluator) {
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
			logger.info("[Initiating Component heartbeat trace]");	
		}
	
		}
		catch(Exception e)
		{
			logger.error(e.getStackTrace(),e);
		}
		
		}
	@Override
	public void execute(ConfigurationsCollection modesCollection, Environment environment, Object...args){
		System.err.println(this.getClass().getSimpleName()+".execute()");
		getPropertyEvaluator().run(modesCollection, environment, args[0]);
	}
	
	
	
	public LocalControl deepClone(Object ... args){
		LocalControl newHandler = new LocalControlHandler(this);
		newHandler.setPropertyEvaluator((PropertyEvaluator) args[0]);
		return newHandler;
	}

	@Override
	public boolean executeListeningThread() {
		return true;
	}

}
