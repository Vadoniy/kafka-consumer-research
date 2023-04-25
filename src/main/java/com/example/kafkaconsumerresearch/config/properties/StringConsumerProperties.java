package com.example.kafkaconsumerresearch.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "kafka-consumer.string-consumer")
@Getter
@Setter
public class StringConsumerProperties implements ConsumerProperties {

    private String keyDeserializer;

    private String valueDeserializer;

    private String groupId;
}
