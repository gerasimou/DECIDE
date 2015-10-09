package decide.cla;

public class SelectionHandler extends Selection{

	public SelectionHandler() {
		super();
//		System.out.println(this.getClass().getName());
	}
	
	@Override
	public void execute(Object...args){
		System.out.println(this.getClass().getName()+".execute()");		
	}

}
