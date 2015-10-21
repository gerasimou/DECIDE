package main;

import decide.DECIDE;
import decide.component.Component;
import decide.component.ComponentFactory;
import decide.configuration.ConfigurationsCollection;
import decide.environment.Environment;
import robot.RobotConfigurationsCollection;
import robot.RobotEnvironment;
import robot.RobotSimulator;

public class mainDECIDE {

	
	public static void main(String[] args) {
		
		System.err.println("Starting DECIDE simulation\n========================\n\n");
				
		//create a new component given its ID and transmitting + receiving features
		String[] componentDetails 	= ComponentFactory.getComponentDetails();
		String 	 componentID		= componentDetails[0];
		String 	 componentFeatures	= componentDetails[1];
		
		//create a new robot configuration instance
		ConfigurationsCollection configuration = new RobotConfigurationsCollection();
		
		//create a new robot environment instance
		Environment   environment	= new RobotEnvironment();

		//create a new DECIDE protocol instance
		DECIDE decide = new DECIDE(componentID, configuration, environment);
		
		//create a new component
		Component aComponent = ComponentFactory.makeNewComponentMulticast(RobotSimulator.class, componentID, componentFeatures, decide);

		//start executing		
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
}
