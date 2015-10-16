package decide.qv;

public interface QV {
	
	public abstract void run(Object ... args);

	public abstract void close();
	
	public abstract QV deepClone(Object ... args);
	
}
