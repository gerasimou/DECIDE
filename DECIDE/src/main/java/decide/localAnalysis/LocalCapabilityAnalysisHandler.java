package decide.localAnalysis;

import decide.qv.prism.QV;

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
		this.qv 	= null;//instance.qv.deepClone();
//		this.client	= instance.client.deepClone();
	}

	
	@Override
	public void execute(Object... args) {
//		System.out.println(this.getClass().getName()+".execute()");
		
		client.send((String)args[0]);
	}

	
	public LocalCapabilityAnalysis deepClone(){
		LocalCapabilityAnalysis newHandler = new LocalCapabilityAnalysisHandler(this);
		return newHandler;
	}

}
