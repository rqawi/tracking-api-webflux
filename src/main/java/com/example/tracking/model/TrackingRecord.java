package com.example.tracking.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.ZonedDateTime;

@Data
@Builder
@Document(collection = "tracking_records")
public class TrackingRecord {
    @Id
    private String trackingNumber;
    private String originCountry;
    private String destinationCountry;
    private String customerId;
    private String customerName;
    private String customerSlug;
    private double weight;
    private ZonedDateTime generatedAt;
}
