package exc;

public class DuplicateUserException extends UserDatabaseException {
	private static final long serialVersionUID = -3267250052300443292L;
	/**
	 * 
	 */
	public DuplicateUserException(String message) {
        super(message);
    }	

}
