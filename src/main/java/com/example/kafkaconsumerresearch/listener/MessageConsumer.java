package com.example.kafkaconsumerresearch.listener;

import com.example.avro.TestAvroDto;
import com.example.kafkaconsumerresearch.dto.TestDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Slf4j
public class MessageConsumer {

    @KafkaListener(
            topics = {"${kafka-topic-names.string}"},
            groupId = "${kafka-consumer.string-consumer.group-id}",
            containerFactory = "stringKafkaListenerContainerFactory")
    public void getStringMessage(GenericMessage<String> message) {
        Optional.ofNullable(message)
                .map(GenericMessage::getPayload)
                .ifPresentOrElse(
                        payload -> log.info("${kafka-topic-names.string}"
                                + "\n${kafka-consumer.string-consumer.group-id}"
                                + "\nstringKafkaListenerContainerFactory\n"
                                + payload),
                        () -> System.out.println("Empty payload"));
    }

    @KafkaListener(
            topics = {"${kafka-topic-names.avro}"},
            groupId = "${kafka-consumer.avro-consumer.group-id}",
            containerFactory = "avroKafkaListenerContainerFactory")
    public void getAvroMessage(ConsumerRecord<String, TestAvroDto> consumerRecord) {
        System.out.println(consumerRecord.value().getName());
    }

    @KafkaListener(
            topicPartitions
                    = @TopicPartition(topic = "${kafka-topic-names.custom}", partitions = {"0"}),
            groupId = "${kafka-consumer.custom-consumer.group-id}",
            containerFactory = "customKafkaListenerContainerFactory")
    public void getCustomMessage(@Header(KafkaHeaders.RECEIVED_PARTITION) String partitionId,
                                 @Header(KafkaHeaders.OFFSET) String offset,
                                 GenericMessage<TestDto> message) {
        Optional.ofNullable(message)
                .map(GenericMessage::getPayload)
                .ifPresentOrElse(
                        payload -> log.info("Partition0: "
                                + partitionId
                                + "\n"
                                + "Offsett0: "
                                + offset
                                + "\n"
                                + payload),
                        () -> System.out.println("Empty payload"));
    }

    @KafkaListener(
            topicPartitions
                    = @TopicPartition(topic = "${kafka-topic-names.custom}", partitions = {"1"}),
            groupId = "${kafka-consumer.custom-consumer.group-id}",
            containerFactory = "customKafkaListenerContainerFactory")
    public void getCustomMessage1(@Header(KafkaHeaders.RECEIVED_PARTITION) String partitionId,
                                  @Header(KafkaHeaders.OFFSET) String offset,
                                  GenericMessage<TestDto> message) {
        Optional.ofNullable(message)
                .map(GenericMessage::getPayload)
                .ifPresentOrElse(
                        payload -> log.info("Partition1: "
                                + partitionId
                                + "\n"
                                + "Offsett0: "
                                + offset
                                + "\n"
                                + payload),
                        () -> System.out.println("Empty payload"));
    }
}
