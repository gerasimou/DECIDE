package decide.receipt;

public class CLAReceiptHandler extends CLAReceipt{
	
	public CLAReceiptHandler() {
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
	public CLAReceipt deepClone() {
		CLAReceiptHandler newHandler = new CLAReceiptHandler();
		return newHandler;
	}

	
	public void receive(String msg){
		
	}
}
