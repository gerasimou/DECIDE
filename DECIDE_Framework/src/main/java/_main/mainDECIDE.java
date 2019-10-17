package _main;

import java.io.File;

import org.apache.log4j.Logger;

import auxiliary.Utility;
import caseStudies.uuvNew.UUVCLAReceiptNew;
import caseStudies.uuvNew.UUVCapabilitySummaryCollectionNew;
import caseStudies.uuvNew.UUVConfigurationsCollectionNew;
import caseStudies.uuvNew.UUVEnvironmentNew;
import caseStudies.uuvNew.UUVLocalCapabilityAnalysisNew;
import caseStudies.uuvNew.UUVLocalControlNew;
import caseStudies.uuvNew.UUVNew;
import caseStudies.uuvNew.UUVSelectionExhaustiveNew;
import decide.DECIDENew;
import decide.KnowledgeNew;
import decide.capabilitySummary.CapabilitySummaryCollectionNew;
import decide.component.ComponentFactory;
import decide.component.ComponentNew;
import decide.configuration.ConfigurationsCollectionNew;
import decide.environment.EnvironmentNew;
import decide.evaluator.AttributeEvaluatorNew;
import decide.localAnalysis.LocalCapabilityAnalysisNew;
import decide.localControl.LocalControlNew;
import decide.qv.prism.PrismQVNew;
import decide.receipt.CLAReceiptNew;
import decide.selection.SelectionNew;



public class mainDECIDE {

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
				
		//setup configuration file, it can also be provided as an argument
		String configurationFile = "resources" + File.separator + "uuv" +File.separator +"config.properties";
		Utility.setConfigurationFile(configurationFile);
		
		//create a new robot configuration instance
		final int NUM_OF_SENSORS		= 3;
		final double SPEED_MAX 			= 5.0;
		final double STEP	  			= 0.2;
		ConfigurationsCollectionNew configurationCollections = new UUVConfigurationsCollectionNew(NUM_OF_SENSORS, SPEED_MAX, STEP);
		
		//create a new robot environment instance
		EnvironmentNew   environment	= new UUVEnvironmentNew();

		//create capability summary collection
		CapabilitySummaryCollectionNew capabilitySummaryCollection = new UUVCapabilitySummaryCollectionNew();
		
		//create new attribute evaluator
		AttributeEvaluatorNew attributeEvaluator = new PrismQVNew();

		//create local capability analysis
		LocalCapabilityAnalysisNew lca 	= new UUVLocalCapabilityAnalysisNew(attributeEvaluator);

		//create cla receipt
		CLAReceiptNew claReceipt		= new UUVCLAReceiptNew(capabilitySummaryCollection);

		//create selection part
		SelectionNew selection 			= new UUVSelectionExhaustiveNew();

		//crate local control
		LocalControlNew localControl 	= new UUVLocalControlNew(attributeEvaluator);
		
		//create a new DECIDE protocol instance
		DECIDENew decide  = new DECIDENew (lca, claReceipt, selection, localControl, configurationCollections, capabilitySummaryCollection, environment);
		
		//create a new component
		ComponentNew aComponent = ComponentFactory.makeNewComponentMulticastNew(UUVNew.class, configurationFile, decide);

		//init knowledge
		KnowledgeNew.initKnowledgeNew(aComponent);

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
