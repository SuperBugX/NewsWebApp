package com.example.demo.controllers;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;
import org.springframework.web.socket.messaging.SessionUnsubscribeEvent;

import com.example.demo.configurations.KafkaConfig;
import com.example.demo.consumers.CustomMessageListener;
import com.example.demo.consumers.KafkaConsumerUtil;
import com.example.demo.resources.ClientSession;
import com.example.demo.resources.NewsTopicProcessor;
import com.example.demo.resources.TopicSubscription;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Controller
public class WebSocketController {

	@Autowired
	private SimpMessagingTemplate simpMessagingTemplate;

	private Map<String, Object> consumerConfig = KafkaConfig.getConsumerConfig();
	private Map<String, TopicSubscription> activeTopics = new HashMap<String, TopicSubscription>(15);
	private Map<String, ClientSession> activeSessions = new HashMap<String, ClientSession>(50);

	@EventListener
	public void onApplicationEvent(SessionSubscribeEvent event) {

		Map<String, Object> nativeHeaders = (Map<String, Object>) event.getMessage().getHeaders().get("nativeHeaders");
		String subscriptionDestination = ((ArrayList<String>) nativeHeaders.get("destination")).get(0);

		if (subscriptionDestination != null) {

			String topic = subscriptionDestination.replaceFirst("/topic/", "");
			Map<String, String> sessionAttributes = (ConcurrentHashMap<String, String>) event.getMessage().getHeaders()
					.get("simpSessionAttributes");
			String sessionID = sessionAttributes.get("sessionId");
			ClientSession sessionEntry = (ClientSession) activeSessions.get(sessionID);
			boolean isSubscribed = false;

			if (sessionEntry != null) {
				if (!sessionEntry.getSubscriptions().contains(topic)) {
					sessionEntry.addSubscription(topic);
				} else {
					isSubscribed = true;
				}
			} else {
				activeSessions.put(sessionID,
						new ClientSession(sessionID, new LinkedList<String>(Arrays.asList(topic))));
			}

			if (!isSubscribed) {
				TopicSubscription topicEntry = (TopicSubscription) activeTopics.get(topic);
				if (topicEntry != null) {
					topicEntry.incrementSubscriptions();
				} else {

					activeTopics.put(topic, new TopicSubscription(topic));

					// start the consumer
					KafkaConsumerUtil.startOrCreateConsumers(topic,
							new CustomMessageListener(new NewsTopicProcessor(topic, simpMessagingTemplate)), 1,
							consumerConfig);
				}
			}
		}
	}

	@EventListener
	public void onApplicationEvent(SessionDisconnectEvent event) {

		Map<String, String> attributes = (ConcurrentHashMap<String, String>) event.getMessage().getHeaders()
				.get("simpSessionAttributes");
		String sessionID = attributes.get("sessionId");

		if (sessionID != null) {
			ClientSession session = activeSessions.get(sessionID);
			if (session != null) {

				List<String> subscriptions = session.getSubscriptions();

				for (String subscription : subscriptions) {
					TopicSubscription topic = activeTopics.get(subscription);
					if (topic != null) {
						topic.decrementSubscriptions();
						if (topic.getSubscriptions() <= 0) {
							activeTopics.remove(subscription);
							KafkaConsumerUtil.stopConsumer(subscription);
						}
					}
				}

				activeSessions.remove(sessionID);
			}
		}
	}

	@EventListener
	public void onApplicationEvent(SessionUnsubscribeEvent event) {

		Map<String, String> attributes = (ConcurrentHashMap<String, String>) event.getMessage().getHeaders()
				.get("simpSessionAttributes");
		String sessionID = attributes.get("sessionId");
		String subscription = event.getMessage().getHeaders().get("simpSubscriptionId").toString()
				.replaceFirst("/topic/", "");
		ClientSession sessionEntry = (ClientSession) activeSessions.get(sessionID);

		if (sessionEntry != null) {
			if (sessionEntry.removeSubscription(subscription)) {

				TopicSubscription topicEntry = (TopicSubscription) activeTopics.get(subscription);

				if (topicEntry != null) {
					topicEntry.decrementSubscriptions();
					if (topicEntry.getSubscriptions() <= 0) {
						activeTopics.remove(subscription);
						KafkaConsumerUtil.stopConsumer(subscription);
					}
				}
			}
		}
	}
}
