package t1.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import t1.dto.LimitRequestDto;
import t1.dto.LimitResponseDto;
import t1.dto.ReserveLimitDto;
import t1.dto.VerificationLimitDto;
import t1.service.LimitService;

@RestController
@RequestMapping("/v1/api/limits")
@RequiredArgsConstructor
public class LimitController {
    private final LimitService limitService;

    @PutMapping("{id}")
    public LimitResponseDto updateLimit(@PathVariable("id") Long id, @RequestBody LimitRequestDto limitRequestDto) {
        return limitService.updateLimit(id, limitRequestDto);
    }

    @PutMapping
    public LimitResponseDto updateLimits(@RequestBody LimitRequestDto limitRequestDto) {
        return limitService.updateLimits(limitRequestDto);
    }

    @PostMapping("/reserve")
    public LimitResponseDto reserveLimits(@RequestBody ReserveLimitDto reserveLimitDto) {
        return limitService.reserveLimits(reserveLimitDto);
    }

    @PostMapping("/verification")
    public LimitResponseDto verificationLimits(@RequestBody VerificationLimitDto verificationLimitDto) {
        return limitService.verificationLimits(verificationLimitDto);
    }
}
