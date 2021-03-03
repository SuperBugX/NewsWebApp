package com.newssite.demo.controllers;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/TopicSubscriptionService")
public class TopicSubscriptionController {

	@MessageMapping("/subscribe")
	public void subscribeTopic(@Payload String message) {
		System.out.println("YES I GOT SOMETHING HERE");
	}

	@MessageMapping("/unsubscribe")
	public void unsubscribeTopic(@Payload String message) {

	}
}
