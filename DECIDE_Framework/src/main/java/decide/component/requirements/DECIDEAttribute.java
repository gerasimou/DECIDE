package decide.component.requirements;

import auxiliary.Utility;
import decide.configuration.Configuration;
import decide.evaluator.AttributeEvaluatorNew;

public class DECIDEAttribute{
	
	/** Stochastic model & properties filenames*/
	protected final String MODEL_FILENAME;

	/** */
	protected final String PROPERTY;
	
	/** */
//	protected String modelTemplate;
	
	/** */
	protected AttributeEvaluatorNew attrEvaluator;
	
	/** */
	protected final String name;
	
	/** */
	protected final DECIDEAttributeType attrType;
	
	protected ModelTemplateDelegate modelTemplateDelegate;


	
	public DECIDEAttribute (String attributeName, String modelFilename, String property, DECIDEAttributeType attributeType) {//, AttributeEvaluatorNew evaluator) {
		MODEL_FILENAME 			= modelFilename;
//		modelTemplate			= Utility.readFile(MODEL_FILENAME);
		PROPERTY				= property;
		name					= attributeName;
		attrType				= attributeType;
		attrEvaluator			= null;
		modelTemplateDelegate	= new DefaultModelTemplateDelegate(Utility.readFile(MODEL_FILENAME));
	}

	
	/**
	 * Return model template for verification
	 * @return
	 */
	public String getModelTemplate(Configuration configuration) {
//		return this.modelTemplate +"\n";
		return modelTemplateDelegate.getModelTemplate(configuration);
	}

	
	public String getModelTemplate() {
		return modelTemplateDelegate.getModelTemplate(null);
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
	
	
	/**
	 * Get Model Filename
	 * @return
	 */
	public String getModelFileName() {
		return this.MODEL_FILENAME;
	}
	
	
	/**
	 * 
	 */
	public void setModelTemplateDelegate (ModelTemplateDelegate mtDelegate) {
		modelTemplateDelegate = mtDelegate;
	}
	
	
	/**
	 * 
	 * @param modelTemplate
	 */
	public void setModelTemplate (String modelTemplate) {
		modelTemplateDelegate.setModelTemplate(modelTemplate);
	}

}
