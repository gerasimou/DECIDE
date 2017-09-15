package decide.localControl;

import java.io.Serializable;

import decide.configuration.ConfigurationsCollection;
import decide.environment.Environment;
import decide.qv.QV;

public abstract class LocalControl implements Serializable{
	
	/** QV handler */
	protected QV qv;
	
	
	/**
	 * Class constructor
	 * @param qvInstance
	 */
	public LocalControl(){
	}
	
	
	/**
	 * Return the QV instance
	 * @return
	 */
	public QV getQV(){
		return this.qv;
	}
	
	
	/**
	 * <b>Abstact</b> execute action
	 * @param args
	*/
	public abstract void execute(ConfigurationsCollection modesCollection, Environment environment, Object...args);
	
	
	public abstract LocalControl deepClone(Object ... args);

}
