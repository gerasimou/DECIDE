package decide.evaluator;

import decide.configuration.ConfigurationsCollection;
import decide.environment.Environment;

public interface AttributeEvaluator {
	
	/**
	 * 
	 * @param args
	 */
	public abstract void run(ConfigurationsCollection modesCollection, Environment environment, boolean adjustEnvironment);

	public abstract void close();
	
	public abstract AttributeEvaluator deepClone();
	
}
