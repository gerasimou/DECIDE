package decide.localControl;

import decide.qv.prism.QV;

public class LocalControlHandler extends LocalControl{

	public LocalControlHandler(QV qvInstance) {
		super(qvInstance);
//		System.out.println(this.getClass().getName());		
	}
	
	@Override
	public void execute(Object...args){
		System.out.println(this.getClass().getName()+".execute()");
	}

}
