package shared.exceptions;

public class StringFormatException extends Exception {

    public StringFormatException(String message) {
        super(message);
    }

    public StringFormatException(String message, Throwable cause) {
        super(message, cause);
    }
}
