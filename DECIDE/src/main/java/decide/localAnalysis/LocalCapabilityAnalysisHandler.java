package decide.localAnalysis;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import decide.qv.QV;

public class LocalCapabilityAnalysisHandler extends LocalCapabilityAnalysis {

	/**
	 * Class constructor
	 * @param qvInstance
	 */
	public LocalCapabilityAnalysisHandler (QV qvInstance){
		super();
		this.qv = qvInstance;
//		System.out.println(this.getClass().getName());
	}
	
	
	/**
	 * Class <b>copy</b> constructor
	 */
	private LocalCapabilityAnalysisHandler (LocalCapabilityAnalysisHandler instance) {
		super();
//		this.client	= instance.client.deepClone();
	}

	
	@Override
	public void execute(Object... args) {
		System.err.println(this.getClass().getSimpleName()+".execute()");
		
		//Step 1) Carry out DECIDE-based quantitative verification
		qv.run(Arrays.copyOfRange(args, 1,args.length));
		
		//Step 2) Find the best result per configuration subset
		
		
		//Step 3) Assemble capability summary
		
		
//		for (Object object : resultsList){
//			System.out.println(object.toString());
//		}
//		
//		client.send((String)args[0]);
	}

	
	public LocalCapabilityAnalysis deepClone(Object ... args){
		LocalCapabilityAnalysis newHandler = new LocalCapabilityAnalysisHandler(this);
		newHandler.qv = (QV) args[0];
		return newHandler;
	}	
	

}
