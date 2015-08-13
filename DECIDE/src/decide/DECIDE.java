package decide;

import java.io.Serializable;
import java.util.List;

import auxiliary.Utility;
import decide.cla.Selection;
import decide.cla.SelectionHandler;
import decide.lca.LocalCapabilityAnalysis;
import decide.lca.LocalCapabilityAnalysisHandler;
import decide.lcl.LocalControl;
import decide.lcl.LocalControlHandler;
import decide.receipt.CLAReceipt;
import decide.receipt.CLAReceiptHandler;
import network.ClientDECIDE;
import network.MulticastReceiver;
import network.MulticastTransmitter;
import network.ServerDECIDE;

public class DECIDE implements Cloneable, Serializable{
	
	/** DECIDE stages interfaces*/
	private LocalCapabilityAnalysis lca;
	private CLAReceipt				claReceipt;
	private LocalControl 			localControl;
	private Selection				selection;
	private MulticastTransmitter	transmitter;
	private List<MulticastReceiver>	receiversList;
	
	
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
		for (MulticastReceiver receiver : receiversList){
			new Thread(receiver, receiver.toString()).start();;
		}
//		new Thread(transmitter, transmitter.toString()).start();
//		claReceipt.execute();
//		selection.execute();
//		localControl.execute();
	}
	

	public void transmit(){
		new Thread(transmitter, transmitter.toString()).start();
	}
	
	public void setTransmitter(MulticastTransmitter transmitter){
		this.transmitter = transmitter;
		lca.setTransmitter(transmitter);
	}
	
	
	public void setReceivers(List<MulticastReceiver> peersList){
		this.receiversList = peersList;
		claReceipt.setPeersList(peersList);
	}
	
	
	@Deprecated
	public void setPeersList(List<ClientDECIDE> peersList){
		lca.setPeersList( peersList);
	}
	
	
	@Deprecated
	public void setServer(ServerDECIDE server){
		claReceipt.setServerDECIDE(server);
	}
	
	
	
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
	
	
	public DECIDE deepClone(){ 
		return new DECIDE(this);
	}
	
	
	private DECIDE (DECIDE decide){
		this.lca 			= (LocalCapabilityAnalysis) Utility.deepCopy(decide.lca);
		this.claReceipt		= decide.claReceipt.deepClone();
		this.selection		= (Selection)Utility.deepCopy(decide.selection);
		this.localControl	= (LocalControl)Utility.deepCopy(decide.localControl);
	}
	

	public String toString(){
		return this.hashCode() +","+ lca +","+ claReceipt +","+ selection +","+ localControl;
	}

	
}
