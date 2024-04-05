package edu.java.scrapper.controller.interceptor;

import edu.java.scrapper.controller.exceptions.TooManyRequestsException;
import edu.java.scrapper.controller.limiter.RateLimiter;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.web.servlet.HandlerInterceptor;

@RequiredArgsConstructor
public class RateLimiterInterceptor implements HandlerInterceptor {
    private final RateLimiter rateLimiter;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        Bucket bucket = rateLimiter.getBucket(request.getRemoteAddr());
        ConsumptionProbe probe = bucket.tryConsumeAndReturnRemaining(1);
        if (!probe.isConsumed()) {
            long secondsWaitForRefill = TimeUnit.SECONDS.convert(probe.getNanosToWaitForRefill(), TimeUnit.NANOSECONDS);
            throw new TooManyRequestsException("Wait %d seconds for refill".formatted(secondsWaitForRefill));
        }
        return true;
    }
}
