package decide.localAnalysis;

import decide.qv.prism.QV;

public class LocalCapabilityAnalysisHandler extends LocalCapabilityAnalysis {

	public LocalCapabilityAnalysisHandler (QV qvInstance){		
		super(qvInstance);
//		System.out.println(this.getClass().getName());
	}

	
	@Override
	public void execute(Object... args) {
//		System.out.println(this.getClass().getName()+".execute()");
		
		client.send((String)args[0]);
	}

}
