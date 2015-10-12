package decide.localAnalysis;

public class LocalCapabilityAnalysisHandler extends LocalCapabilityAnalysis {

	public LocalCapabilityAnalysisHandler (){		
		super();
//		System.out.println(this.getClass().getName());
	}

	@Override
	public void execute(Object... args) {
//		System.out.println(this.getClass().getName()+".execute()");
		
		client.send((String)args[0]);
	}

}
