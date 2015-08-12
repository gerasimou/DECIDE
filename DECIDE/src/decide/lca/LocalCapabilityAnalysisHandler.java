package decide.lca;

public class LocalCapabilityAnalysisHandler extends LocalCapabilityAnalysis {

	public LocalCapabilityAnalysisHandler (){		
//		System.out.println(this.getClass().getName());
	}
	
	@Override
	public void execute() {
		System.out.println(this.getClass().getName()+".execute()");
	}
	
	public void test(){
		System.out.println(this.getClass().getCanonicalName() + ".test");
	}

}
