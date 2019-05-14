package decide.evaluator;

import decide.configuration.ConfigurationsCollection;
import decide.environment.Environment;

public interface PropertyEvaluator {
	
	/**
	 * 
	 * @param args
	 */
	public abstract void run(ConfigurationsCollection modesCollection, Environment environment, Object...args);

	public abstract void close();
	
	public abstract PropertyEvaluator deepClone(Object ... args);
	
}
