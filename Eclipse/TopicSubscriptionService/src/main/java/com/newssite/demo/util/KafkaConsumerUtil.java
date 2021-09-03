package com.newssite.demo.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

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
import com.newssite.demo.consumers.TopicAckMessageListener;
import com.newssite.demo.resources.TopicReaderProcessor;

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
		if (container != null) {
			container.stop();
		}
	}

	// Method deletes a consumer group based on the provided topic name
	public static void removeConsumer(String topic) {
		ConcurrentMessageListenerContainer<String, String> container = consumersMap.get(topic);
		if (container != null) {
			container.stop();
			consumersMap.put(topic, null);
		}
	}

	//Create a new Kafka topic consumer for a certain amount of time in the range of milliseconds
	public static void createTemporaryConsumer(final String topic,
			final AcknowledgingMessageListener<String, String> messageListener,
			final Map<String, Object> consumerProperties, final int delay) {

		ContainerProperties containerProps = new ContainerProperties(topic);

		Boolean enableAutoCommit = (Boolean) consumerProperties.get(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG);

		if (enableAutoCommit != null && !enableAutoCommit) {
			containerProps.setAckMode(AckMode.MANUAL_IMMEDIATE);
		}

		containerProps.setPollTimeout(100);
		containerProps.setGroupId(UUID.randomUUID().toString());
		ConsumerFactory<String, String> factory = new DefaultKafkaConsumerFactory<>(consumerProperties);
		ConcurrentMessageListenerContainer<String, String> container = new ConcurrentMessageListenerContainer<>(factory,
				containerProps);

		container.setupMessageListener(messageListener);
		container.setConcurrency(1);
		container.start();

		new java.util.Timer().schedule(new java.util.TimerTask() {
			@Override
			public void run() {
				container.stop();
			}
		}, delay);
	}

	public static void createTemporaryConsumer(final String topic,
			final MessageListener<String, String> messageListener, final Map<String, Object> consumerProperties,
			final int delay) {

		ContainerProperties containerProps = new ContainerProperties(topic);

		Boolean enableAutoCommit = (Boolean) consumerProperties.get(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG);

		if (enableAutoCommit != null && !enableAutoCommit) {
			containerProps.setAckMode(AckMode.MANUAL_IMMEDIATE);
		}

		containerProps.setPollTimeout(100);
		containerProps.setGroupId(UUID.randomUUID().toString());
		ConsumerFactory<String, String> factory = new DefaultKafkaConsumerFactory<>(consumerProperties);
		ConcurrentMessageListenerContainer<String, String> container = new ConcurrentMessageListenerContainer<>(factory,
				containerProps);

		container.setupMessageListener(messageListener);
		container.setConcurrency(1);
		container.start();

		new java.util.Timer().schedule(new java.util.TimerTask() {
			@Override
			public void run() {
				container.stop();
			}
		}, delay);
	}

	// Method returns a list of articles from Apache Kafka when provided with a
	// Kafka topic
	// Execution is blocked for at least 3 seconds
	public static <T> List<T> getAllFromTopic(Class<T> clazz, String kafkaTopic) {

		TopicReaderProcessor<T> articleTopicReader = new TopicReaderProcessor<T>(clazz);
		KafkaConsumerUtil.startOrCreateConsumers(kafkaTopic, new TopicAckMessageListener(articleTopicReader, false), 1,
				KafkaConfig.getGenericConsumerProps());

		try {
			TimeUnit.SECONDS.sleep(3);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		KafkaConsumerUtil.removeConsumer(kafkaTopic);
		return articleTopicReader.getEvents();
	}

	// Method checks if a topic exists and is in use in Apache Kafka
	public static boolean articleTopicExists(String topic) {
		AdminClient admin = AdminClient.create(KafkaConfig.getArticleConsumerProps());
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
