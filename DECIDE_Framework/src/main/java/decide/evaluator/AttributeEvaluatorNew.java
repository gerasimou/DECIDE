package decide.evaluator;


public interface AttributeEvaluatorNew {
	
	/**
	 * 
	 * @param args
	 */
//	public abstract void run(ConfigurationsCollectionNew modesCollection, Environment environment, boolean adjustEnvironment);

	public abstract double run(String model, String property);

	
	public abstract void close();
	
	public abstract AttributeEvaluatorNew deepClone();
	
}
