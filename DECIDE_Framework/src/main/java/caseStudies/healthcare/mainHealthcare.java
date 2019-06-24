package caseStudies.healthcare;

import org.apache.log4j.Logger;

import caseStudies.uuv.UUV;
import caseStudies.uuv.UUVConfigurationsCollection;
import caseStudies.uuv.UUVLocalCapabilityAnalysis;
import decide.DECIDE;
import decide.DECIDENew;
import decide.DecideException;
import decide.Knowledge;
import decide.capabilitySummary.CapabilitySummaryCollectionNew;
import decide.capabilitySummary.CapabilitySummaryNew;
import decide.component.Component;
import decide.component.ComponentFactory;
import decide.component.ComponentNew;
import decide.configuration.ConfigurationsCollection;
import decide.configuration.ConfigurationsCollectionNew;
import decide.environment.EnvironmentNew;
import decide.evaluator.AttributeEvaluator;
import decide.evaluator.AttributeEvaluatorNew;
import decide.localAnalysis.LocalCapabilityAnalysis;
import decide.localAnalysis.LocalCapabilityAnalysisNew;
import decide.localControl.LocalControlNew;
import decide.qv.prism.PrismQV;
import decide.qv.prism.PrismQVNew;
import decide.receipt.CLAReceipt;
import decide.receipt.CLAReceiptNew;
import decide.selection.SelectionNew;
import main.mainDECIDE;
import java.io.File;

public class mainHealthcare {

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
		
	
	
	public static void main(String[] args) throws DecideException {	
		logEvents("Starting DECIDE simulation");
			
		//setup configuration file, it can also be provided as an argument
		String configurationFile = "resources" + File.separator + "healthcare" +File.separator +"config.properties";
		
		//create a new component given its ID and transmitting + receiving features
		String[] componentDetails 	= ComponentFactory.getComponentDetails(configurationFile);
		String 	 componentID			= componentDetails[0].split("_")[1];
		String 	 componentFeatures	= componentDetails[1];

		//create a new robot configuration instance
		final double P3_FULL_MAX = 1.0;
		final double STEP		= 0.1;
		ConfigurationsCollectionNew configurationCollections = new RobotConfigurationCollection(P3_FULL_MAX, STEP);

		//create a new robot environment instance
		EnvironmentNew environment = new RobotEnvironment();
		
		//create capability summary collection
		CapabilitySummaryCollectionNew capabilitySummaryCollection = new RobotCapabilitySummaryCollection();
		
		//create new attribute evaluator
		AttributeEvaluatorNew attributeEvaluator = new PrismQVNew();
		
		//create local capability analysis
		LocalCapabilityAnalysisNew lca 	= new RobotLocalCapabilityAnalysis(attributeEvaluator);
		
		//create cla receipt
		CLAReceiptNew claReceipt			= new RobotCLAReceipt();
		claReceipt.setCapabilitySummaryCollection(capabilitySummaryCollection);

		//create selection part
		SelectionNew selection = new RobotSelection();
		
		//crate local control
		LocalControlNew localControl = new RobotLocalControl(attributeEvaluator);

		DECIDENew decide  = new DECIDENew (lca, claReceipt, selection, localControl, configurationCollections, capabilitySummaryCollection, environment);

		//create a new component
		ComponentNew aComponent = ComponentFactory.makeNewComponentMulticastNew(Robot.class, componentID, componentFeatures, decide);

		//init knowledge
		//TODO
		Knowledge.initKnowledgeNew(aComponent);

		//start executing		
		aComponent.run();		

		
	}

}
