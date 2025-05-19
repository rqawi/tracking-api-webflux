package com.example.tracking.controller;

import com.example.tracking.model.TrackingResponse;
import com.example.tracking.service.TrackingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/next-tracking-number")
@RequiredArgsConstructor
@Slf4j
public class TrackingController {

    private final TrackingService trackingService;

    @GetMapping
    public Mono<TrackingResponse> getTrackingNumber(
            @RequestParam String origin_country_id,
            @RequestParam String destination_country_id,
            @RequestParam double weight,
            @RequestParam String customer_id,
            @RequestParam String customer_name,
            @RequestParam String customer_slug) {

        log.info("Received request to generate tracking number with origin={}, destination={}, weight={}, customerId={}, customerName={}, customerSlug={}",
                origin_country_id, destination_country_id, weight, customer_id, customer_name, customer_slug);

        return trackingService.generateTrackingNumber(
                        origin_country_id,
                        destination_country_id,
                        weight,
                        customer_id,
                        customer_name,
                        customer_slug)
                .doOnSuccess(response -> log.info("Generated tracking number: {}", response.getTrackingNumber()))
                .doOnError(error -> log.error("Failed to generate tracking number", error));
    }
}
