package decide.cla;

public class SelectionHandler extends Selection{

	public SelectionHandler() {
//		System.out.println(this.getClass().getName());
	}
	
	@Override
	public void execute() {
		System.out.println(this.getClass().getName()+".execute()");		
	}

}
