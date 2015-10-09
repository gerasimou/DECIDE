package decide.lcl;

import java.io.Serializable;

public abstract class LocalControl implements Serializable{
	
	public abstract void execute(Object...args);
}
