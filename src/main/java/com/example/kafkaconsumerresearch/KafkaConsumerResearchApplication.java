package com.example.kafkaconsumerresearch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableKafka
public class KafkaConsumerResearchApplication {

	public static void main(String[] args) {
		SpringApplication.run(KafkaConsumerResearchApplication.class, args);
	}

}
