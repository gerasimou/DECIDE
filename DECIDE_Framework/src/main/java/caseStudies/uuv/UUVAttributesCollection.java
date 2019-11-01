package caseStudies.uuv;

import decide.component.requirements.DECIDEAttribute;
import decide.component.requirements.DECIDEAttributeCollection;
import decide.evaluator.AttributeEvaluatorNew;
import decide.qv.prism.PrismQVNew;

public class UUVAttributesCollection extends DECIDEAttributeCollection {

	@Override
	public void setInternalModelFile() {
		this.internalModelFile = "models/uuv/uuvComplete.sm";
	}

	
	@Override
	public void setEvaluatorPerAttribute() {
		
		//1) create new attribute evaluator
		AttributeEvaluatorNew attributeEvaluator = new PrismQVNew();

		//2) assign the evaluator to each attribute
		for (DECIDEAttribute attribute : values()) {
			attribute.setAttributeEvaluator(attributeEvaluator);
		}		
	}

	

}
