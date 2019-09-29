package shared.exceptions;

public class DataUpdatingException extends Exception {

    public DataUpdatingException(String message) {
        super(message);
    }

    public DataUpdatingException(String message, Throwable cause) {
        super(message, cause);
    }
}
