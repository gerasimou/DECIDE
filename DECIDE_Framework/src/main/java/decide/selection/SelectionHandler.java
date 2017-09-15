package decide.selection;

public class SelectionHandler extends Selection{

	public SelectionHandler() {
		super();
//		System.out.println(this.getClass().getName());
	}
	
	@Override
	public void execute(Object...args){
		System.out.println(this.getClass().getName()+".execute()");		
	}

	
	@Override
	public Selection deepClone(Object... args) {
		// TODO Auto-generated method stub
		return null;
	}

}
