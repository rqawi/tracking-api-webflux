package com.example.tracking.controller;

import com.example.tracking.model.TrackingResponse;
import com.example.tracking.service.TrackingService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.time.ZonedDateTime;

import static org.mockito.ArgumentMatchers.*;

@WebFluxTest(TrackingController.class)
@Import(TrackingService.class)
class TrackingControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private TrackingService trackingService;

    @Test
    void getTrackingNumber_success() {
        TrackingResponse mockResponse = TrackingResponse.builder()
                .trackingNumber("ABC123XYZ")
                .generatedAt(ZonedDateTime.now())
                .build();

        Mockito.when(trackingService.generateTrackingNumber(
                        anyString(), anyString(), anyDouble(), anyString(), anyString(), anyString()))
                .thenReturn(Mono.just(mockResponse));

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/next-tracking-number")
                        .queryParam("origin_country_id", "US")
                        .queryParam("destination_country_id", "IN")
                        .queryParam("weight", 5.0)
                        .queryParam("customer_id", "C123")
                        .queryParam("customer_name", "ACME")
                        .queryParam("customer_slug", "acme-inc")
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.trackingNumber").isEqualTo("ABC123XYZ");
    }

    @Test
    void getTrackingNumber_failure() {
        Mockito.when(trackingService.generateTrackingNumber(
                        anyString(), anyString(), anyDouble(), anyString(), anyString(), anyString()))
                .thenReturn(Mono.error(new IllegalStateException("Duplicate")));

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/next-tracking-number")
                        .queryParam("origin_country_id", "US")
                        .queryParam("destination_country_id", "IN")
                        .queryParam("weight", 5.0)
                        .queryParam("customer_id", "C123")
                        .queryParam("customer_name", "ACME")
                        .queryParam("customer_slug", "acme-inc")
                        .build())
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.CONFLICT);
    }
}
