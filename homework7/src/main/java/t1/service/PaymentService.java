package t1.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import t1.dto.IntegrationProductResponseDto;
import t1.dto.ResponseDto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {
    private final RestTemplate paymentRestTemplate;


    public List<IntegrationProductResponseDto> getProducts(Long userId) {
        final var path = "/all/" + userId;
        List<IntegrationProductResponseDto> productsList = paymentRestTemplate.exchange(path,
                                                                                HttpMethod.GET,
                                                                                null,
                                                                                new ParameterizedTypeReference<List<IntegrationProductResponseDto>>() {
                                                                                })
                                                                      .getBody();
        log.info("Получен список продуктов по userId: {} {}", userId, productsList);
        return productsList;
    }

    public ResponseDto executePayment(Long productId,
                                      Long userId,
                                      BigDecimal sum) throws BadRequestException {
        final var pathGetProduct = "/" + productId + "/user/" + userId;
        IntegrationProductResponseDto product = paymentRestTemplate.getForObject(pathGetProduct, IntegrationProductResponseDto.class);
        log.info("Получен продукт {}", product);
        BigDecimal currentBalance = product.balance();
        String accountNumber = product.accountNumber();
        if (Objects.isNull(currentBalance) || currentBalance.compareTo(sum) < 0) {
            log.warn("Ошибка выполнения платежа для продукта со счетом: {}", accountNumber);
            throw new BadRequestException(String.format("Продукт со счетом: %s имеет баланс меньше переданной суммы", accountNumber));
        }
        IntegrationProductResponseDto updateProduct = product.toBuilder()
                                                             .balance(currentBalance.subtract(sum))
                                                             .build();

        final var pathUpdateProduct = "/" + productId;
        paymentRestTemplate.put(pathUpdateProduct, updateProduct);
        log.info("Платеж успешно выполнен для продукта со счетом: {}", accountNumber);
        return new ResponseDto("Платеж успешно выполнен");
    }
}
