package t1.dto;

public record PaymentErrorResponseDto(
        String reason,
        String message) {
}
