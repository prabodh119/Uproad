package exc;

public class EmailException extends Exception{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4453564740094967684L;

	public EmailException(String message) {
        super(message);
    }

    public EmailException(String message, Throwable cause) {
        super(message, cause);
    }
}
