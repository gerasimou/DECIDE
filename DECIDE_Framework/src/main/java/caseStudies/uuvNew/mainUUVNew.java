package caseStudies.uuvNew;

import java.io.File;

import auxiliary.Utility;
import decide.DECIDENew;
import decide.KnowledgeNew;
import decide.capabilitySummary.CapabilitySummaryCollectionNew;
import decide.component.ComponentFactory;
import decide.component.ComponentNew;
import decide.component.requirements.DECIDEAttributeCollection;
import decide.configuration.ConfigurationsCollectionNew;
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
		
		//create the set of UUV attributes
		DECIDEAttributeCollection uuvAttributes = new UUVAttributesCollection();
		
		//create a new robot configuration instance
		final int NUM_OF_SENSORS		= 3;
		final double SPEED_MAX 			= 5.0;
		final double STEP	  			= 0.2;
		ConfigurationsCollectionNew configurationCollections = new UUVConfigurationsCollectionNew(uuvAttributes, NUM_OF_SENSORS, SPEED_MAX, STEP);
		
		//create a new robot environment instance
		EnvironmentNew   environment	= new UUVEnvironmentNew();

		//create capability summary collection
		CapabilitySummaryCollectionNew capabilitySummaryCollection = new UUVCapabilitySummaryCollectionNew();
		
//		//create new attribute evaluator
//		AttributeEvaluatorNew attributeEvaluator = new PrismQVNew();

		//create local capability analysis
		LocalCapabilityAnalysisNew lca 	= new UUVLocalCapabilityAnalysisNew();

		//create cla receipt
		CLAReceiptNew claReceipt		= new UUVCLAReceiptNew(capabilitySummaryCollection);

		//create selection part
		SelectionNew selection 			= new UUVSelectionExhaustiveNew();

		//crate local control
		LocalControlNew localControl 	= new UUVLocalControlNew();

	
		//create new DECIDE
		DECIDENew decide  = new DECIDENew (lca, claReceipt, selection, localControl, configurationCollections, capabilitySummaryCollection, environment);

		
		//create a new component
		ComponentNew aComponent = ComponentFactory.makeNewComponentMulticastNew(UUV.class, configurationFile, decide);

		//init knowledge
		KnowledgeNew.initKnowledgeNew(aComponent);
		
		//start executing		
		aComponent.run();		

	}

}
