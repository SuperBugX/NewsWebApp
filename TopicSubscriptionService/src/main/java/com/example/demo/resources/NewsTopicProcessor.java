package com.example.demo.resources;

import org.springframework.messaging.simp.SimpMessagingTemplate;

import com.example.demo.interfaces.TopicProcessor;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NewsTopicProcessor implements TopicProcessor {

	//Attributes
	private String topic;
	private SimpMessagingTemplate simpMessagerTemplate;

	//Methods
	@Override
	public void process(String key, String message) {
		// TODO Auto-generated method stub
		System.out.println("I GOT SOME NEWS" + message);
		simpMessagerTemplate.convertAndSend("/topic/" + topic , message);
	}
}
