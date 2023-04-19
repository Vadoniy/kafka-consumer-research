package com.example.kafkaconsumerresearch.config;

import com.example.avro.TestAvroDto;
import com.example.kafkaconsumerresearch.config.properties.AvroKafkaConsumerProperties;
import com.example.kafkaconsumerresearch.config.properties.CustomConsumerProperties;
import com.example.kafkaconsumerresearch.config.properties.StringConsumerProperties;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

@Configuration
public class KafkaConsumerConfig {

    private final AvroKafkaConsumerProperties avroKafkaConsumerProperties;
    private final CustomConsumerProperties customConsumerProperties;
    private final StringConsumerProperties stringConsumerProperties;
    private final KafkaProperties kafkaProperties;

    public KafkaConsumerConfig(AvroKafkaConsumerProperties avroKafkaConsumerProperties,
                               CustomConsumerProperties customConsumerProperties,
                               StringConsumerProperties stringConsumerProperties,
                               KafkaProperties kafkaProperties) {
        this.avroKafkaConsumerProperties = avroKafkaConsumerProperties;
        this.customConsumerProperties = customConsumerProperties;
        this.stringConsumerProperties = stringConsumerProperties;
        this.kafkaProperties = kafkaProperties;
    }

    // 1. Consume Avro data from Kafka

    @Bean
    public ConsumerFactory<String, TestAvroDto> avroConsumerFactory(KafkaProperties kafkaProperties,
                                                                    AvroKafkaConsumerProperties avroKafkaConsumerProperties) throws ClassNotFoundException {
        final var props = kafkaProperties.buildConsumerProperties();
        props.put(ConsumerConfig.GROUP_ID_CONFIG, avroKafkaConsumerProperties.getGroupId());
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
                Class.forName(avroKafkaConsumerProperties.getKeyDeserializer()));
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                Class.forName(avroKafkaConsumerProperties.getValueDeserializer()));
        props.putAll(avroKafkaConsumerProperties.getProperties());
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, TestAvroDto>
    avroKafkaListenerContainerFactory() throws ClassNotFoundException {
        final var factory = new ConcurrentKafkaListenerContainerFactory<String, TestAvroDto>();
        factory.setConsumerFactory(avroConsumerFactory(kafkaProperties, avroKafkaConsumerProperties));
        return factory;
    }

    // 2. Consume String objects from Kafka

    @Bean
    public ConsumerFactory<String, String> stringConsumerFactory(KafkaProperties kafkaProperties,
                                                               StringConsumerProperties stringConsumerProperties)
            throws ClassNotFoundException {
        final var props = kafkaProperties.buildConsumerProperties();
        props.put(ConsumerConfig.GROUP_ID_CONFIG, stringConsumerProperties.getGroupId());
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
                Class.forName(stringConsumerProperties.getKeyDeserializer()));
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                Class.forName(stringConsumerProperties.getValueDeserializer()));
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> stringKafkaListenerContainerFactory()
            throws ClassNotFoundException {
        final var factory = new ConcurrentKafkaListenerContainerFactory<String, String>();
        factory.setConsumerFactory(stringConsumerFactory(kafkaProperties, stringConsumerProperties));
        return factory;
    }

    // 3. Consume Custom objects from Kafka

    @Bean
    public ConsumerFactory<String, String> customConsumerFactory(KafkaProperties kafkaProperties,
                                                                 CustomConsumerProperties customConsumerProperties)
            throws ClassNotFoundException {
        final var props = kafkaProperties.buildConsumerProperties();
        props.put(ConsumerConfig.GROUP_ID_CONFIG, customConsumerProperties.getGroupId());
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
                Class.forName(customConsumerProperties.getKeyDeserializer()));
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                Class.forName(customConsumerProperties.getValueDeserializer()));
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> customKafkaListenerContainerFactory()
            throws ClassNotFoundException {
        final var factory = new ConcurrentKafkaListenerContainerFactory<String, String>();
        factory.setConsumerFactory(customConsumerFactory(kafkaProperties, customConsumerProperties));
        return factory;
    }
}
