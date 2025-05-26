package t1.exception;

public class ExceedingLimitException extends RuntimeException {
    public ExceedingLimitException(String message) {
        super(message);
    }
}
