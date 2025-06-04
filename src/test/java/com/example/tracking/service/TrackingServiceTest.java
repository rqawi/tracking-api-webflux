package com.example.tracking.service;

import com.example.tracking.service.TrackingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.data.redis.core.ReactiveValueOperations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class TrackingServiceTest {

    private ReactiveStringRedisTemplate redisTemplate;
    private ReactiveValueOperations<String, String> valueOperations;
    private TrackingService trackingService;

    @BeforeEach
    void setup() {
        redisTemplate = mock(ReactiveStringRedisTemplate.class);
        valueOperations = mock(ReactiveValueOperations.class);

        when(redisTemplate.opsForValue()).thenReturn(valueOperations);

        trackingService = new TrackingService(redisTemplate);
    }

    @Test
    void generateTrackingNumber_shouldReturnTrackingResponse_whenUnique() {
        when(valueOperations.setIfAbsent(anyString(), anyString()))
                .thenReturn(Mono.just(true));

        StepVerifier.create(trackingService.generateTrackingNumber(
                        "NYC", "LAX", 5.5, "CUST123", "Customer Inc", "customer-inc"))
                .expectNextMatches(response -> response.getTrackingNumber() != null &&
                        response.getGeneratedAt() != null)
                .verifyComplete();

        verify(valueOperations, times(1)).setIfAbsent(anyString(), anyString());
    }

    @Test
    void generateTrackingNumber_shouldError_whenDuplicate() {
        when(valueOperations.setIfAbsent(anyString(), anyString()))
                .thenReturn(Mono.just(false));

        StepVerifier.create(trackingService.generateTrackingNumber(
                        "NYC", "LAX", 5.5, "CUST123", "Customer Inc", "customer-inc"))
                .expectErrorMatches(error -> error instanceof IllegalStateException &&
                        error.getMessage().contains("Duplicate tracking number"))
                .verify();

        verify(valueOperations, times(1)).setIfAbsent(anyString(), anyString());
    }
}
