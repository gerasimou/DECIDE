package decide.component;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import auxiliary.Utility;
import decide.DECIDE;
import network.ClientDECIDE;
import network.MulticastReceiver;
import network.MulticastTransmitter;
import network.ServerDECIDE;

public class ComponentFactory {

	public static List<Component> createComponents(DECIDE decide){
		Utility.setup();
		
		//Get the properties set
		Set<Entry<Object,Object>> propertiesSet = Utility.getPropertiesEntrySet();
		
		List<Component> componentsList = new ArrayList<Component>();
		
		//Get the iterator
		Iterator<Entry<Object,Object>> iterator = propertiesSet.iterator();
				
		while (iterator.hasNext()){
			Entry<Object, Object> entry = iterator.next();
			String key					= entry.getKey().toString();
			String value				= entry.getValue().toString().replaceAll("\\s+","");//remove whitespaces
			
			//create the components list
			if (key.contains("COMPONENT")){
				componentsList.add(makeNewComponentMulticast(key, value, decide));
			}//if			
		}//while
		
		
		return componentsList;
	}
	
	
	
	private static Component makeNewComponentMulticast(String id, String features, DECIDE decide){
		//get all component features
		String[] featuresList	= features.split(",");
		
		
		String componentID = null;
		//new DECIDE transmitter
		MulticastTransmitter transmitter = null;
		//create a list of peers
		List<MulticastReceiver> peersList = new ArrayList<MulticastReceiver>();

		for (String feature : featuresList){

			//get component's ID
			if (feature.contains("ID")){
				//get its ID
				componentID = feature.split(":")[1];				
			}
			
			//get component's transmitting address
			if (feature.contains("TRANSMITTING")){
				String transmittingAddress	= feature.split(":")[1];
				int transmittingPort 		= Integer.parseInt(feature.split(":")[2]);
				//create a new DECIDE transmitter
				transmitter 				= new MulticastTransmitter(transmittingAddress, transmittingPort);
			}
			
			//find the information of its peers
			if (feature.contains("RECEIVING")){
				String peerAddress	= feature.split(":")[1];
				int peerPort 		= Integer.parseInt(feature.split(":")[2]);
				peersList.add(new MulticastReceiver(peerAddress, peerPort));
			}			
		}
		
		//clone the DECIDE instance given by the user
		DECIDE newDECIDE = decide.deepClone();
		
		//set the DECIDE transmitter and receivers
		newDECIDE.setTransmitter(transmitter);
		newDECIDE.setReceivers(peersList);
		
		//set the DECIDE server and peers
//		newDECIDE.setServer(server);
//		newDECIDE.setPeersList(peersList);
		
		Component component = new Component(componentID, newDECIDE);
		return component;
	}
	
	
	private static Component makeNewComponent(String id, String features, DECIDE decide){
		//get all component features
		String[] featuresList	= features.split(",");

		//get its ID
		String componentID		= featuresList[0];

		//find the port it is listening to
		int listeningPort 		= Integer.parseInt(featuresList[1].split(":")[1]);
		
		//create a new DECIDE server
		ServerDECIDE server = new ServerDECIDE(listeningPort);
		
//		List<String[]> peersListData = new ArrayList<String[]>();
		
		//create a list of peers
		List<ClientDECIDE> peersList = new ArrayList<ClientDECIDE>();
		
		//find the information of its peers
		for (String feature : featuresList){
			if (feature.contains("PEER")){
				String[] peerFeatures	= feature.split(":");
				String serverAddress 	= peerFeatures[1];
				int    port				= Integer.parseInt(peerFeatures[2]);
				
				//create a new CLientDECIDE
//				peersListData.add(new String[]{serverAddress, port});
				peersList.add(new ClientDECIDE(serverAddress, port));
			}				
		}
		
		//clone the DECIDE instance given by the user
		DECIDE newDECIDE = decide.deepClone();
		
		//set the DECIDE server and peers
		newDECIDE.setServer(server);
		newDECIDE.setPeersList(peersList);
		
		Component component = new Component(componentID, newDECIDE);
		return component;
	}
}
