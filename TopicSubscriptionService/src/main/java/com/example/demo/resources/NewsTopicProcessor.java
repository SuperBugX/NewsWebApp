package com.example.demo.resources;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import com.example.demo.interfaces.TopicProcessor;

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
		System.out.println("i consumed " + message);
		simpMessagerTemplate.convertAndSend(stompTopic, message);
	}
}
