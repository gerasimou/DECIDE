package decide.component.requirements;

import auxiliary.Utility;

public class DECIDEAttribute {
	
	/** Stochastic model & properties filenames*/
	protected final String MODEL_FILENAME;
	protected final String PROPERTY;
	protected final String modelTemplate;
//	protected String name;

	protected Object result;
	
	
	/**
	 * Attribute class constructor
	 * @param modelFilename
	 * @param property
	 */
	public DECIDEAttribute (String modelFilename, String property) {
		MODEL_FILENAME 	= modelFilename;
		modelTemplate	= Utility.readFile(MODEL_FILENAME);
		PROPERTY			= property;
//		name				= attributeName;
	}
	
	
	/**
	 * Set the verification result for this attribute
	 * @param result
	 */
	public void setVerificationResult (Object result) {
		this.result = result;
	}
	
	
	/**
	 * Get verification result
	 * @return
	 */
	public Object getVerificationResult() {
		return this.result;
	}
	
	
	/**
	 * Return model template for verification
	 * @return
	 */
	public String getModelTemplate() {
		return this.modelTemplate +"\n";
	}
	
	
	/**
	 * Return property for evaluation;
	 * @return
	 */
	public String getProperty() {
		return this.PROPERTY;
	}
	
//	/**
//	 * Get the name of this attribute
//	 * @return
//	 */
//	public String getAttributeName() {
//		return this.name;
//	}
	

}
