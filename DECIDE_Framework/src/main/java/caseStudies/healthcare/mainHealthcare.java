package caseStudies.healthcare;

import org.apache.log4j.Logger;

import caseStudies.uuv.UUVConfigurationsCollection;
import decide.component.ComponentFactory;
import decide.configuration.ConfigurationsCollection;
import decide.configuration.ConfigurationsCollectionNew;
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
		
	
	
	public static void main(String[] args) {	
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

		
	}

}
