package network;


public abstract class ReceiverDECIDENew extends NetworkComponent implements Runnable {

	protected NetworkUser networkUser;
			
	protected volatile String replyMessage = "";
	
	protected  int status;

	
	
	public ReceiverDECIDENew (String serverAddress, int serverPort, ComponentTypeDECIDE networkType) {
    	super(serverAddress, serverPort, networkType);
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

	
	public void setReplyMessage(String replyMessage, boolean receivedEnvironmentMapUpdated) {
		this.replyMessage = replyMessage;
		if (receivedEnvironmentMapUpdated)
			status			  = 1;
	}

	
	public void setStatus(int status) {
		this.status = status;
	}

	
	public long getTimeStamp() {
		return timeStamp;
	}
	
	public abstract void  run();
}
