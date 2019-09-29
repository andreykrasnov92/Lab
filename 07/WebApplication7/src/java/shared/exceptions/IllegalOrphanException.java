package shared.exceptions;

public class IllegalOrphanException extends Exception {

    public IllegalOrphanException(String message) {
        super(message);
    }

    public IllegalOrphanException(String message, Throwable cause) {
        super(message, cause);
    }
}
