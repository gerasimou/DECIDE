package decide;

import network.TransmitterDECIDE;

public class HeartbeatDECIDE implements Runnable{

	/** Heartbeat transmitter to DECIDE peers */
	protected TransmitterDECIDE heartbeat;

	/** time window for waiting for new capability summaries*/
	protected final long TIME_WINDOW;
	
	
	private final String HEARTBEAT = "1111";

	
	public HeartbeatDECIDE (long TIME_WINDOW) {
		//Nothing to do
		this.TIME_WINDOW = TIME_WINDOW;		
	}
	
	
	/**
	 * Assign this DECIDE instance client, i.e., where it can transmit
	 * @param client
	 */
	public void setHeartbeatTransmitterToOtherDECIDE(TransmitterDECIDE client){
		this.heartbeat = client;
	}


	
	/**
	 * Executing Heartbeat mechanism
	 */
	@Override
	public void run() {
		while (true) {
			
			try {
				//wait for some time for before sending the heartbeat
				Thread.sleep(TIME_WINDOW);
				heartbeat.send(HEARTBEAT);
			} 
			catch (InterruptedException e) {
				e.printStackTrace();
			}	
		}
	}


}
