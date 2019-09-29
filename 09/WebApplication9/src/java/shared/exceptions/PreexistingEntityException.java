package shared.exceptions;

public class PreexistingEntityException extends Exception {

    public PreexistingEntityException(String message) {
        super(message);
    }

    public PreexistingEntityException(String message, Throwable cause) {
        super(message, cause);
    }
}
