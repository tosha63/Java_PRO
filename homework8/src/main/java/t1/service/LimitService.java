package t1.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import t1.dto.LimitRequestDto;
import t1.dto.LimitResponseDto;
import t1.dto.ReserveLimitDto;
import t1.dto.VerificationLimitDto;
import t1.entity.Limit;
import t1.entity.Operation;
import t1.exception.ExceedingLimitException;
import t1.exception.StateNotFoundException;
import t1.mappers.LimitMapper;
import t1.repository.LimitRepository;
import t1.repository.OperationRepository;

import java.math.BigDecimal;

import static t1.dto.State.SUCCESS;

@Service
@RequiredArgsConstructor
@Slf4j
public class LimitService {
    private final LimitRepository limitRepository;
    private final OperationRepository operationRepository;
    private final LimitMapper limitMapper;

    @Value("${limit.default-amount-limit}")
    private BigDecimal defaultAmountLimit;

    public LimitResponseDto updateLimit(Long id, LimitRequestDto updateLimitRequestDto) {
        final var entity = limitRepository.findById(id)
                                          .orElseThrow(() -> new EntityNotFoundException("Лимит не найден"));
        final var updateProduct = limitMapper.map(entity, updateLimitRequestDto);
        limitRepository.save(updateProduct);
        String messageInfo = String.format("Лимит с id %s обновлен до суммы %s", id, updateProduct.getAmountLimit());
        log.info(messageInfo);
        return getResponseDto(messageInfo);
    }

    @Transactional
    public LimitResponseDto updateLimits(LimitRequestDto updateLimitRequestDto) {
        limitRepository.updateLimit(updateLimitRequestDto.amountLimit());
        String messageInfo = String.format("Лимиты по всем пользователям обновлены до суммы %s", updateLimitRequestDto.amountLimit());
        log.info(messageInfo);
        return getResponseDto(messageInfo);
    }


    public LimitResponseDto reserveLimits(ReserveLimitDto reserveLimitDto) {
        Long userId = reserveLimitDto.userId();
        Limit limit = limitRepository.findByUserId(userId)
                                     .orElse(getDefaultLimit(userId));
        BigDecimal currentAmountLimit = limit.getAmountLimit();
        BigDecimal subAmountLimit = reserveLimitDto.amountPayment();
        if (currentAmountLimit.compareTo(subAmountLimit) < 0) {
            throw new ExceedingLimitException(String.format("Сумма платежа превышает сумму лимита на сумму %s", subAmountLimit.subtract(currentAmountLimit)));
        }
        limit.setAmountLimit(currentAmountLimit.subtract(subAmountLimit));
        operationRepository.save(new Operation(reserveLimitDto.operationId(), userId, subAmountLimit));
        limitRepository.save(limit);
        String messageInfo = String.format("Лимит по пользователю %s зарезервирован на сумму %s", userId, subAmountLimit);
        log.info(messageInfo);
        return getResponseDto(messageInfo);
    }

    public LimitResponseDto verificationLimits(VerificationLimitDto verificationLimitDto) {
        Long userId = verificationLimitDto.userId();
        Operation operation = operationRepository.findByOperationIdAndUserId(verificationLimitDto.operationId(), userId)
                                                 .orElseThrow(() -> new EntityNotFoundException("Операция с данным платежом не найдена"));
        Limit limit = limitRepository.findByUserId(userId)
                                     .orElseThrow(() -> new EntityNotFoundException("Лимит не найден"));
        BigDecimal currentAmountLimit = limit.getAmountLimit();
        switch (verificationLimitDto.state()) {
            case SUCCESS -> {
                String messageInfo = String.format("Платеж проведен успешно, оставшийся лимит %s", currentAmountLimit);
                log.info(messageInfo);
                return getResponseDto(messageInfo);
            }
            case FAILED -> {
                limit.setAmountLimit(currentAmountLimit.add(operation.getPayment()));
                limitRepository.save(limit);
                operationRepository.delete(operation);
                String messageInfo = String.format("Птатеж проведен с ошибкой, лимит восстановлен %s", limit.getAmountLimit());
                log.info(messageInfo);
                return getResponseDto(messageInfo);
            }
            default -> throw new StateNotFoundException();
        }
    }

    @Scheduled(cron = "${limit.scheduled.cron:0 0 0 * * *}")
    @Transactional
    public void updateLimitsCron() {
        limitRepository.updateLimit(defaultAmountLimit);
        log.info("Лимиты сброшены");
    }

    private LimitResponseDto getResponseDto(String messageInfo) {
        return LimitResponseDto.builder()
                               .state(SUCCESS)
                               .messageInfo(messageInfo)
                               .build();
    }

    private Limit getDefaultLimit(Long userId) {
        return new Limit(userId, defaultAmountLimit);
    }
}
