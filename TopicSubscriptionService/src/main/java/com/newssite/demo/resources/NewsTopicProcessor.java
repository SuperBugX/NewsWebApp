package com.newssite.demo.resources;

import org.springframework.messaging.simp.SimpMessagingTemplate;

import com.newssite.demo.interfaces.TopicProcessor;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

@Data
@AllArgsConstructor
public class NewsTopicProcessor implements TopicProcessor {

	// Attributes
	@NonNull
	private String stompTopic;
	@NonNull
	private SimpMessagingTemplate simpMessagerTemplate;

	// Method
	@Override
	public void process(String key, String message) {

		// Send the received kafka message to the designated topic
		System.out.println("All Users Subscribed to The Topic: " + stompTopic + ", Received News");
		simpMessagerTemplate.convertAndSend(stompTopic, message);
	}
}
