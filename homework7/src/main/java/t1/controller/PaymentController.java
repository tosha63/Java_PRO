package t1.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import t1.dto.IntegrationProductResponseDto;
import t1.dto.ResponseDto;
import t1.service.PaymentService;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/v1/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @GetMapping("/products/{userId}")
    public List<IntegrationProductResponseDto> getProducts(@PathVariable Long userId) {
        return paymentService.getProducts(userId);
    }

    @GetMapping("/execute")
    public ResponseDto executePayment(@RequestParam("productId") Long productId,
                                      @RequestParam("userId") Long userId,
                                      @RequestParam("sum") BigDecimal sum) throws Exception {
        return paymentService.executePayment(productId, userId, sum);
    }
}
