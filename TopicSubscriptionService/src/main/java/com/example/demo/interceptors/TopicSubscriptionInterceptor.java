package com.example.demo.interceptors;

import java.util.HashMap;
import java.util.Map;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;

import com.example.demo.resources.TopicSubscription;

public class TopicSubscriptionInterceptor implements ChannelInterceptor {

	private Map<String, TopicSubscription> topics;

	public TopicSubscriptionInterceptor() {
		topics = new HashMap<String, TopicSubscription>();
	}

	@Override
	public Message<?> preSend(Message<?> message, MessageChannel channel) {
		StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(message);
		String topicID = headerAccessor.getSubscriptionId();
		System.out.println("OKOKOK :" + headerAccessor.toString());

		if (topicID != null) {
			TopicSubscription topic = topics.get(topicID);

			switch (headerAccessor.getCommand()) {

			case SUBSCRIBE:
				if (topic != null) {
					topic.incrementSubscriptions();
				} else {
					topics.put(topicID, new TopicSubscription(headerAccessor.getDestination().replaceFirst("/topic", "")));
				}
				break;

			case UNSUBSCRIBE:
				if (topic != null && topic.getSubscriptions() > 0) {
					topic.decrementSubscriptions();
				}
				break;
			default:
				break;
			}
		}

		 for (Map.Entry<String,TopicSubscription> entry : topics.entrySet())  
	            System.out.println("Key = " + entry.getKey() + 
	                             ", Value = " + entry.getValue().getSubscriptions() + " name = :" + entry.getValue().getName()); 
	    
		return message;
	}
}