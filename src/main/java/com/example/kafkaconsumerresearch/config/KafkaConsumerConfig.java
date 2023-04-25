package com.example.kafkaconsumerresearch.config;

import com.example.avro.TestAvroDto;
import com.example.kafkaconsumerresearch.config.properties.AvroKafkaConsumerProperties;
import com.example.kafkaconsumerresearch.config.properties.ConsumerProperties;
import com.example.kafkaconsumerresearch.config.properties.CustomConsumerProperties;
import com.example.kafkaconsumerresearch.config.properties.StringConsumerProperties;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.Map;
import java.util.Optional;

@Configuration
@RequiredArgsConstructor
public class KafkaConsumerConfig {

    private final AvroKafkaConsumerProperties avroKafkaConsumerProperties;
    private final CustomConsumerProperties customConsumerProperties;
    private final StringConsumerProperties stringConsumerProperties;
    private final KafkaProperties kafkaProperties;

    // 1. Consume Avro data from Kafka
    @Bean
    public ConsumerFactory<String, TestAvroDto> avroConsumerFactory() {
        final var props = createConsumerProperties(avroKafkaConsumerProperties);
        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, TestAvroDto> avroKafkaListenerContainerFactory() {
        final var factory = new ConcurrentKafkaListenerContainerFactory<String, TestAvroDto>();
        factory.setConsumerFactory(avroConsumerFactory());
        return factory;
    }

    // 2. Consume String objects from Kafka
    @Bean
    public ConsumerFactory<String, String> stringConsumerFactory() {
        final var props = createConsumerProperties(stringConsumerProperties);
        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> stringKafkaListenerContainerFactory() {
        final var factory = new ConcurrentKafkaListenerContainerFactory<String, String>();
        factory.setConsumerFactory(stringConsumerFactory());
        return factory;
    }

    // 3. Consume Custom objects from Kafka
    @Bean
    public ConsumerFactory<String, String> customConsumerFactory() {
        final var props = createConsumerProperties(customConsumerProperties);
        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> customKafkaListenerContainerFactory() {
        final var factory = new ConcurrentKafkaListenerContainerFactory<String, String>();
        factory.setConsumerFactory(customConsumerFactory());
        return factory;
    }

    private Map<String, Object> createConsumerProperties(ConsumerProperties consumerProperties) {
        final var props = kafkaProperties.buildConsumerProperties();
        props.put(ConsumerConfig.GROUP_ID_CONFIG, consumerProperties.getGroupId());
        try {
            props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
                    Class.forName(consumerProperties.getKeyDeserializer()));
            props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                    Class.forName(consumerProperties.getValueDeserializer()));
        } catch (ClassNotFoundException e) {
            final var errorMessage = String.format("""
                            Key or value deserializer class is not found from property class %s:
                            key-deserializer: %s
                            value-deserializer: %s""",
                    consumerProperties.getClass().getName(),
                    consumerProperties.getKeyDeserializer(),
                    consumerProperties.getValueDeserializer());
            throw new RuntimeException(errorMessage, e);
        }
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        Optional.ofNullable(consumerProperties.getProperties())
                .ifPresent(props::putAll);
        return props;
    }
}
