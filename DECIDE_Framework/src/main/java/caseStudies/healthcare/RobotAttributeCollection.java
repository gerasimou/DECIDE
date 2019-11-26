package caseStudies.healthcare;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import auxiliary.Utility;
import decide.Knowledge;
import decide.component.requirements.DECIDEAttribute;
import decide.component.requirements.DECIDEAttributeCollection;
import decide.component.requirements.ModelTemplateDelegate;
import decide.component.requirements.reqNew.LocalConstraint;
import decide.configuration.Configuration;
import decide.evaluator.AttributeEvaluatorNew;
import decide.qv.prism.PrismQVNew;

public class RobotAttributeCollection extends DECIDEAttributeCollection {

	
	@Override
	public void setInternalModelFile() {
		this.internalModelFile = "models/healthcare/roomDummy.prism";
	}

	
	@Override
	public void setEvaluatorPerAttribute() {
		//1) create new attribute evaluator
		AttributeEvaluatorNew prismEvaluator 	= new PrismQVNew();
		AttributeEvaluatorNew binaryEvaluator 	= new RobotBinaryEvaluator("t", 100, 20);
				

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
		get("Utility").setAttributeEvaluator(prismEvaluator);
	}


	
	@Override
	public void customiseModelTemplatePerAttribute() {
		StringBuilder paramsRoomT1 = new StringBuilder();
		paramsRoomT1.append("const double rt1l1i = " 		+ Utility.getProperty("rt1l1i") 	+";\n");
		paramsRoomT1.append("const double rt1l2i = " 		+ Utility.getProperty("rt1l2i") 	+";\n");
		paramsRoomT1.append("const double rt1l2ip = " 		+ Utility.getProperty("rt1l2ip") 	+";\n");
		paramsRoomT1.append("const double rt1p2norm = " 	+ Utility.getProperty("rt1p2norm") 	+";\n");
		paramsRoomT1.append("const double rt1p2retry = " 	+ Utility.getProperty("rt1p2retry") +";\n");
		paramsRoomT1.append("const double rt1c1i = " 		+ Utility.getProperty("rt1c1i") 	+";\n");
		paramsRoomT1.append("const double rt1c2i = " 		+ Utility.getProperty("rt1c2i") 	+";\n");

		StringBuilder paramsRoomT2 = new StringBuilder();
		paramsRoomT2.append("const double rt2l1i = " 		+ Utility.getProperty("rt2l1i") 	+";\n");
		paramsRoomT2.append("const double rt2l2i  = " 		+ Utility.getProperty("rt2l2i") 	+";\n");
		paramsRoomT2.append("const double rt2l3i = " 		+ Utility.getProperty("rt2l3i") 	+";\n");
		paramsRoomT2.append("const double rt2l3ip = " 		+ Utility.getProperty("rt2l3ip") 	+";\n");
		paramsRoomT2.append("const double rt2p2req = " 		+ Utility.getProperty("rt2p2req") 	+";\n");
		paramsRoomT2.append("const double rt2p2retry = " 	+ Utility.getProperty("rt2p2retry") +";\n");
		paramsRoomT2.append("const double rt2p3poss = " 	+ Utility.getProperty("rt2p3poss") 	+";\n");
		paramsRoomT2.append("const double rt2c1i = " 		+ Utility.getProperty("rt2c1i") 	+";\n");
		paramsRoomT2.append("const double rt2c2i = " 		+ Utility.getProperty("rt2c2i") 	+";\n");
		paramsRoomT2.append("const double rt2c3i = " 		+ Utility.getProperty("rt2c3i") 	+";\n");
		paramsRoomT2.append("const double rt2c3ip = " 		+ Utility.getProperty("rt2c3ip") 	+";\n");
		paramsRoomT2.append("const double rt2u3 = " 		+ Utility.getProperty("rt2u3") 		+";\n");
		paramsRoomT2.append("const double rt2u3p = " 		+ Utility.getProperty("rt2u3p")		+";\n");

		
		//Customise attribute Room Type 1 Cost :R{"c"}=? [ F "end" ] --> Normal Prism
		DECIDEAttribute rt1CostAttr = get("Room-Type1-Cost"); 
		String room1C = rt1CostAttr.getModelTemplate();
		rt1CostAttr.setModelTemplate(room1C +"\n"+ paramsRoomT1);

		
		//Customise attribute Room Type 1 Time: P>=0.095[F<=t "end"] --> Binary
		DECIDEAttribute rt1TimeAttr = get("Room-Type1-Time"); 
		String room1T = rt1TimeAttr.getModelTemplate();
		rt1TimeAttr.setModelTemplate(room1T +"\n"+ paramsRoomT1);

		
		//Customise attribute Room Type 2 Cost :R{"c"}=? [ F "end" ] --> Normal Prism
		DECIDEAttribute rt2CostAttr = get("Room-Type2-Cost"); 
		String room2C = rt2CostAttr.getModelTemplate();
		rt2CostAttr.setModelTemplate(room2C +"\n"+ paramsRoomT2);

		
		//Customise attribute Room Type 2 Time: P>=0.095[F<=t "end"] --> Binary
		DECIDEAttribute rt2TimeAttr = get("Room-Type2-Time"); 
		String room2T = rt2TimeAttr.getModelTemplate();
		rt2TimeAttr.setModelTemplate(room2T +"\n"+ paramsRoomT2);


		//Customise attribute servicing travel,ti = min{t>0|pmc(Mi,P≥.95[F≤t end])}
		DECIDEAttribute servTimeAttr = get("Servicing-Time");
		servTimeAttr.setModelTemplateDelegate(new RobotPPModelTemplateDelegate("srv", servTimeAttr.getModelTemplate() +"\n"+ paramsRoomT1 +"\n"+ paramsRoomT2) {
			@Override
			public String getModelTemplate(Configuration configuration) {
				List<LocalConstraint> responsibilitiesList = Knowledge.getResponsibilities();
				
				int MYROOMST1 = RobotKnowledge.getMyRoomsT1();
				int MYROOMST2 = RobotKnowledge.getMyRoomsT2();
				
				String[] ppArgs 		= {roomsPPTemplate, MYROOMST1 +"", MYROOMST2 +""};
				String model			= lmb.preprocess(ppArgs);
				
				model +=  "\n"+ paramsRoomT1 +"\n"+ paramsRoomT2;
				return model;
			}
		});
		
		
		DECIDEAttribute utilityAttr = get("Utility");
		utilityAttr.setModelTemplateDelegate(new RobotPPModelTemplateDelegate("util", utilityAttr.getModelTemplate()+"\n"+ paramsRoomT1 +"\n"+ paramsRoomT2) {
			@Override
			public String getModelTemplate(Configuration configuration) {
				List<LocalConstraint> responsibilitiesList = Knowledge.getResponsibilities();		
				
				int MYROOMST1 = RobotKnowledge.getMyRoomsT1();
				int MYROOMST2 = RobotKnowledge.getMyRoomsT2();
				
				String[] ppArgs 		= {roomsPPTemplate, MYROOMST1 +"", MYROOMST2 +""};
				String model			= lmb.preprocess(ppArgs);
				
				model +=  "\n"+ paramsRoomT1 +"\n"+ paramsRoomT2;

				return model;
			}
		}); 
	}	
	
	
	
}



abstract class RobotPPModelTemplateDelegate extends ModelTemplateDelegate{
	public 		String roomsPPTemplate;
	protected 	LocalModelBuilder lmb;
	
	protected RobotPPModelTemplateDelegate  (String name, String modelTemplate) {
		super(modelTemplate);
		try {
			lmb 	= new LocalModelBuilderCustom();
			
			roomsPPTemplate = "models/healthcare/local/rooms_" + name +".pp";
			new File(roomsPPTemplate).createNewFile();
			BufferedWriter writer = new BufferedWriter(new FileWriter(roomsPPTemplate));
			writer.write(modelTemplate);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
}
