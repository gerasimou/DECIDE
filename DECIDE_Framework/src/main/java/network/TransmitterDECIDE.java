package network;

public interface TransmitterDECIDE {
	
	public void send (Object object);
	
	public abstract TransmitterDECIDE deepClone(); 

}
