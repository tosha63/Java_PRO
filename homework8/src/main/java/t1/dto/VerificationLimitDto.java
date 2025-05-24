package t1.dto;

import java.util.UUID;

public record VerificationLimitDto(
        UUID operationId,
        Long userId,
        State state) {
}
