package com.platform.billprocessing.service;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class RateLimitingService {

    private final Map<String, Bucket> tenantBuckets = new ConcurrentHashMap<>();

    @Value("${app.rate-limiting.capacity:60}")
    private long capacity;

    @Value("${app.rate-limiting.tokens-refill:60}")
    private long tokensRefill;

    @Value("${app.rate-limiting.duration-seconds:60}")
    private long durationSeconds;

    @Value("${app.rate-limiting.enabled:true}")
    private boolean enabled;

    public boolean tryConsume(Long tenantId) {
        if (!enabled || tenantId == null) {
            return true;
        }
        String key = "tenant_" + tenantId;
        Bucket bucket = tenantBuckets.computeIfAbsent(key, k -> createNewBucket());
        return bucket.tryConsume(1);
    }

    private Bucket createNewBucket() {
        Bandwidth limit = Bandwidth.classic(capacity, Refill.intervally(tokensRefill, Duration.ofSeconds(durationSeconds)));
        return Bucket.builder()
                .addLimit(limit)
                .build();
    }
}

