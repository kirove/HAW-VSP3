package cash_access;

public class OverdraftException extends Exception { 
	/**
	 * 
	 */
	private static final long serialVersionUID = 2848167533623958234L;

	public OverdraftException(String message) {
		super(message);
	} 
}