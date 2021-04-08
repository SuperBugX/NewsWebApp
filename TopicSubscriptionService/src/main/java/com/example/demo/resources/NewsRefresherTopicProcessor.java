package com.example.demo.resources;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import com.example.demo.interfaces.TopicProcessor;

import lombok.NonNull;
import lombok.Data;

@Data
public class NewsRefresherTopicProcessor implements TopicProcessor {

	// Attributes
	@NonNull
	private String stompTopic;
	@NonNull
	private String userName;
	@NonNull
	private SimpMessagingTemplate simpMessagerTemplate;

	// Constructor
	public NewsRefresherTopicProcessor(String stompTopic, String userName,
			SimpMessagingTemplate simpMessagerTemplate) {
		super();
		this.stompTopic = stompTopic;
		this.userName = userName;
		this.simpMessagerTemplate = simpMessagerTemplate;
	}

	// Method
	@Override
	public void process(String key, String message) {

		// Send the received kafka message to the designated topic
		System.out.println("USER GET MESSAGE " + message + "TO USER" + userName);
		simpMessagerTemplate.convertAndSendToUser(userName, stompTopic, message);
	}

}
