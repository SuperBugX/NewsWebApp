package com.example.demo.controllers;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {

	@MessageMapping("/general")
	@SendTo("/topic/general")
	public void sendGeneralNews(@Payload String message){
		System.out.println(message);
	}

	@MessageMapping("/business")
	@SendTo("/topic/business")
	public void sendBusinessNews(@Payload String message){
		System.out.println(message);
	}

	@MessageMapping("/entertainment")
	@SendTo("/topic/entertainment")
	public void sendEntertainmentNews(@Payload String message){
		System.out.println(message);
	}

	@MessageMapping("/health")
	@SendTo("/topic/health")
	public void sendHealthNews(@Payload String message){
		System.out.println(message);
	}

	@MessageMapping("/science")
	@SendTo("/topic/science")
	public void sendScienceNews(@Payload String message){
		System.out.println(message);
	}

	@MessageMapping("/sports")
	@SendTo("/topic/sports")
	public void sendSportsNews(@Payload String message){
		System.out.println(message);
	}

	@MessageMapping("/technology")
	@SendTo("/topic/technology")
	public void sendTechnologyNews(@Payload String message){
		System.out.println(message);
	}
}
