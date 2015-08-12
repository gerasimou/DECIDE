package decide;

import decide.cla.Selection;
import decide.cla.SelectionHandler;
import decide.lca.LocalCapabilityAnalysis;
import decide.lca.LocalCapabilityAnalysisHandler;
import decide.lcl.LocalControl;
import decide.lcl.LocalControlHandler;
import decide.receipt.CLAReceipt;
import decide.receipt.CLAReceiptHandler;

public class DECIDE {
	
	/** DECIDE stages interfaces*/
	private LocalCapabilityAnalysis lca;
	private CLAReceipt				claReceipt;
	private LocalControl 			localControl;
	private Selection				selection;
	
	
	/**
	 * Default constructor: instantiates the default handlers
	 */
	public DECIDE(){
		this(null, null, null, null);
	}
	
	
	/**
 	 * Constructor requiring all components of DECIDE framework. 
 	 * If any is null, the default handler will be instantiated.
	 * @param lca
	 * @param claReceipt
	 * @param selection
	 * @param localControl
	 */
	public DECIDE(LocalCapabilityAnalysis lca, CLAReceipt claReceipt, Selection selection, LocalControl localControl){
		//if CLAReceipt is null -> instantiate the default handler
		if (lca == null)
			this.lca = new LocalCapabilityAnalysisHandler();
		else
			this.lca = lca;

		//if CLAReceipt is null -> instantiate the default handler
		if (claReceipt == null)
			this.claReceipt = new CLAReceiptHandler();
		else
			this.claReceipt = claReceipt;
		
		//if Selection is null -> instantiate the default handler
		if (selection == null)
			this.selection = new SelectionHandler();
		else
			this.selection = selection;
		
		//if LocalControl is null -> instantiate the default handler
		if (localControl == null)
			this.localControl = new LocalControlHandler();
		else
			this.localControl = localControl;
	}
	
	
	/**
	 * Constructor accepting a <b>LocalCapabilityAnalysis</b> instance
	 * @param lca
	 */
	public DECIDE(LocalCapabilityAnalysis lca){
		this(lca, null, null, null);
	}
	

	/**
	 * Constructor accepting a <b>CLAReceipt</b> instance
	 * @param claReceipt
	 */
	public DECIDE(CLAReceipt claReceipt){
		this(null, claReceipt, null, null);
	}
	

	/**
	 * Constructor accepting a <b>Selection</b> instance
	 * @param selection
	 */
	public DECIDE(Selection selection){
		this(null, null, selection, null);
	}
	
	
	/**
	 * Constructor accepting a <b>LocalControl</b> instance
	 * @param localControl
	 */
	public DECIDE (LocalControl localControl){
		this (null, null, null, localControl);
	}
	
	
	/**
	 * Run <b>DECIDE</b> protocol
	 */
	public void run(){
		localControl.execute();
		claReceipt.execute();
		selection.execute();
		localControl.execute();
	}
}
