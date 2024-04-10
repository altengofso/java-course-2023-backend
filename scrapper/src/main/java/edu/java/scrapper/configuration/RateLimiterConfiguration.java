package edu.java.scrapper.configuration;

import edu.java.scrapper.controller.interceptor.RateLimiterInterceptor;
import edu.java.scrapper.controller.limiter.RateLimiter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@ConditionalOnProperty(prefix = "app.rate-limit", name = "enabled", havingValue = "true")
@RequiredArgsConstructor
public class RateLimiterConfiguration implements WebMvcConfigurer {
    private final ApplicationConfig applicationConfig;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        RateLimiter rateLimiter = new RateLimiter(applicationConfig);
        RateLimiterInterceptor rateLimiterInterceptor = new RateLimiterInterceptor(rateLimiter);
        registry.addInterceptor(rateLimiterInterceptor);
    }
}
