package com.example.tracking;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class TrackingApiWebfluxApplication {

    public static void main(String[] args) {
        log.info("Starting Tracking API WebFlux Application...");
        SpringApplication.run(TrackingApiWebfluxApplication.class, args);
        log.info("Tracking API is up and running.");
    }
}