package decide.selection;

import java.io.Serializable;

public abstract class Selection implements Serializable{
	
	public abstract void execute(Object...args);
}
