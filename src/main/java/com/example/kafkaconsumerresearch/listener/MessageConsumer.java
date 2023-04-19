package com.example.kafkaconsumerresearch.listener;

import com.example.avro.TestAvroDto;
import com.example.kafkaconsumerresearch.dto.TestDto;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class MessageConsumer {

    @KafkaListener(
            topics = {"${kafka-topic-names.string}"},
            groupId = "${kafka-consumer.string-consumer.group-id}",
            containerFactory = "stringKafkaListenerContainerFactory")
    public void getStringMessage(GenericMessage<String> message) {
        Optional.ofNullable(message)
                .map(GenericMessage::getPayload)
                .ifPresentOrElse(System.out::println, () -> System.out.println("Empty payload"));
    }

    @KafkaListener(
            topics = {"${kafka-topic-names.avro}"},
            groupId = "${kafka-consumer.avro-consumer.group-id}",
            containerFactory = "avroKafkaListenerContainerFactory")
    public void getAvroMessage(ConsumerRecord<String, TestAvroDto> consumerRecord) {
        System.out.println(consumerRecord.value().getName());
    }

    @KafkaListener(
            topics = {"${kafka-topic-names.custom}"},
            groupId = "${kafka-consumer.custom-consumer.group-id}",
            containerFactory = "customKafkaListenerContainerFactory")
    public void getCustomMessage(GenericMessage<TestDto> message) {
        Optional.ofNullable(message)
                .map(GenericMessage::getPayload)
                .ifPresentOrElse(System.out::println, () -> System.out.println("Empty payload"));
    }
}
