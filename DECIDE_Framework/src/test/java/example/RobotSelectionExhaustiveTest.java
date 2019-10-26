package example;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import auxiliary.Utility;
import caseStudies.healthcare.RobotAttributeCollection;
import caseStudies.healthcare.RobotCapabilitySummary;
import caseStudies.healthcare.RobotCapabilitySummaryCollection;
import caseStudies.healthcare.RobotConfigurationCollection;
import caseStudies.healthcare.RobotSelectionExhaustive;
import decide.capabilitySummary.CapabilitySummaryCollectionNew;
import decide.capabilitySummary.CapabilitySummaryNew;
import decide.component.requirements.DECIDEAttributeCollection;
import decide.configuration.ConfigurationsCollectionNew;
import decide.environment.EnvironmentNew;


public class RobotSelectionExhaustiveTest {

	static ConfigurationsCollectionNew configurationsCollection;	
	static EnvironmentNew environment;
	static CapabilitySummaryCollectionNew capabilitySummaryCollection;
	static RobotSelectionExhaustive selectionExhaustive;
	
	
	@BeforeClass
	public static void init() {
		
		//setup configuration file, it can also be provided as an argument
		String configurationFile = "resources" + File.separator + "healthcare" +File.separator +"config.properties";
		Utility.setConfigurationFile(configurationFile);

		//create the set of robot attributes
		DECIDEAttributeCollection robotAttributes = new RobotAttributeCollection();

		//create a new robot configuration instance
		final double P3_FULL_MAX = 1.0;
		final double STEP		= 0.1;
		configurationsCollection = new RobotConfigurationCollection(robotAttributes, P3_FULL_MAX, STEP);
		
		//create capability summary of me
		configurationsCollection.insertCapabilitySummary("M0", new RobotCapabilitySummary(0, 0, 0, 0, 0));//, 0));
		configurationsCollection.insertCapabilitySummary("M1", new RobotCapabilitySummary(1.1, 1.2, 1.3, 1.4, 1.5));//, 4));
		configurationsCollection.insertCapabilitySummary("M2", new RobotCapabilitySummary(2.1, 2.2, 2.3, 2.4, 2.5));//, 5));
	
		//create capability summary collection
		capabilitySummaryCollection = new RobotCapabilitySummaryCollection();
			
		//capability summary of peer 2
		CapabilitySummaryNew[] cs2 = new RobotCapabilitySummary[] {new RobotCapabilitySummary(0, 0, 0, 0, 0), new RobotCapabilitySummary(3.1, 3.2, 3.3, 3.4, 3.5), new RobotCapabilitySummary(4.1, 4.2, 4.3, 4.4, 4.5)};
		capabilitySummaryCollection.addCapabilitySummary("2", cs2);

		//capability summary of peer 3
		CapabilitySummaryNew[] cs3 = new RobotCapabilitySummary[] {new RobotCapabilitySummary(0, 0, 0, 0, 0), new RobotCapabilitySummary(5.1, 5.2, 5.3, 5.4, 5.5), new RobotCapabilitySummary(6.1, 6.2, 6.3, 6.4, 6.5)};
		capabilitySummaryCollection.addCapabilitySummary("3", cs3);
		
		
		selectionExhaustive = new RobotSelectionExhaustive();
	}
	
	
	@Before
    public void beforeEachTest() {
        System.out.println("This is executed before each Test");
    }
    
    
    @After
    public void afterEachTest() {
        System.out.println("This is exceuted after each test");
    }

    
    @Test
    public void justAnExample() {
//    	List<List<CapabilitySummaryNew>>  combinations = selectionExhaustive.getAllCombinations(configurationsCollection, capabilitySummaryCollection);
//    	System.out.println(combinations.size());
//    	for (List<CapabilitySummaryNew> combination : combinations) {
//    		for (CapabilitySummaryNew cs : combination)
//    			System.out.print(cs.getCapabilitySummaryValues().toString() +"\t");
//    		System.out.println();
//    	}
//    	assertEquals(5.5, 5.4, 0.1);
    }

}
