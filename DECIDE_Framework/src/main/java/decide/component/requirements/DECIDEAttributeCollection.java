package decide.component.requirements;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import auxiliary.Utility;
import decide.DECIDEConstants;
import decide.DecideException;


public abstract class DECIDEAttributeCollection extends ConcurrentHashMap<String, DECIDEAttribute>{

//	/**List of model template files in the config.properties file as many as the attributes 
//	 * e.g., MODELS_FILES = M1.pm,M2.pm **/
//	protected String MODELS_FILES 	= Utility.getProperty(DECIDEConstants.MODELS_FILE_KEYWORD);
	
	/**File with list of properties as many as the attributes**/
	protected String PROPERTIES_FILE	= Utility.getProperty(DECIDEConstants.PROPERTIES_FILE_KEYWORD);
	
	
	protected String internalModelFile;
	

	public DECIDEAttributeCollection () {
		initAttributes();
	}
	
	
	/**
	 * Initialise attributes by parsing the <b>model file</b> and <b>properties file</b> properties
	 * defined in the configuration script
	 */
	private void initAttributes() {
		
		//set the internal model file
		setInternalModelFile(); 
		
		//read the internal model that enables to validate the properties
		String internalModel = Utility.readFile(internalModelFile);
		
		//generate the String-Attributes map
		Map<String, DECIDEAttribute> attributesMap = AttributeFactory.generate(internalModel);
		
		//add attributes map
		putAll(attributesMap);
		
		//set evaluator for each attribute
		setEvaluatorPerAttribute();
	}

	
	@Override
	public DECIDEAttribute get (Object key) {
		return super.get(key.toString().toUpperCase());
	}
	
	public abstract void setEvaluatorPerAttribute();
	
	
	public abstract void setInternalModelFile();
}
