package com.example.demo.consumers;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class ArticleConsumer {

	@KafkaListener(topics = { "sports", "business", "entertainment", "health", "science",
			"technology" })
	public void consume(String message) {

		System.out.println("I CONSUMED" + message);

	}
}
