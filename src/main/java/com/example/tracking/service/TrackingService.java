package com.example.tracking.service;

import com.example.tracking.model.TrackingResponse;
import com.example.tracking.util.TrackingNumberGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.ZonedDateTime;

@Service
@RequiredArgsConstructor
public class TrackingService {

    private final ReactiveStringRedisTemplate redisTemplate;

    public Mono<TrackingResponse> generateTrackingNumber(
            String origin, String destination, double weight, String customerId, String customerName, String customerSlug) {

        String trackingNumber = TrackingNumberGenerator.generate(origin, destination, customerSlug);
        String redisKey = "tracking:" + trackingNumber;

        return redisTemplate.opsForValue().setIfAbsent(redisKey, ZonedDateTime.now().toString())
                .flatMap(isNew -> {
                    if (Boolean.TRUE.equals(isNew)) {
                        return Mono.just(TrackingResponse.builder()
                                .trackingNumber(trackingNumber)
                                .generatedAt(ZonedDateTime.now())
                                .build());
                    } else {
                        return Mono.error(new IllegalStateException("Duplicate tracking number generated, please retry."));
                    }
                });
    }
}
