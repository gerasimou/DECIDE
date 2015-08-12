package decide.lca;

public class LocalCapabilityAnalysisHandler implements LocalCapabilityAnalysis {

	public LocalCapabilityAnalysisHandler (){
		
	}
	
	@Override
	public void execute() {
		System.out.println(this.getClass().getName());
	}
	
	public void test(){
		System.out.println(this.getClass().getCanonicalName() + ".test");
	}

}
