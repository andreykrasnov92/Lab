package shared.exceptions;

public class WrongMessageException extends Exception {

    public WrongMessageException(String message) {
        super(message);
    }

    public WrongMessageException(String message, Throwable cause) {
        super(message, cause);
    }
}
