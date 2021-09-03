package com.newssite.demo.configurations;

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Configuration
public class KafkaConfig {

	// Attributes
	private static Map<String, Object> genericConsumerProps;
	private static Map<String, Object> articleConsumerProps;
	private static Map<String, Object> eventProducerProps;
	private static Map<String, Object> eventConsumerProps;

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
		articleConsumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

		// Configure the producer properties for the microservice with Apache Kafka
		eventProducerProps = new HashMap<String, Object>();
		eventProducerProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
		eventProducerProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		eventProducerProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		
		// Configure the consumers properties for the microservice with Apache Kafka
		eventConsumerProps = new HashMap<String, Object>();
		eventConsumerProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
		eventConsumerProps.put(ConsumerConfig.GROUP_ID_CONFIG, "event_consumer");
		eventConsumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
		eventConsumerProps.put(ConsumerConfig.ALLOW_AUTO_CREATE_TOPICS_CONFIG, false);
		eventConsumerProps.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
		eventConsumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		eventConsumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
	}

	// Getters
	public static Map<String, Object> getGenericConsumerProps() {
		return genericConsumerProps;
	}
	
	public static Map<String, Object> getArticleConsumerProps() {
		return articleConsumerProps;
	}


	public static Map<String, Object> getEventProducerProps() {
		return eventProducerProps;
	}
	
	public static Map<String, Object> getEventConsumerProps() {
		return eventConsumerProps;
	}

	// Methods
	@Bean
	public ConsumerFactory<String, String> genericConsumerFactory() {
		// Create a ConsumerFactory instance to be dependency injected that will
		// be used to create Apache Kafka listeners/consumers
		return new DefaultKafkaConsumerFactory<>(genericConsumerProps);
	}
	
	@Bean
	public ConsumerFactory<String, String> articleConsumerFactory() {
		// Create a ConsumerFactory instance to be dependency injected that will
		// be used to create Apache Kafka listeners/consumers
		return new DefaultKafkaConsumerFactory<>(articleConsumerProps);
	}
	
	@Bean
	public ConsumerFactory<String, String> eventConsumerFactory() {
		// Create a ConsumerFactory instance to be dependency injected that will
		// be used to create Apache Kafka listeners/consumers
		return new DefaultKafkaConsumerFactory<>(eventConsumerProps);
	}

	@Bean
	public ProducerFactory<String, String> eventProducerFactory() {
		// Create a ProducerFactory instance to be dependency injected that will
		// be used to create Apache Kafka producers
		return new DefaultKafkaProducerFactory<>(eventProducerProps);
	}

	@Bean
	@Qualifier("kafkaEventTemplate")
	public KafkaTemplate<String, String> kafkaEventTemplate() {
		// Create a template to be dependency injected for pushing new
		// events into an Apache Kafka topic using the producer properties specified
		return new KafkaTemplate<>(eventProducerFactory());
	}
}
