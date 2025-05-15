package t1.dto;

import lombok.Builder;

import java.math.BigDecimal;

@Builder(toBuilder = true)
public record IntegrationProductResponseDto(
        String accountNumber,
        BigDecimal balance) {
}
