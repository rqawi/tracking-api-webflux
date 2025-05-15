package com.example.tracking.config;

import com.example.tracking.model.TrackingRecord;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.support.serializer.JsonSerializer;
import reactor.kafka.sender.SenderOptions;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfig {

    @Bean
    public ReactiveKafkaProducerTemplate<String, TrackingRecord> reactiveKafkaProducerTemplate() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        props.put(JsonSerializer.ADD_TYPE_INFO_HEADERS, false);

        SenderOptions<String, TrackingRecord> senderOptions = SenderOptions.create(props);
        return new ReactiveKafkaProducerTemplate<>(senderOptions);
    }
}
