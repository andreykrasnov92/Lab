package shared.exceptions;

public class EmptyStringException extends Exception {

    public EmptyStringException(String message) {
        super(message);
    }

    public EmptyStringException(String message, Throwable cause) {
        super(message, cause);
    }
}
