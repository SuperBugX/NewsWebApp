package com.example.demo.resources;

import lombok.Data;
import lombok.NonNull;

@Data
public class TopicSubscription {

	// Attributes
	@NonNull
	private String kafkaTopic;
	@NonNull
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
