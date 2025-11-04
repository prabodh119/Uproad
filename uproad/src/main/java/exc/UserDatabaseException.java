package exc;

public class UserDatabaseException extends Exception {
    /**
	 * 
	 */
	private static final long serialVersionUID = 5672386297606568650L;

	public UserDatabaseException(String message) {
        super(message);
    }

    public UserDatabaseException(String message, Throwable cause) {
        super(message, cause);
    }
}
