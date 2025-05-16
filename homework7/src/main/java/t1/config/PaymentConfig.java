package t1.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableConfigurationProperties(IntegrationProperties.class)
@RequiredArgsConstructor
public class PaymentConfig {

    private final IntegrationProperties properties;

    @Bean
    public RestTemplate paymentRestTemplate(RestTemplateResponseErrorHandler errorHandler) {
        RestTemplateProperties productServiceProperties = properties.getProductServiceProperties();
        return new RestTemplateBuilder()
                .rootUri(productServiceProperties.url())
                .setConnectTimeout(productServiceProperties.connectTimeout())
                .setReadTimeout(productServiceProperties.readTimeout())
                .errorHandler(errorHandler)
                .build();
    }
}
