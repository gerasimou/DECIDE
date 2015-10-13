package decide;

import java.io.Serializable;
import java.util.List;

import auxiliary.Utility;
import decide.localAnalysis.LocalCapabilityAnalysis;
import decide.localAnalysis.LocalCapabilityAnalysisHandler;
import decide.localControl.LocalControl;
import decide.localControl.LocalControlHandler;
import decide.qv.prism.ModelChecker;
import decide.qv.prism.PrismAPI;
import decide.qv.prism.QV;
import decide.receipt.CLAReceipt;
import decide.receipt.CLAReceiptHandler;
import decide.selection.Selection;
import decide.selection.SelectionHandler;
import network.ClientDECIDE;
import network.ServerDECIDE;

public class DECIDE implements Cloneable, Serializable{
	
	/** DECIDE stages interfaces*/
	private LocalCapabilityAnalysis lca;
	private CLAReceipt				claReceipt;
	private LocalControl 			localControl;
	private Selection				selection;
	
	/** QV handler */
	private QV qv;

	/** this DECIDE ID */
	private String					ID;
	
	/**
	 * Default constructor: instantiates the default handlers
	 */
	public DECIDE(String ID){
		this(null, null, null, null);
		this.ID  	= ID;
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
		
		if (lca!=null)
			this.qv = lca.getQV();
		else if (localControl!=null)
			this.qv = localControl.getQV();
		else
			this.qv	= new PrismAPI();
		
		
		//if CLAReceipt is null -> instantiate the default handler
		if (lca == null)
			this.lca = new LocalCapabilityAnalysisHandler(qv);
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
			this.localControl = new LocalControlHandler(qv);
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
		long delay = Long.parseLong(Utility.getProperty("DELAY", "2000"));
		try{
			while (true){
				lca.execute(this.ID);
				try{
					Thread.sleep(delay);
				}
				catch (InterruptedException ie){
					ie.printStackTrace();
				}
//				claReceipt.execute();
//				selection.execute();
//				localControl.execute();
			}
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Set the DECIDE client, i.e., where DECIDE can transmit
	 * @param client
	 */
	public void setClient(ClientDECIDE client){
		lca.assignClient(client);
	}
	
	
	/** 
	 * Set the DECIDE servers, i.e., where DECIDE can receive messages
	 * @param serverList
	 */
	public void setServersList(List<ServerDECIDE> serverList){
		claReceipt.setServersList(serverList);
	}
	
	
	/**
	 * Clone <b>this</b> DECIDE object 
	 */
	public DECIDE clone(){
		try {
			return (DECIDE) super.clone();
		}
		catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		System.exit(-1);
		return null;
	}
	
	
	/**
	 * <b>Deep clone</b> of this DECIDE object
	 * @return
	 */
	public DECIDE deepClone(){ 
		return new DECIDE(this);
	}
	
	
	/**
	 * Copy constructor
	 * @param decide
	 */
	private DECIDE (DECIDE decide){
		this.qv				= decide.qv.deepClone();
		this.lca 			= decide.lca.deepClone();// (LocalCapabilityAnalysis) Utility.deepCopy(decide.lca);
		this.claReceipt		= decide.claReceipt.deepClone();
		this.selection		= (Selection)Utility.deepCopy(decide.selection);
		this.localControl	= decide.localControl.deepClone();//(LocalControl)Utility.deepCopy(decide.localControl);
		this.ID				= decide.ID;
	}

	
	/**
	 * Overloaded toString  method
	 */
	@Override
	public String toString(){
		return this.hashCode() +","+ lca +","+ claReceipt +","+ selection +","+ localControl;
	}

	
}
