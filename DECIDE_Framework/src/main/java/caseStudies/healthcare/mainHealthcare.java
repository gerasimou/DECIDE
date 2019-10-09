package caseStudies.healthcare;

import org.apache.log4j.Logger;

import _main.mainDECIDE;
import auxiliary.Utility;
import decide.DECIDENew;
import decide.DecideException;
import decide.Knowledge;
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
		Utility.setConfigurationFile(configurationFile);

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
		CLAReceiptNew claReceipt	= new RobotCLAReceipt(capabilitySummaryCollection);

		//create selection part
//		SelectionNew selection 		= new RobotSelectionMDP();
		SelectionNew selection 		= new RobotSelectionExhaustive();
		
		//crate local control
		LocalControlNew localControl = new RobotLocalControl(attributeEvaluator);

		DECIDENew decide  = new DECIDENew (lca, claReceipt, selection, localControl, configurationCollections, capabilitySummaryCollection, environment);

		//create a new component
		ComponentNew aComponent = ComponentFactory.makeNewComponentMulticastNew(Robot.class, configurationFile, decide);

		//init knowledge
		//TODO
		KnowledgeNew.initKnowledgeNew(aComponent);

		//start executing		
		aComponent.run();		

		
	}

}
