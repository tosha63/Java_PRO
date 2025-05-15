package t1.config;

import java.time.Duration;

public record RestTemplateProperties(
        String url,
        Duration connectTimeout,
        Duration readTimeout) {
}
