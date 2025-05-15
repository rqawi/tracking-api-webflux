package com.example.tracking.model;

import lombok.Builder;
import lombok.Data;

import java.time.ZonedDateTime;

@Data
@Builder
public class TrackingResponse {
    private String trackingNumber;
    private ZonedDateTime generatedAt;
}
