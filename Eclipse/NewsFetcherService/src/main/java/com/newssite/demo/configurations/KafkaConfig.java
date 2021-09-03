package com.newssite.demo.configurations;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Qualifier;
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
	private static Map<String, Object> genericConsumerProps;
	private static Map<String, Object> articleProducerProps;
	private static Map<String, Object> articleConsumerProps;

	static {

		// Configure the consumers properties for the microservice with Apache Kafka
		genericConsumerProps = new HashMap<String, Object>();
		genericConsumerProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
		genericConsumerProps.put(ConsumerConfig.GROUP_ID_CONFIG, "generic_consumer");
		genericConsumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
		genericConsumerProps.put(ConsumerConfig.ALLOW_AUTO_CREATE_TOPICS_CONFIG, false);
		genericConsumerProps.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
		genericConsumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		genericConsumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

		// Configure the consumers properties for the microservice with Apache Kafka
		articleConsumerProps = new HashMap<String, Object>();
		articleConsumerProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
		articleConsumerProps.put(ConsumerConfig.GROUP_ID_CONFIG, "news_consumer");
		articleConsumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
		articleConsumerProps.put(ConsumerConfig.ALLOW_AUTO_CREATE_TOPICS_CONFIG, true);
		articleConsumerProps.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
		articleConsumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		articleConsumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
		articleConsumerProps.put(JsonDeserializer.TRUSTED_PACKAGES, "*");

		// Configure the producer properties for the microservice with Apache Kafka
		articleProducerProps = new HashMap<String, Object>();
		articleProducerProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
		articleProducerProps.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, "zstd");
		articleProducerProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		articleProducerProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
	}

	// Getters
	public static Map<String, Object> getGenericConsumerProps() {
		return genericConsumerProps;
	}

	public static Map<String, Object> getArticleProducerProps() {
		return articleProducerProps;
	}

	public static Map<String, Object> getArticleConsumerProps() {
		return articleConsumerProps;
	}

	// Methods
	@Bean
	public ConsumerFactory<String, Article> genericConsumerFactory() {
		// Create a ConsumerFactory instance to be dependency injected that will
		// be used to create Apache Kafka listeners/consumers
		return new DefaultKafkaConsumerFactory<>(genericConsumerProps);
	}

	@Bean
	public ProducerFactory<String, Article> articleProducerFactory() {
		// Create a ProducerFactory instance to be dependency injected that will
		// be used to create Apache Kafka producers
		return new DefaultKafkaProducerFactory<>(articleProducerProps);
	}

	@Bean
	public ConsumerFactory<String, Article> articleConsumerFactory() {
		// Create a ConsumerFactory instance to be dependency injected that will
		// be used to create Apache Kafka listeners/consumers
		return new DefaultKafkaConsumerFactory<>(articleConsumerProps);
	}

	@Bean
	@Qualifier("kafkaArticleTemplate")
	public KafkaTemplate<String, Article> kafkaArticleTemplate() {
		// Create a template to be dependency injected for pushing new
		// events into an Apache Kafka topic using the producer properties specified
		return new KafkaTemplate<>(articleProducerFactory());
	}
}
