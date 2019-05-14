package decide.receipt;

public class CLAReceiptHandler extends CLAReceipt{
	
	/**
	 * Class constructor
	 */
	public CLAReceiptHandler() {
		super();
	}
	
	
	/**
	 * Class <b>copy</b> constructor
	 */
	private CLAReceiptHandler(CLAReceiptHandler instance) {
		super();
	}


	/**
	 * Execute this CLAReceipt handler
	 */
	@Override
	public boolean execute(Object...args){
		System.out.println(this.getClass().getSimpleName()+".execute()");
		return false;
	}

	@Override
	public boolean executeListeningThread()
	{
		System.out.println(this.getClass().getSimpleName()+".execute()");
		return true;
	}
	
	/**
	 * Clone the CLAReceipt handler
	 */
	@Override
	public CLAReceipt deepClone(Object ... args) {
		CLAReceipt newHandler = new CLAReceiptHandler(this);
		return newHandler;
	}	
}
