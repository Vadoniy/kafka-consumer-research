package com.example.kafkaconsumerresearch.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "kafka-consumer.avro-consumer")
@Getter
@Setter
public class AvroKafkaConsumerProperties implements ConsumerProperties {

    private String keyDeserializer;

    private String valueDeserializer;

    private String groupId;

    private Map<String, String> properties;
}
