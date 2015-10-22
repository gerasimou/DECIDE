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
	public void execute(Object...args){
		System.out.println(this.getClass().getName()+".execute()");
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
