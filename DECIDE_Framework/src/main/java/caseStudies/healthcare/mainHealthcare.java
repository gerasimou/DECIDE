package caseStudies.healthcare;

import java.io.File;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import auxiliary.Utility;
import decide.DECIDE;
import decide.DecideException;
import decide.Knowledge;
import decide.capabilitySummary.CapabilitySummaryCollection;
import decide.component.Component;
import decide.component.ComponentFactory;
import decide.component.requirements.DECIDEAttributeCollection;
import decide.configuration.ConfigurationsCollection;
import decide.environment.Environment;
import decide.localAnalysis.LocalCapabilityAnalysis;
import decide.localControl.LocalControl;
import decide.receipt.CLAReceipt;
import decide.selection.Selection;


public class mainHealthcare {

	static Logger logger = LogManager.getLogger(mainHealthcare.class);
	
	
	public static void main(String[] args) throws DecideException {	
			
		//setup configuration file, it can also be provided as an argument
		String configurationFile = "resources" + File.separator + "healthcare" +File.separator +"config.properties";
		Utility.setConfigurationFile(configurationFile);
		
		//create the set of robot attributes
		DECIDEAttributeCollection robotAttributes = new RobotAttributeCollection();


		//create a new robot configuration instance
		final double P3_FULL_MAX	= 1.0;
		final double STEP			= 0.5;
		ConfigurationsCollection configurationCollections = new RobotConfigurationCollection(robotAttributes, P3_FULL_MAX, STEP);

		
		//create a new robot environment instance
		Environment environment = new RobotEnvironment();

		
		//create capability summary collection
		CapabilitySummaryCollection capabilitySummaryCollection = new RobotCapabilitySummaryCollection();
		
		
		//create local capability analysis
		LocalCapabilityAnalysis lca 	= new RobotLocalCapabilityAnalysis();
		
		
		//create cla receipt
		CLAReceipt claReceipt		= new RobotCLAReceipt(capabilitySummaryCollection);

		
		//create selection part
		String modelExportFile 	= "models/healthcare/global/allocmodel.prism";
		String propertiesFile 	= "models/healthcare/global/gallocsp.props";
		String advExportFile 	= "models/healthcare/global/adv.tra";
		Selection selection 		= new RobotSelectionMDP(modelExportFile, propertiesFile, advExportFile);
//		SelectionNew selection 		= new RobotSelectionExhaustive();
		
		//crate local control
		LocalControl localControl = new RobotLocalControl();

		DECIDE decide  = new DECIDE (lca, claReceipt, selection, localControl, configurationCollections, capabilitySummaryCollection, environment);

		//create a new component
		Component aComponent = ComponentFactory.makeNewComponentMulticastNew(Robot.class, configurationFile, decide);

		//init knowledge
		//TODO
		Knowledge.initKnowledgeNew(aComponent);
		RobotKnowledge.initRobotKnowledge();

		//start executing		
		aComponent.run();			
	}

}
