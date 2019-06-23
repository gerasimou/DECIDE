package decide;


/**
 * Class representing a DECIDE exception
 * @author sgerasimou
 *
 */
public class DecideException extends Exception{

	private static final long serialVersionUID = 3322424663356101425L;

	public DecideException(String s)
	{
		super(s);
	}
	
	public String toString()
	{
		return "Error: " + getMessage() + ".";
	}
}