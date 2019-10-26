package decide;

/**
Collected constants of general utility.
<P>All members of this class are immutable. 
*/

public class DECIDEConstants {

	  /** The caller should be prevented from constructing objects of 
	   * this class, by declaring this private constructor. 
	   */
	  private DECIDEConstants() {
	    //this prevents even the native class from 
	    //calling this ctor as well :
	    throw new AssertionError();
	  }
	  
	  /** Keyword for probabilistic model template*/
	  public static final String MODELS_FILE_KEYWORD 		= "MODEL_TEMPLATE_FILE";

	  /** Keyword for probabilistic properties file*/
	  public static final String PROPERTIES_FILE_KEYWORD 	= "PROPERTIES_FILE";

	  /** Keyword for properties */
	  public static final String LCA_KEYWORD 				= "LCA";

	  /** Keyword for properties */
	  public static final String LOCAL_CONTROL_KEYWORD 		= "LOCAL_CONTROL";

	  /** Keyword for properties */
	  public static final String PRISM_OUTPUT_FILENAME 		= "PRISM_OUTPUT_FILENAME";

	  public static final String LOCAL_CONTROL_TIME_WINDOW	= "LOCAL_CONTROL_TIME_WINDOW";
	  
	  public static final String a_CONFIDENCE = "a_CONFIDENCE";
	  
	  public static final String HEARTBEAT_TIME_WINDOW = "HEARTBEAT_TIME_WINDOW";
	  
	  public static final String DECIDE_LOOP_TIME_WINDOW = "DECIDE_LOOP_TIME_WINDOW";
	  
	  public static final String CLA_TIME_WINDOW = "CLA_TIME_WINDOW";
	  
}
