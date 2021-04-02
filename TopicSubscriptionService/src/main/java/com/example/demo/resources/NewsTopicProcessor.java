package com.example.demo.resources;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import com.example.demo.interfaces.TopicProcessor;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NewsTopicProcessor implements TopicProcessor {

	// Attributes
	private String topic;
	private SimpMessagingTemplate simpMessagerTemplate;

	// Method
	@Override
	public void process(String key, String message) {
		// TODO Auto-generated method stub
		simpMessagerTemplate.convertAndSend("/topic/" + topic, message);
		System.out.println("I sent  : + " + topic);
	}
}
