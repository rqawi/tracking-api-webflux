package com.example.tracking.service;

import com.example.tracking.model.TrackingResponse;
import com.example.tracking.util.TrackingNumberGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.ZonedDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class TrackingService {

    private final ReactiveStringRedisTemplate redisTemplate;

    public Mono<TrackingResponse> generateTrackingNumber(
            String origin, String destination, double weight, String customerId, String customerName, String customerSlug) {
        log.info("Generating tracking number for customerId={}, origin={}, destination={}, weight={}",
                customerId, origin, destination, weight);

        String trackingNumber = TrackingNumberGenerator.generate(origin, destination, customerSlug);
        String redisKey = "tracking:" + trackingNumber;
        String now = ZonedDateTime.now().toString();

        log.debug("Generated tracking number: {}, Redis key: {}", trackingNumber, redisKey);

        return redisTemplate.opsForValue().setIfAbsent(redisKey, now)
                .doOnNext(isNew -> log.debug("Redis setIfAbsent for key={} returned {}", redisKey, isNew))
                .flatMap(isNew -> {
                    if (Boolean.TRUE.equals(isNew)) {
                        log.info("Tracking number [{}] successfully stored in Redis", trackingNumber);
                        return Mono.just(TrackingResponse.builder()
                                .trackingNumber(trackingNumber)
                                .generatedAt(ZonedDateTime.parse(now))
                                .build());
                    } else {
                        log.warn("Duplicate tracking number [{}] detected", trackingNumber);
                        return Mono.error(new IllegalStateException("Duplicate tracking number generated, please retry."));
                    }
                })
                .doOnError(ex -> log.error("Error during tracking number generation", ex));
    }
}
