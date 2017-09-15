package network;

public interface ClientDECIDE {
	
	public void send (Object message);
	
	public abstract ClientDECIDE deepClone();

}
