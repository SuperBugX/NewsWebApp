package com.newssite.demo.resources;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import com.newssite.demo.interfaces.TopicProcessor;

import lombok.NonNull;

public class NewsRefresherTopicProcessor implements TopicProcessor {

	// Attributes
	private String stompTopic;
	private String userName;
	private SimpMessagingTemplate simpMessengerTemplate;

	// Constructor
	public NewsRefresherTopicProcessor(@NonNull String stompTopic, @NonNull String userName, @NonNull SimpMessagingTemplate simpMessengerTemplate) {
		super();
		this.stompTopic = stompTopic;
		this.userName = userName;
		this.simpMessengerTemplate = simpMessengerTemplate;
	}
	
	//Getters and Setters
	public String getStompTopic() {
		return stompTopic;
	}

	public void setStompTopic(String stompTopic) {
		this.stompTopic = stompTopic;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
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
		// Send the received kafka message to a specific user
		if (message instanceof String) {
			simpMessengerTemplate.convertAndSendToUser(userName, stompTopic, message);
		}
	}
}
