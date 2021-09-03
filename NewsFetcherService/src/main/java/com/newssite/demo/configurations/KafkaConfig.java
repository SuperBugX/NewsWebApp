package com.newssite.demo.configurations;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import com.newssite.demo.resources.Article;

@Configuration
public class KafkaConfig {

	// Attributes
	private static Map<String, Object> producerProps;
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
		consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
		consumerProps.put(JsonDeserializer.TRUSTED_PACKAGES, "*");

		// Configure the producer properties for the microservice with Apache Kafka
		producerProps = new HashMap<String, Object>();
		producerProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
		producerProps.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, "zstd");
		producerProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		producerProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
	}

	// Getters
	public static Map<String, Object> getProducerProps() {
		return producerProps;
	}

	public static Map<String, Object> getConsumerProps() {
		return consumerProps;
	}

	// Methods
	@Bean
	public ProducerFactory<String, Article> producerFactory() {
		// Create a ProducerFactory instance to be dependency injected that will
		// be used to create Apache Kafka producers
		return new DefaultKafkaProducerFactory<>(producerProps);
	}

	@Bean
	public ConsumerFactory<String, Article> consumerFactory() {
		// Create a ConsumerFactory instance to be dependency injected that will
		// be used to create Apache Kafka listeners/consumers
		return new DefaultKafkaConsumerFactory<>(consumerProps);
	}

	@Bean
	public KafkaTemplate<String, Article> kafkaTemplate() {
		// Create a template to be dependency injected for pushing new
		// events into an Apache Kafka topic using the producer properties specified
		return new KafkaTemplate<>(producerFactory());
	}
}
