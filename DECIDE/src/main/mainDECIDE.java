package main;

import java.util.List;

import decide.DECIDE;
import decide.component.Component;
import decide.component.ComponentFactory;

public class mainDECIDE {

	
	public static void main(String[] args) {
		
		System.err.println("Starting DECIDE simulation");
				
		//TODO how the extended DECIDE stages can be realised with minimum effort by the DECIDE framework?
		DECIDE decide = new DECIDE();

		//init system components based on the features described in config.properties
		List<Component> componentsList = ComponentFactory.createComponents(decide);

		//init DECIDE for each component
		for (Component component : componentsList){
			component.run();
			System.out.println(component.getID() +"\t"+ component.getDECIDE());
		}

		
//		for (Component component : componentsList){						
//			String componentID	= component.getID();
//			new Thread(component, componentID).start();
//		}		
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
