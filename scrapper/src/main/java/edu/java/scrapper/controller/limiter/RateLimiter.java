package edu.java.scrapper.controller.limiter;

import edu.java.scrapper.configuration.ApplicationConfig;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RateLimiter {
    private final Map<String, Bucket> cache = new ConcurrentHashMap<>();
    private final Bandwidth bandwidth;

    public RateLimiter(ApplicationConfig applicationConfig) {
        int capacity = applicationConfig.rateLimit().capacity();
        int refillRate = applicationConfig.rateLimit().refillRate();
        Duration refillInterval = applicationConfig.rateLimit().refillInterval();
        int initialTokens = applicationConfig.rateLimit().initialTokens();
        bandwidth = Bandwidth.builder()
            .capacity(capacity)
            .refillIntervally(refillRate, refillInterval)
            .initialTokens(initialTokens)
            .build();
    }

    public Bucket getBucket(String ipAddress) {
        return cache.computeIfAbsent(ipAddress, this::newBucket);
    }

    private Bucket newBucket(String ipAddress) {
        return Bucket.builder()
            .addLimit(bandwidth)
            .build();
    }
}
