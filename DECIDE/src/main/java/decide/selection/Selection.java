package decide.selection;

import java.io.Serializable;

import decide.localAnalysis.LocalCapabilityAnalysis;

public abstract class Selection implements Serializable{
	
	public abstract void execute(Object...args);
	
	public abstract Selection deepClone(Object ... args);

}
