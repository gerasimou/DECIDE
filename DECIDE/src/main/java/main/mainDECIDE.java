package main;

import decide.DECIDE;
import decide.component.Component;
import decide.component.ComponentFactory;
import decide.configuration.Configuration;
import decide.configuration.ResultDECIDE;
import robot.RobotOld;
import robot.RobotConfiguration;

public class mainDECIDE {

	
	public static void main(String[] args) {
		
		System.err.println("Starting DECIDE simulation");
				
		//create a new component given its ID and transmitting + receiving features
		String[] componentDetails 	= ComponentFactory.getComponentDetails();
		String 	 componentID		= componentDetails[0];
		String 	 componentFeatures	= componentDetails[1];
		
		Configuration config = new RobotConfiguration();

		DECIDE decide = new DECIDE(componentID, config);
		
		Component aComponent = ComponentFactory.makeNewComponentMulticast(RobotOld.class, componentID, componentFeatures, decide);
//		
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
