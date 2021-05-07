package com.newssite.demo.util;

import java.util.HashMap;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.ListTopicsResult;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.ContainerProperties.AckMode;

import com.newssite.demo.configurations.KafkaConfig;
import com.newssite.demo.consumers.TopicAckMessageListener;

public final class KafkaConsumerUtil {

	private static Map<String, ConcurrentMessageListenerContainer<String, String>> consumersMap = new HashMap<>();

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void startOrCreateConsumers(final String topic, final Object messageListener, final int concurrency,
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

		if ((enableAutoCommit != null && !enableAutoCommit) && !(messageListener instanceof TopicAckMessageListener)) {
			throw new IllegalArgumentException("Expected message listener of type TopicAckMessageListener");
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

	public static void pauseConsumer(final String topic) {
		ConcurrentMessageListenerContainer<String, String> container = consumersMap.get(topic);
		container.stop();
	}

	public static void removeConsumer(final String topic) {
		ConcurrentMessageListenerContainer<String, String> container = consumersMap.get(topic);
		container.stop();
		consumersMap.remove(topic);
	}

	public static void createTemporaryConsumer(final String topic, final Object messageListener,
			final Map<String, Object> consumerProperties, final int delay) {

		ContainerProperties containerProps = new ContainerProperties(topic);

		Boolean enableAutoCommit = (Boolean) consumerProperties.get(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG);

		if (enableAutoCommit != null && !enableAutoCommit) {
			containerProps.setAckMode(AckMode.MANUAL_IMMEDIATE);
		}

		if ((enableAutoCommit != null && !enableAutoCommit) && !(messageListener instanceof TopicAckMessageListener)) {
			throw new IllegalArgumentException("Expected message listener of type TopicAckMessageListener");
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

	public static boolean topicExists(String topic) {
		AdminClient admin = AdminClient.create(KafkaConfig.getConsumerConfig());
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
	
	private KafkaConsumerUtil() {
		throw new UnsupportedOperationException("Can not instantiate KafkaConsumerUtil");
	}
}
