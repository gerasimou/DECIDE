package decide.component;

import java.util.ArrayList;
import java.util.List;

import decide.DECIDE;
import decide.component.requirements.Requirement;
import network.ClientSocketDECIDE;
import network.ServerSocketDECIDE;

public class Component implements Runnable{

	/** List of global (system-level) requirements (constraints + objectives)*/
	private List<Requirement> requirementsGlobalList;

	/** List of local (component-level) requirements (constraints + objectives)*/
	private List<Requirement> requirementsLocalList;
	
	/** DECIDE protocol handler for this component*/
	private DECIDE decide;
	
	/** Component ID*/
	private String id;
	
	/** Server handler */
	private ServerSocketDECIDE server;
	
	/** Peers list*/
	private List<ClientSocketDECIDE> peersList;
	
	
	
	/**
	 * Default constructor
	 */
	protected Component(String id, DECIDE decide) {
		//init component's id
		this.id = id;
		
		//init global and local requirement lists
		this.requirementsGlobalList = new ArrayList<Requirement>();
		this.requirementsLocalList	= new ArrayList<Requirement>();
		
//		//Server handler
//		this.server = new ServerDECIDE(Integer.parseInt(listeningPort));
//		
//		//initialise peers list
//		this.peersList = new ArrayList<ClientDECIDE>();
//		for (String[] peerArray : peersDetailsList){
//			this.peersList.add(new ClientDECIDE(peerArray[0], Integer.parseInt(peerArray[1])));
//		}
		
		//DECIDE handler
		this.decide = decide;
	}


	/**
	 * Get component ID
	 * @return ID String
	 */
	public String getID(){
		return this.id;
	}
	
	
	/**
	 * Set the decide handler
	 * @param decide
	 */
	public void setDECIDE(DECIDE decide){
		this.decide = decide;
	}
	
	
	/**
	 * Get the <b>DECIDE</b> instance
	 * @return
	 */
	public DECIDE getDECIDE(){
		return this.decide;
	}
		
	
	public void run(){
		decide.run();
	}	
	
	
}
