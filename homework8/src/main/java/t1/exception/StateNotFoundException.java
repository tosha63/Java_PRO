package t1.exception;

public class StateNotFoundException extends RuntimeException {
    public StateNotFoundException() {
        super("Передано некорректное состояние платежа");
    }
}
