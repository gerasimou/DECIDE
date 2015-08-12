package main;

import java.util.List;

import decide.DECIDE;
import decide.component.Component;
import decide.component.ComponentFactory;

public class mainDECIDE {

	
	public static void main(String[] args) {
		
		System.out.println("Starting DECIDE simulation");
				
		//init system components based on the features described in config.properties
		List<Component> componentsList = ComponentFactory.createComponents();

		DECIDE decide = new DECIDE();

		//init DECIDE for each component
		for (Component component : componentsList){
			component.configure(decide);
			System.out.println(component.getID() +"\t"+ component.getDECIDE());
		}

		
		for (Component component : componentsList){						
			String componentID	= component.getID();
//			new Thread(component, componentID).start();
		}

		
//		LocalCapabilityAnalysis lcaHandler 			= new LocalCapabilityAnalysisHandler();
//		CLAReceiptHandler       receiptHandler		= new CLAReceiptHandler();
//		SelectionHandler		selectionHandler	= new SelectionHandler();
//		LocalControlHandler		localControlHandler	= new LocalControlHandler();
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
