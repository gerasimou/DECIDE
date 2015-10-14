package decide.localControl;

import decide.qv.QV;

public class LocalControlHandler extends LocalControl{

	/**
	 * Class constructor
	 * @param qvInstance
	 */
	public LocalControlHandler(QV qvInstance) {
		super();
		this.qv = qvInstance;
//		System.out.println(this.getClass().getName());		
	}
	
	
	/**
	 * Class <b>copy</b> constructor
	 */
	private LocalControlHandler (LocalControlHandler instance) {
		super();
//		this.client	= instance.client.deepClone();
	}

	
	@Override
	public void execute(Object...args){
		System.out.println(this.getClass().getName()+".execute()");
	}
	
	
	
	public LocalControl deepClone(Object ... args){
		LocalControl newHandler = new LocalControlHandler(this);
		newHandler.qv = (QV) args[0];
		return newHandler;
	}


}