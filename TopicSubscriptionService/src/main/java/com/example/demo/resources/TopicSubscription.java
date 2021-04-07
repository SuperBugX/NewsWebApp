package com.example.demo.resources;

import lombok.Data;

@Data
public class TopicSubscription {

	// Attributes
	private String kafkaTopic;
	private String stompTopic;
	private int subscriptions;

	// Constructors
	public TopicSubscription(String kafkaTopic, String stompTopic, int subscriptions) {
		super();
		this.kafkaTopic = kafkaTopic;
		this.stompTopic = stompTopic;
		this.subscriptions = subscriptions;
	}
	
	// Constructors
	public TopicSubscription(String kafkaTopic, String stompTopic) {
		super();
		this.kafkaTopic = kafkaTopic;
		this.stompTopic = stompTopic;
		this.subscriptions = 1;
	}

	// Methods
	public void incrementSubscriptions() {
		subscriptions++;
	}

	public void decrementSubscriptions() {
		subscriptions--;
	}
}
