package t1.exception;

import lombok.Getter;

@Getter
public class IntegrationException extends RuntimeException{
    private final String reason;

    public IntegrationException(String reason, String message) {
        super(message);
        this.reason = reason;
    }
}
