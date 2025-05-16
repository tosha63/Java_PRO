package t1.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;
import t1.dto.PaymentErrorResponseDto;
import t1.exception.IntegrationException;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class RestTemplateResponseErrorHandler implements ResponseErrorHandler {
    private final ObjectMapper mapper;
    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        HttpStatusCode statusCode = response.getStatusCode();
        return statusCode.is4xxClientError() || statusCode.is5xxServerError();
    }

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        if (response.getStatusCode().is4xxClientError()) {
            PaymentErrorResponseDto paymentErrorResponseDto = mapper.readValue(response.getBody(), PaymentErrorResponseDto.class);
            throw new IntegrationException(paymentErrorResponseDto.reason(), paymentErrorResponseDto.message());
        }
    }
}
