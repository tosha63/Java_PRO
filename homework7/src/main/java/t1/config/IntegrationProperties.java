package t1.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "integrations")
@RequiredArgsConstructor
@Getter
public class IntegrationProperties {
    private final RestTemplateProperties productServiceProperties;
}
