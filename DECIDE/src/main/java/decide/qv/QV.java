package decide.qv;

import decide.configuration.ConfigurationsCollection;
import decide.environment.Environment;

public interface QV {
	
	/**
	 * 
	 * @param args
	 */
	public abstract void run(ConfigurationsCollection modesCollection, Environment environment, Object...args);

	public abstract void close();
	
	public abstract QV deepClone(Object ... args);
	
}
