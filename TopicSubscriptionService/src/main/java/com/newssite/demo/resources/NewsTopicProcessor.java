package com.newssite.demo.resources;

import org.springframework.messaging.simp.SimpMessagingTemplate;

import com.newssite.demo.interfaces.TopicProcessor;

import lombok.NonNull;

public class NewsTopicProcessor implements TopicProcessor {

	// Attributes
	private String stompTopic;
	private SimpMessagingTemplate simpMessagerTemplate;

	// Constructors
	public NewsTopicProcessor(@NonNull String stompTopic, @NonNull SimpMessagingTemplate simpMessagerTemplate) {
		super();
		this.stompTopic = stompTopic;
		this.simpMessagerTemplate = simpMessagerTemplate;
	}

	// Method
	@Override
	public void process(String key, String message) {
		// Send the received kafka message to the designated topic
		simpMessagerTemplate.convertAndSend(stompTopic, message);
	}
}
