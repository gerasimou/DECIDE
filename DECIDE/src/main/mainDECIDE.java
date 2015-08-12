package main;

import decide.DECIDE;
import decide.cla.SelectionHandler;
import decide.lca.LocalCapabilityAnalysis;
import decide.lca.LocalCapabilityAnalysisHandler;
import decide.lcl.LocalControlHandler;
import decide.receipt.CLAReceiptHandler;

public class mainDECIDE {

	
	public static void main(String[] args) {
		
		System.out.println("Starting DECIDE");
		LocalCapabilityAnalysis lcaHandler 			= new LocalCapabilityAnalysisHandler();
		CLAReceiptHandler       receiptHandler		= new CLAReceiptHandler();
		SelectionHandler		selectionHandler	= new SelectionHandler();
		LocalControlHandler		localControlHandler	= new LocalControlHandler();
		
		DECIDE decide = new DECIDE(lcaHandler, receiptHandler, selectionHandler, localControlHandler);
		decide.run();
	}
	

	
	
	
	
	
	private static void runMe(String parameter){
//		if(logger.isDebugEnabled()){
//			logger.debug("This is debug : " + parameter);
//		}
//		
//		if(logger.isInfoEnabled()){
//			logger.info("This is info : " + parameter);
//		}
//		
//		logger.warn("This is warn : " + parameter);
//		logger.error("This is error : " + parameter);
//		logger.fatal("This is fatal : " + parameter);
//		
	}
}
