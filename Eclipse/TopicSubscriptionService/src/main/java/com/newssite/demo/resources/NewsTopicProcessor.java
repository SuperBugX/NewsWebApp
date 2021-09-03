package com.newssite.demo.resources;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import com.newssite.demo.interfaces.TopicProcessor;

import lombok.NonNull;

public class NewsTopicProcessor implements TopicProcessor {

	// Attributes
	private String stompTopic;
	private SimpMessagingTemplate simpMessengerTemplate;

	// Constructors
	public NewsTopicProcessor(@NonNull String stompTopic, @NonNull SimpMessagingTemplate simpMessengerTemplate) {
		super();
		this.stompTopic = stompTopic;
		this.simpMessengerTemplate = simpMessengerTemplate;
	}
	
	//Getters and Setters
	public String getStompTopic() {
		return stompTopic;
	}

	public void setStompTopic(String stompTopic) {
		this.stompTopic = stompTopic;
	}

	public SimpMessagingTemplate getSimpMessengerTemplate() {
		return simpMessengerTemplate;
	}

	public void setSimpMessengerTemplate(SimpMessagingTemplate simpMessengerTemplate) {
		this.simpMessengerTemplate = simpMessengerTemplate;
	}

	// Method
	@Override
	public void process(String key, Object message) {
		// Send the received kafka message to the designated topic
		if (message instanceof String) {
			simpMessengerTemplate.convertAndSend(stompTopic, message);
		}
	}
}
