package example.activityBot;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

//import activityBot.ActivityBotConfigurationsCollection;
//import activityBot.ActivityBotEnvironment;
//import activityBot.ActivityBotSimulator;
import decide.DECIDE;
import decide.Knowledge;
import decide.component.Component;
import decide.component.ComponentFactory;
import decide.configuration.Configuration;
import decide.configuration.ConfigurationsCollection;
import decide.configuration.Mode;
import decide.environment.Environment;

public class mainActivityBot {
//	/*
//	// testing 
//	static Logger logger = Logger.getLogger(mainDECIDEtest.class);
//	
//	Component component;
//	public mainDECIDEtest(Component com)
//	{
//		this.component = com ;
//	}
//	
//	/**
//	 * Log system events to console or file
//	 * @param String log
//	 */
//	private static void logEvents(String parameter){
//
//		if(logger.isDebugEnabled())
//			logger.debug("[debug] : " + parameter);
//		else
//			logger.info("[info] : " + parameter);
//
//		//logger.error("This is error : " + parameter);
//	}
//	@SuppressWarnings("unchecked")
//	private boolean execute() {
//		//For all configurations run QV
//				Configuration 	config 			= null;
//				boolean 		result = false;
//
//				
//				while ((config = this.component.getDECIDE().getConfigurationsCollection().getNextConfiguration()) != null){
//					List<Double> resultsList = new ArrayList<Double> ();
//
//					    //for all system properties
//						//1) Instantiate parametric stochastic model								
//						//String model = config.getModel() + environment.getModel(adjustEnvironment, config, propertyNum);
//				
//						//2) load PRISM model
//						//prism.loadModel(model);
//						
//						//3) run PRISM
//						//Double res = prism.run(propertyNum);
//					List<Object> configurationElements = (List<Object>) config.getConfigurationElements();
//					
//						resultsList.add((double)((Integer)configurationElements.get(1)));
//						resultsList.add((double)((Integer)configurationElements.get(2)));
//						//resultsList.add((double)(config.getConfigurationElements().indexOf(2)));
//					
//					config.setResults(resultsList);
//					result =true;
//				}
//		return result;
//	}
//
//	
//	public static void main(String[] args) {
//	
//		
//		logEvents("Framework Start");
//		
//		
//		
//				
//		//create a new component given its ID and transmitting + receiving features
//		String[] componentDetails 	= ComponentFactory.getComponentDetails();
//		String 	 componentID		= componentDetails[0];
//		String 	 componentFeatures	= componentDetails[1];
//		
//		//create a new robot configuration instance
//		ConfigurationsCollection configurations = new ActivityBotConfigurationsCollection();
//		
//		//create a new robot environment instance
//		Environment   environment	= new ActivityBotEnvironment();
//		
//
//		//create a new DECIDE protocol instance
//		DECIDE decide = new DECIDE(componentFeatures.split(",")[0], configurations, environment);
//		
//
//		//create a new Robot component
//		Component aComponent = ComponentFactory.makeNewRobotComponentMulticast(ActivityBotSimulator.class, componentID, componentFeatures, decide);
//	
//		// set id in ClaReceipt object
//		decide.setID(Integer.parseInt(aComponent.getID()));
//		
//		//init knowledge
//		Knowledge.initKnowledge(aComponent);
//
//		//start executing		
//		//aComponent.run();
//		
//		
//		mainDECIDEtest mDt = new mainDECIDEtest(aComponent);
//		boolean result = mDt.execute();
//		// if there is a result set then proceed.
//		if(result)
//		{
//			//Step 2) Find the best result per mode (configuration subset)
//			aComponent.getDECIDE().getConfigurationsCollection().findBestPerMode(environment);
//			
//			
//			Mode mode = null;
//			while ( (mode=aComponent.getDECIDE().getConfigurationsCollection().getNextMode()) != null){
//				Configuration bestConfig 		= mode.getBestConfiguration();
//				//Map<String,Object> grResults   	= bestConfig.getGlobalRequirementsResults();
//				//Object[] results				= grResults.values().toArray();
////				String resultsStr				= Arrays.toString(results);
////				capabilitySummary.append(resultsStr);
//				aComponent.getDECIDE().getCapabilitySummary().concurrentConfigurationsMap.put(mode.hashCode()+"", bestConfig);
//			}
//			
//			
//			aComponent.run();
//			
//		}
//		
//				
//		
//		
//		
//		
//		//init system components based on the features described in config.properties
////		List<Component> componentsList = ComponentFactory.createComponents(decide);
////
////		//init DECIDE for each component
////		List<Thread> threadList = new ArrayList<Thread>();
////		for (Component component : componentsList){
////			Thread t = new Thread(component, component.getID());
////			t.start();
////			threadList.add(t);
//////			component.run();
////			System.out.println(component.getID() +"\t"+ component.getDECIDE());
////		}
////		
////		for (Thread thread : threadList){
////			try {
////				thread.join();
////			} catch (InterruptedException e) {
////				// TODO Auto-generated catch block
////				e.printStackTrace();
////			}
////		}
//
//		System.out.println("======================");
//		System.out.println("END");
//	}
//	
}
