package com.example.demo.controllers;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {

	@KafkaListener(topics="general")
	@MessageMapping("/general")
	@SendTo("/topic/general")
	public void sendGeneralNews(){
		System.out.println("I got something general");
	}

	@KafkaListener(topics="business")
	@MessageMapping("/business")
	@SendTo("/topic/business")
	public void sendBusinessNews(){
		System.out.println("I got somethin BB");

	}

	@KafkaListener(topics="entertainment")
	@MessageMapping("/entertainment")
	@SendTo("/topic/entertainment")
	public void sendEntertainmentNews(){
		System.out.println("I got something  EEE");

	}

	@KafkaListener(topics="health")
	@MessageMapping("/health")
	@SendTo("/topic/health")
	public void sendHealthNews(){
		System.out.println("I got something HH");

	}

	@KafkaListener(topics="science")
	@MessageMapping("/science")
	@SendTo("/topic/science")
	public void sendScienceNews(){
		System.out.println("I got something SCIENCE");

	}

	@KafkaListener(topics="sports")
	@MessageMapping("/sports")
	@SendTo("/topic/sports")
	public void sendSportsNews(){
		System.out.println("I got something SPORTS");

	}

	@KafkaListener(topics="technology")
	@MessageMapping("/technology")
	@SendTo("/topic/technology")
	public void sendTechnologyNews(){
		System.out.println("I got something TECHNO");

	}
}
