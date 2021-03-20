package com.example.demo.controllers;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {

	@KafkaListener(topics = "general")
	@MessageMapping("/general")
	@SendTo("/topic/general")
	public void sendGeneralNews(String message) {
		System.out.println("I got something general");
	}

	@KafkaListener(topics = "business")
	@MessageMapping("/business")
	@SendTo("/topic/business")
	public void sendBusinessNews(String message) {
		System.out.println("I got somethin BB");

	}

	@KafkaListener(topics = "entertainment")
	@MessageMapping("/entertainment")
	@SendTo("/topic/entertainment")
	public void sendEntertainmentNews(String message) {
		System.out.println("I got something  EEE");

	}

	@KafkaListener(topics = "health")
	@MessageMapping("/health")
	@SendTo("/topic/health")
	public void sendHealthNews(String message) {
		System.out.println("I got something HH");

	}

	@KafkaListener(topics = "science")
	@MessageMapping("/science")
	@SendTo("/topic/science")
	public void sendScienceNews(String message) {
		System.out.println("I got something SCIENCE");

	}

	@KafkaListener(topics = "sports")
	@MessageMapping("/sports")
	@SendTo("/topic/sports")
	public void sendSportsNews(String message) {
		System.out.println("I got something SPORTS");

	}

	@KafkaListener(topics = "technology")
	@MessageMapping("/technology")
	@SendTo("/topic/technology")
	public void sendTechnologyNews(String message) {
		System.out.println("I got something TECHNO");

	}

	/*
	 * @KafkaListener(topics = "${topics.enabled}", id = "first", autoStartup =
	 * "false")
	 * 
	 * @MessageMapping("/generic")
	 * 
	 * @SendTo("/topic/generic") public void genericLivesNews() {
	 * 
	 * }
	 */
}
