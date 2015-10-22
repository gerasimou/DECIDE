
package decide.receipt;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import network.ServerDECIDE;

public abstract class CLAReceipt implements Serializable{
	
	/** peers list */	
	protected List<ServerDECIDE> serversList;

	/** Message map from peers */
	protected Map<String, String> messagesFromPeers;

	/** ID pattern | 
	 * {C1,[199.18332939896374, 1, 449.77566639554857][153.21894802773718, 1, 307.3191855388467][346.4243336264962, 1, 759.54477487418]}  
	 * 		--> C1*/
	Pattern idPattern = Pattern.compile("\\{(.*?)\\,");
	
	/** Capability pattern
	 * 	{C1,[199.18332939896374, 1, 449.77566639554857][153.21894802773718, 1, 307.3191855388467][346.4243336264962, 1, 759.54477487418]}  
	 * 	--> [199.18332939896374, 1, 449.77566639554857][153.21894802773718, 1, 307.3191855388467][346.4243336264962, 1, 759.54477487418]
	 */
	Pattern capabilityPattern = Pattern.compile("\\[(.*?)\\]");


	/**
	 * Class constructor
	 */
	protected CLAReceipt() {
		//init parameters
		this.messagesFromPeers = new LinkedHashMap<String,String>();
	}

	
	/**
	 * Set the list of servers, i.e., where I am listening to
	 */
	public void setServersList(List<ServerDECIDE> serverList){
		this.serversList = serverList;
		//do initialisation
		for (ServerDECIDE server : this. serversList){
			//assign the CLAReceipt handler
			server.setCLAReceipt(this);
			//start the receivers
			new Thread(server, server.toString()).start();
		}
	}
	

	public void receive(String msg){
//        System.out.println("Received:\t" + msg);
		Matcher idMatcher = idPattern.matcher(msg);
		while (idMatcher.find()){
			System.out.println(idMatcher.group(1));
		}

		Matcher capabilityMatcher = capabilityPattern.matcher(msg);
		while (capabilityMatcher.find()){
			System.out.println(capabilityMatcher.group(1));
		}

	}
	
	
	public abstract void execute(Object...args);

	public abstract CLAReceipt deepClone(Object ... args);
}
