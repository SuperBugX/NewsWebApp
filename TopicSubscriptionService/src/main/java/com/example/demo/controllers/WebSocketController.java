package com.example.demo.controllers;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionUnsubscribeEvent;

import com.example.demo.resources.ClientSession;
import com.example.demo.resources.TopicSubscription;
import org.springframework.context.event.EventListener;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Controller
public class WebSocketController {

	private Map<String, TopicSubscription> topics = new HashMap<String, TopicSubscription>(15);
	private Map<String, ClientSession> sessions = new HashMap<String, ClientSession>(50);

	@SubscribeMapping("/subscribe/{topic}")
	public void subscribeToTopic(@DestinationVariable String topic, SimpMessageHeaderAccessor headerAccessor) {

		String sessionID = headerAccessor.getSessionAttributes().get("sessionId").toString();
		ClientSession sessionEntry = (ClientSession) sessions.get(sessionID);
		boolean isSubscribed = false;

		if (sessionEntry != null) {
			if (!sessionEntry.getSubscriptions().contains(topic)) {
				sessionEntry.addSubscription(topic);
			} else {
				isSubscribed = true;
			}
		} else {
			sessions.put(sessionID, new ClientSession(sessionID, new LinkedList<String>(Arrays.asList(topic))));
		}

		if (!isSubscribed) {

			TopicSubscription topicEntry = (TopicSubscription) topics.get(topic);

			if (topicEntry != null) {
				topicEntry.incrementSubscriptions();
			} else {
				topics.put(topic, new TopicSubscription(topic));
			}
		}

		headerAccessor.setSessionId(sessionID);
	}

	@EventListener
	public void onApplicationEvent(SessionDisconnectEvent event) {
		Map<String, String> attributes = (ConcurrentHashMap<String, String>) event.getMessage().getHeaders()
				.get("simpSessionAttributes");

		String sessionID = attributes.get("sessionId");
		if (sessionID != null) {
			ClientSession session = sessions.get(sessionID);
			if (session != null) {
				List<String> subscriptions = session.getSubscriptions();
				for (String subscription : subscriptions) {
					TopicSubscription topic = topics.get(subscription);
					if (topic != null) {
						topic.decrementSubscriptions();
						if (topic.getSubscriptions() <= 0) {
							topics.remove(subscription);
						}
					}
				}
				sessions.remove(sessionID);
			}
		}
	}

	@EventListener
	public void onApplicationEvent(SessionUnsubscribeEvent event) {
		Map<String, String> attributes = (ConcurrentHashMap<String, String>) event.getMessage().getHeaders()
				.get("simpSessionAttributes");
		String sessionID = attributes.get("sessionId");
		String topic = event.getMessage().getHeaders().get("simpSubscriptionId").toString()
				.replaceFirst("/app/unsubscribe/", "");
		ClientSession sessionEntry = (ClientSession) sessions.get(sessionID);

		if (sessionEntry != null) {
			if (sessionEntry.removeSubscription(topic)) {

				TopicSubscription topicEntry = (TopicSubscription) topics.get(topic);

				if (topicEntry != null) {
					topicEntry.decrementSubscriptions();
					if (topicEntry.getSubscriptions() <= 0) {
						topics.remove(topic);
					}
				}
			}
		}
	}
}
