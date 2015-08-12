package decide.component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import auxiliary.Utility;
import network.ClientDECIDE;
import network.ServerDECIDE;

import java.util.Map.Entry;

public class ComponentFactory {

	public static List<Component> createComponents(){
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
			
//			COMPONENT = 1, SERVER:9991, CLIENT:127.0.0.1:9992, CLIENT:127.0.0.1:9993
//			System.out.println(key +"\t"+ value);
			
			if (key.contains("COMPONENT")){
				componentsList.add(newComponent(key, value));
			}			
		}
		return componentsList;
	}
	
	
	
	private static Component newComponent(String id, String features){
		//get all component features
		String[] featuresList	= features.split(",");

		//get its ID
		String componentID		= featuresList[0];

		//find the port it is listening to
		String listeningPort 		= featuresList[1].split(":")[1];
		
		List<String[]> peersList= new ArrayList<String[]>();
		
		//find the information of its peers
		for (String feature : featuresList){
			if (feature.contains("PEER")){
				String[] peerFeatures	= feature.split(":");
				String serverAddress 	= peerFeatures[1];
				String    port			= peerFeatures[2];
				
				//create a new CLientDECIDE
				peersList.add(new String[]{serverAddress, port});
			}				
		}
		
//		System.out.println(Arrays.toString(featuresList));
		
		Component component = new Component(componentID, listeningPort, peersList);
		return component;
	}
}
