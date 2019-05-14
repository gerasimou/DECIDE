package network;

public interface ClientDECIDE {
	
	public void send (Object ... args);
	
	public abstract ClientDECIDE deepClone();

}
