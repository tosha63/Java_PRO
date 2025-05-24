package t1.dto;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum State {
    SUCCESS,
    FAILED,
    UNKNOWN;

    @JsonCreator
    public static State fromString(String value) {
        try {
            return State.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            return UNKNOWN;
        }
    }
}
