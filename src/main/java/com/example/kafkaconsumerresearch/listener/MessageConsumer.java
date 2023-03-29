package com.example.kafkaconsumerresearch.listener;

import com.example.kafkaconsumerresearch.dto.TestDto;
import com.example.avro.TestAvroDto;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class MessageConsumer {

    @KafkaListener(topics = {"mytopic"}, groupId = "default")
    public void getDefaultTopicMessage(GenericMessage<TestDto> message) {
        Optional.ofNullable(message)
                .map(GenericMessage::getPayload)
                .ifPresentOrElse(System.out::println, () -> System.out.println("Empty payload"));
    }

    @KafkaListener(topics = {"mytopic-avro"}, groupId = "default-avro")
    public void getDefaultTopicAvroMessage(ConsumerRecord<String, TestAvroDto> consumerRecord) {
        System.out.println(consumerRecord.value().getName());
    }
}
