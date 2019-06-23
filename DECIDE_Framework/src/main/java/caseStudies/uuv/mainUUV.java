package caseStudies.uuv;

import org.apache.log4j.Logger;

import decide.DECIDE;
import decide.Knowledge;
import decide.capabilitySummary.CapabilitySummaryCollection;
import decide.component.Component;
import decide.component.ComponentFactory;
import decide.configuration.ConfigurationsCollection;
import decide.environment.Environment;
import decide.evaluator.AttributeEvaluator;
import decide.localAnalysis.LocalCapabilityAnalysis;
import decide.localControl.LocalControl;
import decide.qv.prism.PrismQV;
import decide.receipt.CLAReceipt;
import decide.selection.Selection;
import main.mainDECIDE;

public class mainUUV {

	static Logger logger = Logger.getLogger(mainDECIDE.class);
	/**
	 * Log system events to console or file
	 * @param String log
	 */
	private static void logEvents(String parameter){

		if(logger.isDebugEnabled())
			logger.debug("[debug] : " + parameter);
		else
			logger.info("[info] : " + parameter);

		//logger.error("This is error : " + parameter);
	}
	public static void main(String[] args) {
		
		logEvents("Starting DECIDE simulation");
				
		//create a new component given its ID and transmitting + receiving features
		String[] componentDetails 	= ComponentFactory.getComponentDetails();
		String 	 componentID			= componentDetails[0].split("_")[1];
		String 	 componentFeatures	= componentDetails[1];
		
		//create a new robot configuration instance
		ConfigurationsCollection configurationCollections = new UUVConfigurationsCollection();
		
		//create a new robot environment instance
		Environment   environment	= new UUVEnvironment();

		
		CapabilitySummaryCollection capabilitySummaries = new UUVCapabilitySummaryCollection();
		
		
		//create a new DECIDE protocol instance
//		DECIDE decide = new DECIDE(configurationCollections, capabilitySumamries, environment, 
//				UUVLocalCapabilityAnalysis.class, UUVCLAReceipt.class, UUVSelection.class,UUVLocalControl.class);
		
		AttributeEvaluator propertyEvaluator = new PrismQV();
		LocalCapabilityAnalysis lca 	= new UUVLocalCapabilityAnalysis(propertyEvaluator);
		CLAReceipt claReceipt		= new UUVCLAReceipt();
		claReceipt.setCapabilitySummaryCollection(capabilitySummaries);
		Selection selection			= new UUVSelection();
		LocalControl localControl	= new UUVLocalControl(propertyEvaluator);
		
		DECIDE decide  = new DECIDE(lca, claReceipt, selection, localControl, configurationCollections, capabilitySummaries, environment);
		
		//create a new component
		Component aComponent = ComponentFactory.makeNewComponentMulticast(UUV.class, componentID, componentFeatures, decide);
		
		//init knowledge
		Knowledge.initKnowledge(aComponent);

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
