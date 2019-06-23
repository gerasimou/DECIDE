package network;

public interface TransmitterDECIDE {
	
	public void send (Object ... args);
	
	public abstract TransmitterDECIDE deepClone(); 

}
