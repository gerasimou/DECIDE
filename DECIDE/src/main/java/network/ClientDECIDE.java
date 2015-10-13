package network;

public interface ClientDECIDE {
	
	public void send (String message);
	
	public abstract ClientDECIDE deepClone();

}
