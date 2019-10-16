package network;

public abstract class TransmitterDECIDE extends NetworkComponent{
	

    public abstract void send (Object object);
	
    
    public TransmitterDECIDE (String serverAddress, int serverPort, ComponentTypeDECIDE networkType) {
    	super(serverAddress, serverPort, networkType);
    }

}
