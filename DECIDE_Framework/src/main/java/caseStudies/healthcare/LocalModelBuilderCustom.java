package caseStudies.healthcare;

public class LocalModelBuilderCustom extends LocalModelBuilder {

	public LocalModelBuilderCustom () {
		super();
	}
	
	
	
	@Override
	public String preprocess(String[] ppArgs) {
		int RT1 = Integer.parseInt(ppArgs[1]);
		int RT2 = Integer.parseInt(ppArgs[2]);
		int end = RT1+RT2+1;
		int mf  = 100;
		StringBuilder str = new StringBuilder();
		
		str.append("// Rooms joint CTMC model template\n" 								+ 
				"// ------------------------------------------------------------\n\n" 	+ 
				"ctmc \n");
		
		str.append("// Model parameters\n" 												+
				   "const GO=0; // Initial state\n\n" 									+	 
				   "// Are we there yet?\n" 											+ 
				   "label \"end\" = s=r" +(end) +"T1;\n");
		
		str.append("\n// Rooms Type 1 constants-----------\n"); 
		for (int i=1; i<=RT1; i++) {
			str.append("const int r"+ i + "T1="   + i*mf+0 +"; ");			
			str.append("const int r"+ i + "T2="   + i*mf+1 +"; ");			
			str.append("const int r"+ i + "T2P="  + i*mf+2 +"; ");
			str.append("const int r"+ i + "END="  + i*mf+3 +";\n");
		}

		str.append("\n// Rooms Type 2 constants-----------\n"); 
		for (int i=RT1+1; i<=RT1+RT2; i++) {
			str.append("const int r"+ i + "T1="   + i*mf+0 +"; ");			
			str.append("const int r"+ i + "T2="   + i*mf+1 +"; ");			
			str.append("const int r"+ i + "T3="   + i*mf+2 +"; ");			
			str.append("const int r"+ i + "T3P="  + i*mf+3 +"; ");
			str.append("const int r"+ i + "END="  + i*mf+4 +";\n");
		}

		str.append("\n// All Rooms Done constant-----------\n"); 
		str.append("const int r"+ end + "T1="   + end*mf+0 +"; ");			

		
		str.append("\n// Rooms Module-----------\n");
		str.append("module rooms\n");
		str.append( 
			   "  s:[GO..r" +(end) +"T1] init GO;\n"  +  
			   "// Travel\n" + 
			   " [travel] (s=GO) -> v_i/d_i: (s'=r1T1);\n");

		
		str.append("\n// Rooms Type 1\n");
		for (int i=1; i<=RT1; i++) {
			str.append("[r" +i+ "T1done] (s=r" +i+ "T1) ->  rt1l1i*rt1p2norm:   (s\'=r" +i+ "T2) + rt1l1i*(1-rt1p2norm):    (s\'=r" +i+ "T2P);\n");
			str.append("[r" +i+ "T2done] (s=r" +i+ "T2) ->  rt1l2i*rt1p2retry:  (s\'=r" +i+ "T2) + rt1l2i*(1-rt1p2retry):   (s\'=r" +(i+1)+ "T1);\n");
			str.append("[r" +i+ "T2done] (s=r" +i+ "T2P) -> rt1l2ip*rt1p2retry: (s\'=r" +i+ "T2P) + rt1l2ip*(1-rt1p2retry): (s\'=r" +(i+1)+ "T1);\n");
		}
		
		str.append("\n// Rooms Type 2\n");
		for (int i=RT1+1; i<=RT1+RT2; i++) {
			str.append("[r" +i+ "T1done] (s=r" +i+ "T1) -> rt2l1i*(1-rt2p2req)*(1-rt2p3poss): (s\'=r" +(i+1)+ "T1) + " 		+
					   									  "rt2l1i*(1-rt2p2req)*rt2p3poss*rt2p3ifull: (s\'=r" +i+ "T3) + " 	+ 
					   									  "rt2l1i*rt2p2req: (s\'=r" +i+ "T2) +"								+
					   									  "rt2l1i*(1-rt2p2req)*rt2p3poss*(1-rt2p3ifull): (s\'=r" +i+ "T3P);\n");

			str.append("[r" +i+ "T2done] (s=r" +i+ "T2) -> rt2l2i*(1-rt2p2retry)*rt2p3poss*rt2p3ifull: (s\'=r" +(i)+ "T1) + "	+
						  "rt2l2i*(1-rt2p2retry)*(1-rt2p3poss): (s\'=r" +(i+1)+ "T1) + " 										+ 
						  "rt2l2i*(1-rt2p2retry)*rt2p3poss*(1-rt2p3ifull): (s\'=r" +i+ "T3P) +"									+
						  "rt2l2i*rt2p2retry: (s\'=r" +i+ "T2);\n");

			str.append("[r" +i+ "T3done] (s=r" +i+ "T3)   -> rt2l3i: (s\'=r" +(i+1)+ "T1);\n");
			str.append("[r" +i+ "T3Pdone] (s=r" +i+ "T3P) -> rt2l3ip: (s\'=r" +(i+1)+ "T1);\n");
		}

		
		str.append("\n// All Done\n");
		str.append("[rAlldone] (s=r" +end+ "T1) -> rt1l1i: (s\'=r" +end+ "T1);\n");

		str.append("\nendmodule\n");

		
		str.append("\n// Cost reward\n");
		str.append("rewards \"c\"\n");
		for (int i=1; i<=RT1; i++) {
			str.append("[r" +i+ "T1done] true : rt1c1i;\n");
			str.append("[r" +i+ "T2done] true : rt1c2i;\n");			
		} 
		for (int i=RT1+1; i<=RT1+RT2; i++) {
			str.append("[r" +i+ "T1done] true  : rt2c1i;\n");
			str.append("[r" +i+ "T2done] true  : rt2c2i;\n");
			str.append("[r" +i+ "T3done] true  : rt2c3i;\n");
			str.append("[r" +i+ "T3Pdone] true : rt2c3ip;\n");
		}
		str.append("endrewards\n");				  

		
		str.append("\n// Utility reward\n");
		str.append("rewards \"u\"\n");
		for (int i=RT1+1; i<=RT1+RT2; i++) {
			str.append("[r" +i+ "T3done] true  : rt2u3;\n");
			str.append("[r" +i+ "T3Pdone] true : rt2u3p;\n");
		}
		str.append("endrewards\n");				  

		
		str.append("\n// Time reward\n");
		str.append("rewards \"t\"\n");
		str.append("   true : 1;\n");
		str.append("endrewards\n");				  

		
		return str.toString();
				
	}
	
	
	// Class test
	public static void main(String[] args) {
						
		String workPath = "models/healthcare/local/";//Users/sgerasimou/Documents/Git/DECIDE/DECIDE_Framework/models/healthcare/local/";
		String[] ppArgs = {workPath+"rooms.pp", "0", "2"};
		String modelFile = workPath+"rooms.prism";
		LocalModelBuilderCustom lmb = new LocalModelBuilderCustom();
//		lmb.exportModel(lmb.preprocess(), lmb.getM_out_model_file());
//		lmb.shutDown();
		System.out.println(lmb.preprocess(ppArgs));
	}
	
}
