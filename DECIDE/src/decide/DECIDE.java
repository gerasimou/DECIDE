package decide;

import decide.cla.Selection;
import decide.lca.LocalCapabilityAnalysis;
import decide.lcl.LocalControl;
import decide.receipt.CLAReceipt;

public class DECIDE {
	
	/** DECIDE stages interfaces*/
	private LocalCapabilityAnalysis lca;
	private CLAReceipt				claReceipt;
	private LocalControl 			localControl;
	private Selection				selection;
	
	public DECIDE(LocalCapabilityAnalysis lca, CLAReceipt claReceipt, LocalControl localControl, Selection selection){
		this.lca 			= lca;
		this.claReceipt		= claReceipt;
		this.localControl	= localControl;
		this.selection		= selection;
	}
	
	
	public void run(){
		
	}
}
