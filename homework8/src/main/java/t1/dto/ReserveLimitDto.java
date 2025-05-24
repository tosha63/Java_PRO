package t1.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record ReserveLimitDto(
        UUID operationId,
        BigDecimal amountPayment,
        Long userId) {
}
