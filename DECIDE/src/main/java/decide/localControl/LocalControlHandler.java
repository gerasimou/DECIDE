package decide.localControl;

public class LocalControlHandler extends LocalControl{

	public LocalControlHandler() {
		super();
//		System.out.println(this.getClass().getName());		
	}
	
	@Override
	public void execute(Object...args){
		System.out.println(this.getClass().getName()+".execute()");
	}

}
