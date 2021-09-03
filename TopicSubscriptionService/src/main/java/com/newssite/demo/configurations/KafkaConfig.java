package com.newssite.demo.configurations;

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Configuration
public class KafkaConfig {

	// Attributes
	private static Map<String, Object> consumerProps;

	static {
		// Configure the consumers properties for the microservice with Apache Kafka
		consumerProps = new HashMap<String, Object>();
		consumerProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
		consumerProps.put(ConsumerConfig.GROUP_ID_CONFIG, "news_consumer");
		consumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
		consumerProps.put(ConsumerConfig.ALLOW_AUTO_CREATE_TOPICS_CONFIG, true);
		consumerProps.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
		consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
	}

	// Getters
	public static Map<String, Object> getConsumerProps() {
		return consumerProps;
	}

	// Methods
	@Bean
	public ConsumerFactory<String, String> consumerFactory() {
		// Create a ConsumerFactory instance to be dependency injected that will
		// be used to create Apache Kafka listeners/consumers
		return new DefaultKafkaConsumerFactory<>(consumerProps);
	}
}
