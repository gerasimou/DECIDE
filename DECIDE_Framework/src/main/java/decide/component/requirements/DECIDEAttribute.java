package decide.component.requirements;

import auxiliary.Utility;
import decide.configuration.ConfigurationNew;
import decide.evaluator.AttributeEvaluatorNew;

public class DECIDEAttribute {
	
	/** Stochastic model & properties filenames*/
	protected final String MODEL_FILENAME;

	/** */
	protected final String PROPERTY;
	
	/** */
	protected String modelTemplate;
	
	/** */
	protected AttributeEvaluatorNew attrEvaluator;
	
	/** */
	protected final String name;
	
	/** */
	protected final DECIDEAttributeType attrType;


	
	public DECIDEAttribute (String attributeName, String modelFilename, String property, DECIDEAttributeType attributeType) {//, AttributeEvaluatorNew evaluator) {
		MODEL_FILENAME 	= modelFilename;
		modelTemplate	= Utility.readFile(MODEL_FILENAME);
		PROPERTY		= property;
		name			= attributeName;
		attrType		= attributeType;
	}

	
//	/**
//	 * Set the verification result for this attribute
//	 * @param result
//	 */
//	public void setVerificationResult (Object result) {
//		this.result = result;
//	}
//	
//	
//	/**
//	 * Get verification result
//	 * @return
//	 */
//	public Object getVerificationResult() {
//		return this.result;
//	}
	
	
	/**
	 * Return model template for verification
	 * @return
	 */
	public String getModelTemplate(ConfigurationNew configuration) {
		return this.modelTemplate +"\n";
	}
	

	/**
	 * Set the model template for this attribute. 
	 * This functionality enables to amend the model for this attribute dynamically,
	 * as we do in the healthcare case study
	 * 
	 * @param modelTemplate
	 */
	public void setModelTemplate (String modelTemplate) {
		this.modelTemplate = modelTemplate;
	}
	
	
	/**
	 * Return property for evaluation;
	 * @return
	 */
	public String getProperty() {
		return this.PROPERTY;
	}
	
	
	/**
	 * Set the evaluator for this attribute
	 * @param evaluator
	 */
	public void setAttributeEvaluator (AttributeEvaluatorNew evaluator) {
		this.attrEvaluator = evaluator;
	}
	

	/**
	 * Get the evaluator for this attribute
	 * @return
	 */
	public AttributeEvaluatorNew getAttributeEvaluator() {
		return this.attrEvaluator;
	}
	
	
	/**
	 * Get the name of this attribute
	 * @return
	 */
	public String getAttributeName () { 
		return this.name;
	}
	
	
	
	/**
	 * Get the type of this attribute
	 * @return
	 */
	public DECIDEAttributeType getAttributeType() {
		return this.attrType;
	}
	

}
