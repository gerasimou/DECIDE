package decide.component;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import auxiliary.Utility;
import decide.DECIDE;
import network.ClientDECIDE;
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
				componentsList.add(makeNewComponent(key, value, decide));
			}//if			
		}//while
		
		
		return componentsList;
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
