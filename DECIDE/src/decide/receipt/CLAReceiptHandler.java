package decide.receipt;

public class CLAReceiptHandler implements CLAReceipt{
	
	public CLAReceiptHandler() {
		System.out.println(this.getClass().getName());
	}

	@Override
	public void execute() {
		System.out.println(this.getClass().getName()+".execute()");
	}

}