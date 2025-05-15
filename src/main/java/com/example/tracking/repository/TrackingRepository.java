package com.example.tracking.repository;

import com.example.tracking.model.TrackingRecord;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface TrackingRepository extends ReactiveMongoRepository<TrackingRecord, String> {
}
