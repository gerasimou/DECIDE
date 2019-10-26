package caseStudies.healthcare;

import java.util.List;

import decide.KnowledgeNew;
import decide.component.requirements.DECIDEAttribute;
import decide.component.requirements.DECIDEAttributeCollection;
import decide.component.requirements.DECIDEAttributeType;
import decide.component.requirements.reqNew.LocalConstraintNew;
import decide.configuration.ConfigurationNew;
import decide.evaluator.AttributeEvaluatorNew;
import decide.evaluator.BinaryEvaluator;
import decide.qv.prism.PrismQVNew;

public class RobotAttributeCollection extends DECIDEAttributeCollection {

	
	@Override
	public void setInternalModelFile() {
		this.internalModelFile = "models/healthcare/local/t2room.prism";
	}

	
	@Override
	public void setEvaluatorPerAttribute() {
		//1) create new attribute evaluator
		AttributeEvaluatorNew prismEvaluator 	= new PrismQVNew();
		AttributeEvaluatorNew binaryEvaluator 	= new BinaryEvaluator("t", 100, 20);

		
		//Assign evaluator for property Room Type 1 Cost :R{"c"}=? [ F "end" ] --> Normal Prism
		get("Room-Type1-Cost").setAttributeEvaluator(prismEvaluator);

		//Assign evaluator for property Room Type 1 Time: P>=0.095[F<=t "end"] --> Binary
		get("Room-Type1-Time").setAttributeEvaluator(binaryEvaluator);

		//Assign evaluator for property Room Type2 Cost: R{"c"}=? [ F "end" ] --> Normal Prism
		get("Room-Type2-Cost").setAttributeEvaluator(prismEvaluator);

		//Assign evaluator for property Room Type 2 Time: P>=0.095[F<=t "end"] --> Binary
		get("Room-Type2-Time").setAttributeEvaluator(binaryEvaluator);

		//Assign evaluator for property travelling travel,i = min{t>0|pmc(Mtravel,i,P≥.95[F≤t end])} --> Binary
		get("Travelling-Time").setAttributeEvaluator(binaryEvaluator);

		//Assign evaluator for property servicing travel,ti = min{t>0|pmc(Mi,P≥.95[F≤t end])} --> Binary
		get("Servicing-Time").setAttributeEvaluator(binaryEvaluator);

		//Assign evaluator for property travelling travel, ui = pmc(Mi, Rutility[F end]) - Normal Prism
		get("Utility").setAttributeEvaluator(binaryEvaluator);
	}
	
	
	public void changeTemplate() {
		DECIDEAttribute attr = get("Servicing-Time");
//		attr.
	}

}

class RobotAttribute extends DECIDEAttribute {
	
	public RobotAttribute(String attributeName, String modelFilename, String property, DECIDEAttributeType attributeType) {
		super(attributeName, modelFilename, property, attributeType);
	}
	
	
	@Override
	public String getModelTemplate(ConfigurationNew configuration) {
		List<LocalConstraintNew> responsibilitiesList = KnowledgeNew.getResponsibilities();
		
		
		
		String[] ppArgs 		= {"models/healthcare/local/rooms.pp", "3", "5"};
		LocalModelBuilder lmb 	= new LocalModelBuilder(ppArgs);
		String model			= lmb.preprocess();
		
		return model;
	}
	
}

