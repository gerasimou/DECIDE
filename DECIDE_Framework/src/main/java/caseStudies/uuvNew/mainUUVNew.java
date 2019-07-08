package caseStudies.uuvNew;

import java.io.File;

import auxiliary.Utility;
import caseStudies.healthcare.RobotCLAReceipt;
import caseStudies.healthcare.RobotCapabilitySummaryCollection;
import caseStudies.healthcare.RobotLocalCapabilityAnalysis;
import caseStudies.healthcare.RobotLocalControl;
import caseStudies.healthcare.RobotSelectionExhaustive;
import caseStudies.uuv.UUVEnvironment;
import decide.capabilitySummary.CapabilitySummaryCollectionNew;
import decide.configuration.ConfigurationsCollectionNew;
import decide.environment.Environment;
import decide.environment.EnvironmentNew;
import decide.evaluator.AttributeEvaluatorNew;
import decide.localAnalysis.LocalCapabilityAnalysisNew;
import decide.localControl.LocalControlNew;
import decide.qv.prism.PrismQVNew;
import decide.receipt.CLAReceiptNew;
import decide.selection.SelectionNew;

public class mainUUVNew {

	public static void main(String[] args) {

		//setup configuration file, it can also be provided as an argument
		String configurationFile = "resources" + File.separator + "uuv" +File.separator +"config.properties";
		Utility.setConfigurationFile(configurationFile);
		
		//create a new robot configuration instance
		final int NUM_OF_SENSORS		= 3;
		final double SPEED_MAX 			= 1.0;
		final double STEP	  			= 0.1;
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
		SelectionNew selection 		= new UUVSelectionExhaustiveNew();

		//crate local control
		LocalControlNew localControl = new UUVLocalControlNew(attributeEvaluator);

	}

}
