package decide.component;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import auxiliary.Utility;
import decide.DECIDE;
import network.TransmitterDECIDE;
import network.ComponentTypeDECIDE;
import network.MulticastReceiver;
import network.MulticastTransmitter;
import network.ReceiverDECIDE;
import network.SocketReceiver;

public class ComponentFactory {

	
	
	/**
	 * Create a new component instance using multicast as the communication mechanism
	 * @param id
	 * @param features
	 * @param decide
	 * @return
	 */
	public static Component makeNewComponentMulticastNew(Class componentClass, String configurationFile, DECIDE decide){
		Utility.setup();

		try {
			
			//create a new component given its ID and transmitting + receiving features
			String[] componentDetails 	= ComponentFactory.getComponentDetails(configurationFile);
			String 	 componentID		= componentDetails[0].split("_")[1];
			String 	 componentFeatures	= componentDetails[1];

			//get all component features
			String[] featuresList	= componentFeatures.split(",");
			
			//transmitter to other DECIDE components
			TransmitterDECIDE transmitterDECIDE = null;
			
			//receiver from other DECIDE components
			List<ReceiverDECIDE> peersList = new ArrayList<ReceiverDECIDE>();
			
			for (String feature : featuresList){
				//get component's ID
//				if (feature.contains("ID")){
//					//get its ID
//					componentID = feature.split(":")[1];				
//				}
				
				//get component's transmitting address; mine
				if (feature.contains("TRANSMITTING")){
					String transmittingAddress	= feature.split(":")[1];
					int transmittingPort 		= Integer.parseInt(feature.split(":")[2]);
					//create a new DECIDE transmitter
					transmitterDECIDE 				= new MulticastTransmitter(transmittingAddress, transmittingPort, ComponentTypeDECIDE.PEER);
				}
				
				//find the information of its peers(peerPort%10 for id)
				if (feature.contains("RECEIVING")){
					String peerAddress	= feature.split(":")[1];
					int peerPort 		= Integer.parseInt(feature.split(":")[2]);
					peersList.add(new MulticastReceiver(peerAddress, peerPort, ComponentTypeDECIDE.PEER));
				}			
			}
						
			//set the DECIDE transmitter and receivers through which it communicates with other DECIDE components
			decide.setTransmitterToOtherDECIDE(transmitterDECIDE);
			decide.setReceiversFromOtherDECIDEs(peersList);
			

			
			//transmitter to robot
			TransmitterDECIDE robotTransmitter = null;
			
			//Receiver from robot
			ReceiverDECIDE robotReceiver = null;
			
			// Set Robot communication 
			String robotFeatures = Utility.getProperty(componentClass.getSimpleName().toUpperCase());
			int robotID = 0;
			//get all robot features
			String[] robotFeaturesList	= robotFeatures.split(",");
			
			for (String feature : robotFeaturesList){
				
				//get robot's ID
				if (feature.contains("ID")){
					//get its ID
					robotID = Integer.parseInt(feature.split(":")[1]);				
				}
				
				//get component's remote transmitting address
				if (feature.contains("TRANSMITTING")){
					String transmittingAddress	= feature.split(":")[1];
					int transmittingPort 		= Integer.parseInt(feature.split(":")[2]);
					//create a new DECIDE transmitter
					robotTransmitter 			= new MulticastTransmitter(transmittingAddress, transmittingPort, ComponentTypeDECIDE.ROBOT);
				}
				
				//find the information of remote peers
				if (feature.contains("RECEIVING")){
					String peerAddress	= feature.split(":")[1];
					int peerPort 		= Integer.parseInt(feature.split(":")[2]);
					robotReceiver		= new SocketReceiver(peerAddress, peerPort, ComponentTypeDECIDE.PEER.ROBOT);
				}			
			}
			
			//set the DECIDE remote transmitter and receiver through which DECIDE communicates with the robot/component
			decide.setTransmitterToRobot(robotTransmitter);
			decide.setReceiverFromRobot(robotReceiver);
			
			
			Component comp = (Component) componentClass.getDeclaredConstructor().newInstance();
			comp.setID(componentID);
			comp.setDECIDE(decide);
			return comp;
		} 
		catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
			System.exit(-1);
			return null;
		}
		
//		return new Component(componentID, newDECIDE);
	}
	
	

	/**
	 * Get details of this component, after initialising the utility class with the provided configuration file
	 * @param configurationFile that holds configuration information for this component
	 * @return
	 */
	public static String[] getComponentDetails(String configurationFile){
		Utility.setConfigurationFile(configurationFile);
		Utility.setup();
		
		//Get the properties set
		Set<Entry<Object,Object>> propertiesSet = Utility.getPropertiesEntrySet();
		
		//Get the iterator
		Iterator<Entry<Object,Object>> iterator = propertiesSet.iterator();
		
				
		while (iterator.hasNext()){
			Entry<Object, Object> entry = iterator.next();
			String key					= entry.getKey().toString();
			String value					= entry.getValue().toString().replaceAll("\\s+","");//remove whitespaces
			
			//create the components list
			if (key.contains("COMPONENT")){
				return new String[]{key, value};
			}//if			
		}//while
		
		return null;
	}

	
	public static String[] getComponentDetails() {
			String configurationFile = "resources" + File.separator + "uuv" +File.separator +"config.properties";
			Utility.setConfigurationFile(configurationFile);
			return getComponentDetails();
	}

}
