package network;


public abstract class ReceiverDECIDE extends NetworkComponent implements Runnable {

	protected NetworkUser networkUser;
			
	protected volatile String replyMessage = "";
	
	protected  int status;
	
	protected boolean shouldStop;

	
	
	public ReceiverDECIDE (String serverAddress, int serverPort, ComponentTypeDECIDE networkType) {
    	super(serverAddress, serverPort, networkType);
    	
    	shouldStop = false;
	}
	
	
	public void setNetworkUser (NetworkUser networkUser, long timeStamp){
		this.networkUser 		= networkUser;
		this.timeStamp			= timeStamp;
		this.status 			= 1;
	}
	
	
	
	public int getStatus() {
		return status;
	}
	

	public String getReplyMessage() {
		return replyMessage;
	}

	
	public void setReplyMessage(String replyMessage, boolean receivedEnvironmentMapUpdated, int priority) {
		if (priority < status)
			return;
		this.replyMessage = replyMessage;
//		if (receivedEnvironmentMapUpdated && priority > status)
			status			  = priority;
	}

	
	public void setStatus(int status) {
		this.status = status;
	}

	
	public long getTimeStamp() {
		return timeStamp;
	}
	
	public abstract void  run();

	public abstract void  restart();

}
