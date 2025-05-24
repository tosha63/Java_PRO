package t1.dto;

import lombok.Builder;

@Builder(toBuilder = true)
public record LimitResponseDto(State state, String messageInfo) {
}
