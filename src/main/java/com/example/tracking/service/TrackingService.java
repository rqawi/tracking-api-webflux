package com.example.tracking.service;

import com.example.tracking.model.TrackingRecord;
import com.example.tracking.model.TrackingResponse;
import com.example.tracking.repository.TrackingRepository;
import com.example.tracking.util.TrackingNumberGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.ZonedDateTime;

@Service
@RequiredArgsConstructor
public class TrackingService {

    private final TrackingRepository repository;
    private final ReactiveKafkaProducerTemplate<String, TrackingRecord> kafkaProducer;

    public Mono<TrackingResponse> generateTrackingNumber(
            String origin, String destination, double weight, String customerId, String customerName, String customerSlug) {

        String trackingNumber = TrackingNumberGenerator.generate(origin, destination, customerSlug);
        ZonedDateTime now = ZonedDateTime.now();

        TrackingRecord record = TrackingRecord.builder()
                .trackingNumber(trackingNumber)
                .originCountry(origin)
                .destinationCountry(destination)
                .customerId(customerId)
                .customerName(customerName)
                .customerSlug(customerSlug)
                .weight(weight)
                .generatedAt(now)
                .build();

        return repository.save(record)
                .doOnSuccess(r -> kafkaProducer.send("tracking-events", trackingNumber, record).subscribe())
                .map(r -> TrackingResponse.builder()
                        .trackingNumber(r.getTrackingNumber())
                        .generatedAt(r.getGeneratedAt())
                        .build());
    }
}
