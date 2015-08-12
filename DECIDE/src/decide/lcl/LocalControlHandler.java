package decide.lcl;

public class LocalControlHandler implements LocalControl{

	public LocalControlHandler() {
		System.out.println(this.getClass().getName());		
	}
	
	@Override
	public void execute() {
		System.out.println(this.getClass().getName()+".execute()");
	}

}
