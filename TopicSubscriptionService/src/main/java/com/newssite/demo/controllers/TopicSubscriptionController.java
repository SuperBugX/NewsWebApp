package com.newssite.demo.controllers;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

@Controller
@CrossOrigin
public class TopicSubscriptionController {

	@MessageMapping("/message/sub")
	@SendTo("/topic/reply")
	public void subscribeTopic(@Payload String message) {
		System.out.println("YES I GOT SOMETHING HERE");
	}
}
