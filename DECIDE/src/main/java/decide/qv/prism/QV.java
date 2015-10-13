package decide.qv.prism;

import java.io.Serializable;
import java.util.List;

public interface QV {

	public List<Double> run();
	
	public void closeDown();
	
	public abstract QV deepClone();
	
}
