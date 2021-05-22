package com.newssite.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.ListTopicsResult;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.AcknowledgingMessageListener;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.listener.ContainerProperties.AckMode;

import com.newssite.demo.configurations.KafkaConfig;

public final class KafkaConsumerUtil {

	// Contains all active or inactive consumer (listener) instances for Apache
	// Kafka
	private static Map<String, ConcurrentMessageListenerContainer<String, String>> consumersMap = new HashMap<>();

	/*
	 * Method creates or resumes any consumers when provided with the following:
	 * topic: Used to resume or create a new consumer for the following Kafka topic.
	 * 
	 * messageListener: A AcknowledgingMessageListener interface implementing object
	 * in order to process each Kafka event as desired with manual or automatic
	 * commits.
	 * 
	 * concurrency: The number of consumers (listener) instances that should be
	 * created.
	 * 
	 * consumerProperties: The Apache Kafka properties that should be set for the
	 * new consumer.
	 */
	public static void startOrCreateConsumers(final String topic,
			final AcknowledgingMessageListener<String, String> messageListener, final int concurrency,
			final Map<String, Object> consumerProperties) {

		ConcurrentMessageListenerContainer<String, String> container = consumersMap.get(topic);
		if (container != null) {
			if (!container.isRunning()) {
				container.start();
			}
			return;
		}

		ContainerProperties containerProps = new ContainerProperties(topic);

		Boolean enableAutoCommit = (Boolean) consumerProperties.get(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG);

		if (enableAutoCommit != null && !enableAutoCommit) {
			containerProps.setAckMode(AckMode.MANUAL_IMMEDIATE);
		}

		containerProps.setPollTimeout(100);
		ConsumerFactory<String, String> factory = new DefaultKafkaConsumerFactory<>(consumerProperties);

		container = new ConcurrentMessageListenerContainer<>(factory, containerProps);
		container.setupMessageListener(messageListener);

		if (concurrency == 0) {
			container.setConcurrency(1);
		} else {
			container.setConcurrency(concurrency);
		}

		container.start();

		consumersMap.put(topic, container);
	}

	/*
	 * Method creates or resumes any consumers when provided with the following:
	 * topic: Used to resume or create a new consumer for the following Kafka topic.
	 * 
	 * messageListener: A MessageListener interface implementing object in order to
	 * process each Kafka event as desired with automatic commits.
	 * 
	 * concurrency: The number of consumers (listener) instances that should be
	 * created.
	 * 
	 * consumerProperties: The Apache Kafka properties that should be set for the
	 * new consumer.
	 */
	public static void startOrCreateConsumers(final String topic, final MessageListener<String, String> messageListener,
			final int concurrency, final Map<String, Object> consumerProperties) {

		ConcurrentMessageListenerContainer<String, String> container = consumersMap.get(topic);
		if (container != null) {
			if (!container.isRunning()) {
				container.start();
			}
			return;
		}

		ContainerProperties containerProps = new ContainerProperties(topic);

		Boolean enableAutoCommit = (Boolean) consumerProperties.get(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG);

		if (enableAutoCommit != null && enableAutoCommit) {
			containerProps.setAckMode(AckMode.RECORD);
		}

		containerProps.setPollTimeout(100);
		ConsumerFactory<String, String> factory = new DefaultKafkaConsumerFactory<>(consumerProperties);

		container = new ConcurrentMessageListenerContainer<>(factory, containerProps);
		container.setupMessageListener(messageListener);

		if (concurrency == 0) {
			container.setConcurrency(1);
		} else {
			container.setConcurrency(concurrency);
		}

		container.start();

		consumersMap.put(topic, container);
	}

	// Method pauses a consumer group from processing events from Apache Kafka based
	// on the provided topic name
	public static void pauseConsumer(String topic) {
		ConcurrentMessageListenerContainer<String, String> container = consumersMap.get(topic);
		container.stop();
	}

	// Method deletes a consumer group based on the provided topic name
	public static void removeConsumer(String topic) {
		ConcurrentMessageListenerContainer<String, String> container = consumersMap.get(topic);
		container.stop();
		consumersMap.put(topic, null);
	}

	// Method checks if a topic exists and is in use in Apache Kafka
	public static boolean topicExists(String topic) {
		AdminClient admin = AdminClient.create(KafkaConfig.getConsumerProps());
		ListTopicsResult listTopics = admin.listTopics();

		try {
			Set<String> names = listTopics.names().get();
			return names.contains(topic);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return false;
	}
}
