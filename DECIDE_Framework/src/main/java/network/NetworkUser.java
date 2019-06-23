package network;

public interface NetworkUser {

	/**
	 * Function for receiving <b>message</b> from <b>serverAddress</b>
	 * @param senderAddress
	 * @param message
	 */
	public void receive (String senderAddress, Object message);

}
