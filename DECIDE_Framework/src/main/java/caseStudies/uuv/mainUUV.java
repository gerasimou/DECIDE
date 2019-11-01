package caseStudies.uuv;

import java.io.File;

import auxiliary.Utility;
import decide.DECIDE;
import decide.KnowledgeNew;
import decide.capabilitySummary.CapabilitySummaryCollection;
import decide.component.Component;
import decide.component.ComponentFactory;
import decide.component.requirements.DECIDEAttributeCollection;
import decide.configuration.ConfigurationsCollection;
import decide.environment.Environment;
import decide.localAnalysis.LocalCapabilityAnalysis;
import decide.localControl.LocalControl;
import decide.receipt.CLAReceipt;
import decide.selection.SelectionNew;

public class mainUUV {

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
		ConfigurationsCollection configurationCollections = new UUVConfigurationsCollection(uuvAttributes, NUM_OF_SENSORS, SPEED_MAX, STEP);
		
		//create a new robot environment instance
		Environment   environment	= new UUVEnvironment();

		//create capability summary collection
		CapabilitySummaryCollection capabilitySummaryCollection = new UUVCapabilitySummaryCollection();
		
//		//create new attribute evaluator
//		AttributeEvaluatorNew attributeEvaluator = new PrismQVNew();

		//create local capability analysis
		LocalCapabilityAnalysis lca 	= new UUVLocalCapabilityAnalysis();

		//create cla receipt
		CLAReceipt claReceipt		= new UUVCLAReceipt(capabilitySummaryCollection);

		//create selection part
		SelectionNew selection 			= new UUVSelectionExhaustiveNew();

		//crate local control
		LocalControl localControl 	= new UUVLocalControl();

	
		//create new DECIDE
		DECIDE decide  = new DECIDE (lca, claReceipt, selection, localControl, configurationCollections, capabilitySummaryCollection, environment);

		
		//create a new component
		Component aComponent = ComponentFactory.makeNewComponentMulticastNew(UUV.class, configurationFile, decide);

		//init knowledge
		KnowledgeNew.initKnowledgeNew(aComponent);
		
		((UUVSelectionExhaustiveNew)selection).setMyAddress(lca.getTransmitterToOtherDECIDE().getServerAddress());
		
		//start executing		
		aComponent.run();		

	}

}
