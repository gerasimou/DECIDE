package decide.component;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import auxiliary.Utility;
import decide.DECIDE;
import decide.DECIDENew;
import network.TransmitterDECIDE;
import network.ClientSocketDECIDE;
import network.MulticastReceiver;
import network.MulticastReceiverNew;
import network.MulticastTransmitter;
import network.ReceiverDECIDE;
import network.ReceiverDECIDENew;
import network.DatagramSocketReceiver;
import network.SocketReceiver;
import network.SocketReceiverNew;

public class ComponentFactory {

	/**
	 * Create a new component instance using multicast as the communication mechanism
	 * @param id
	 * @param features
	 * @param decide
	 * @return
	 */
	public static Component makeNewComponentMulticast(Class componentClass, String id, String features, DECIDE decide){
		Utility.setup();

		try {

			//get all component features
			String[] featuresList	= features.split(",");
			
			//transmitter to other DECIDE components
			TransmitterDECIDE transmitter = null;
			
			//receiver from other DECIDE components
			List<ReceiverDECIDENew> peersList = new ArrayList<ReceiverDECIDENew>();
			
			//transmitter to robot
			TransmitterDECIDE uuvTransmitter = null;
			
			//Receiver from robot
			ReceiverDECIDE uuvReceiver = null;
	
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
					transmitter 				= new MulticastTransmitter(transmittingAddress, transmittingPort);
				}
				
				//find the information of its peers(peerPort%10 for id)
				if (feature.contains("RECEIVING")){
					String peerAddress	= feature.split(":")[1];
					int peerPort 		= Integer.parseInt(feature.split(":")[2]);
					peersList.add(new MulticastReceiverNew(peerAddress, peerPort));
				}			
			}
			
			//clone the DECIDE instance given by the user
//			DECIDE newDECIDE = decide.deepClone();
			
			//set the DECIDE transmitter and receivers through which it communicates with other DECIDE components
			decide.setTransmitterToOtherDECIDE(transmitter);
			decide.setReceiverFromOtherDECIDE(peersList);
			
			
			// Set MOOS UUV 
			String uuvFeatures = Utility.getProperty("UUV");
			int uuvID = 0;
			//get all robot features
			String[] uuvFeaturesList	= uuvFeatures.split(",");
			
			for (String feature : uuvFeaturesList){
				
				//get robot's ID
				if (feature.contains("ID")){
					//get its ID
					uuvID = Integer.parseInt(feature.split(":")[1]);				
				}
				
				
				//get component's remote transmitting address
				if (feature.contains("TRANSMITTING")){
					String transmittingAddress	= feature.split(":")[1];
					int transmittingPort 		= Integer.parseInt(feature.split(":")[2]);
					//create a new DECIDE transmitter
					uuvTransmitter 				= new MulticastTransmitter(transmittingAddress, transmittingPort);
				}
				
				//find the information of remote peers
				if (feature.contains("RECEIVING")){
					String peerAddress	= feature.split(":")[1];
					int peerPort 		= Integer.parseInt(feature.split(":")[2]);
					//uuvReceiver 		= new ServerDatagramSocket(peerAddress, peerPort);
					uuvReceiver			= new SocketReceiver(peerPort);
				}			
			}
			
			//set the DECIDE remote transmitter and receiver through which DECIDE communicates with the robot/component
			decide.setTransmitterToComponent(uuvTransmitter);
			decide.setReceiverFromComponent(uuvReceiver);
			
			
			Component comp = (Component) componentClass.newInstance();
			comp.setID(id);
			comp.setDECIDE(decide);
			return comp;
		} 
		catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
			System.exit(-1);
			return null;
		}
		
//		return new Component(componentID, newDECIDE);
	}
	
	
	/**
	 * Create a new component instance using multicast as the communication mechanism
	 * @param id
	 * @param features
	 * @param decide
	 * @return
	 */
	public static ComponentNew makeNewComponentMulticastNew(Class componentClass, String configurationFile, DECIDENew decide){
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
			List<ReceiverDECIDENew> peersList = new ArrayList<ReceiverDECIDENew>();
			
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
					transmitterDECIDE 				= new MulticastTransmitter(transmittingAddress, transmittingPort);
				}
				
				//find the information of its peers(peerPort%10 for id)
				if (feature.contains("RECEIVING")){
					String peerAddress	= feature.split(":")[1];
					int peerPort 		= Integer.parseInt(feature.split(":")[2]);
					peersList.add(new MulticastReceiverNew(peerAddress, peerPort));
				}			
			}
						
			//set the DECIDE transmitter and receivers through which it communicates with other DECIDE components
			decide.setTransmitterToOtherDECIDE(transmitterDECIDE);
			decide.setReceiverFromOtherDECIDE(peersList);
			

			
			//transmitter to robot
			TransmitterDECIDE robotTransmitter = null;
			
			//Receiver from robot
			ReceiverDECIDENew robotReceiver = null;
			
			// Set Robot communication 
			String robotFeatures = Utility.getProperty("ROBOT");
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
					robotTransmitter 			= new MulticastTransmitter(transmittingAddress, transmittingPort);
				}
				
				//find the information of remote peers
				if (feature.contains("RECEIVING")){
					String peerAddress	= feature.split(":")[1];
					int peerPort 		= Integer.parseInt(feature.split(":")[2]);
					robotReceiver		= new SocketReceiverNew(peerAddress, peerPort);
				}			
			}
			
			//set the DECIDE remote transmitter and receiver through which DECIDE communicates with the robot/component
			decide.setTransmitterToComponent(robotTransmitter);
			decide.setReceiverFromComponent(robotReceiver);
			
			
			ComponentNew comp = (ComponentNew) componentClass.newInstance();
			comp.setID(componentID);
			comp.setDECIDE(decide);
			return comp;
		} 
		catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
			System.exit(-1);
			return null;
		}
		
//		return new Component(componentID, newDECIDE);
	}
	
	
	/**
	 * Create a list of components based on the information set in the configuration file
	 * @param decide
	 * @return
	 */
	@Deprecated
	public static List<Component> createComponents(DECIDE decide, Class<?> clazz){
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
				componentsList.add(makeNewComponentMulticast(clazz, key.split("_")[1], value, decide));
			}//if			
		}//while
		
		
		return componentsList;
	}
		
	
	/**
	 * Create a new component instance using multicast as the communication mechanism
	 * @param id
	 * @param features
	 * @param decide
	 * @return
	 */
	@Deprecated
	public static Component makeNewRobotComponentMulticast(Class componentClass, String id, String features, DECIDE decide){
		Utility.setup();

		try {

			//get all component features
			String[] featuresList	= features.split(",");
			
			String componentID = null;
			
			//new DECIDE transmitter
			TransmitterDECIDE transmitter = null;
			
			//new DECIDE Robottransmitter
			TransmitterDECIDE robotTransmitter = null;
			
			//new DECIDE Robottransmitter
			DatagramSocketReceiver robotReceiver = null;
			
			//create a list of peers
			List<ReceiverDECIDENew> peersList = new ArrayList<ReceiverDECIDENew>();
	
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
					peersList.add(new MulticastReceiverNew(peerAddress, peerPort));
				}			
			}
			
			//clone the DECIDE instance given by the user
//			DECIDE newDECIDE = decide.deepClone();
			
			//set the DECIDE transmitter and receivers
			decide.setTransmitterToOtherDECIDE(transmitter);
			decide.setReceiverFromOtherDECIDE(peersList);
			
			String robotFeatures = Utility.getProperty("ROBOT");
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
					robotTransmitter 				= new MulticastTransmitter(transmittingAddress, transmittingPort);
				}
				
				//find the information of remote peers
				if (feature.contains("RECEIVING")){
					String peerAddress	= feature.split(":")[1];
					int peerPort 		= Integer.parseInt(feature.split(":")[2]);
					robotReceiver 		= new DatagramSocketReceiver(peerAddress, peerPort);
				}			
			}
			
			//set the DECIDE remote transmitter and receivers
			decide.setTransmitterToComponent(robotTransmitter);
			decide.setReceiverFromComponent(robotReceiver);
					
			Component comp = (Component) componentClass.newInstance();
			comp.setID(componentID);
			comp.setDECIDE(decide);
			return comp;
		} 
		catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
			System.exit(-1);
			return null;
		}
		
//		return new Component(componentID, newDECIDE);
	}
	
	
	
	/**
	 * Create a new component that uses sockets for its communication mechanism 
	 * <b>(not fully supported yet)</b>
	 * @param id
	 * @param features
	 * @param decide
	 * @return
	 */
	@Deprecated
	private static Component makeNewComponent(String id, String features, DECIDE decide){
		//get all component features
		String[] featuresList	= features.split(",");

		//get its ID
		String componentID		= featuresList[0];

		//find the port it is listening to
		int listeningPort 		= Integer.parseInt(featuresList[1].split(":")[1]);
		
		//create a new DECIDE server
		SocketReceiver server = new SocketReceiver(listeningPort);
		
//		List<String[]> peersListData = new ArrayList<String[]>();
		
		//create a list of peers
		List<ClientSocketDECIDE> peersList = new ArrayList<ClientSocketDECIDE>();
		
		//find the information of its peers
		for (String feature : featuresList){
			if (feature.contains("PEER")){
				String[] peerFeatures	= feature.split(":");
				String serverAddress 	= peerFeatures[1];
				int    port				= Integer.parseInt(peerFeatures[2]);
				
				//create a new CLientDECIDE
//				peersListData.add(new String[]{serverAddress, port});
				peersList.add(new ClientSocketDECIDE(serverAddress, port));
			}				
		}
		
		//clone the DECIDE instance given by the user
		DECIDE newDECIDE = decide.deepClone();
		
		//set the DECIDE server and peers
//		newDECIDE.setTransmitter(transmitter);
//		newDECIDE.setReceivers(peersList);
		
//		Component component = new Component(componentID, newDECIDE);
//		return component;
		return null;
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
