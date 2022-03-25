package server.exceptions;

/**
 * Thrown when a user create attempt failed because the user already existed
 */
public class UserAlreadyExistsException extends Exception {

    /**
     * @param message The error message
     */
    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
