package main;

import java.util.ArrayList;
import java.util.List;

import decide.DECIDE;
import decide.component.Component;
import decide.component.ComponentFactory;

public class mainDECIDE {

	
	public static void main(String[] args) {
		
		System.err.println("Starting DECIDE simulation");
				
		//create a new component given its ID and transmitting + receiving features
		String componentID 			= "C1";
		String componentFeatures 	= "ID:C1,TRANSMITTING:224.224.224.221:8881,RECEIVING:224.224.224.222:8882,RECEIVING:224.224.224.223:8883"; 
		
		componentID 			= "C2";
		componentFeatures		= "ID:C2, TRANSMITTING:224.224.224.222:8882, RECEIVING:224.224.224.221:8881, RECEIVING:224.224.224.223:8883";

		//TODO how the extended DECIDE stages can be realised with minimum effort by the DECIDE framework?
		DECIDE decide = new DECIDE(componentID);
		
		Component aComponent = ComponentFactory.makeNewComponentMulticast(componentID, componentFeatures, decide);
		
		aComponent.run();
		
		
		//init system components based on the features described in config.properties
//		List<Component> componentsList = ComponentFactory.createComponents(decide);
//
//		//init DECIDE for each component
//		List<Thread> threadList = new ArrayList<Thread>();
//		for (Component component : componentsList){
//			Thread t = new Thread(component, component.getID());
//			t.start();
//			threadList.add(t);
////			component.run();
//			System.out.println(component.getID() +"\t"+ component.getDECIDE());
//		}
//		
//		for (Thread thread : threadList){
//			try {
//				thread.join();
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}

		System.err.println("END");
	}
	

	
	
	
	
	
	private static void runMe(String parameter){
//		if(logger.isDebugEnabled()){
//			logger.debug("This is debug : " + parameter);
//		}
//		
//		if(logger.isInfoEnabled()){
//			logger.info("This is info : " + parameter);
//		}
//		
//		logger.warn("This is warn : " + parameter);
//		logger.error("This is error : " + parameter);
//		logger.fatal("This is fatal : " + parameter);
//		
	}
}
