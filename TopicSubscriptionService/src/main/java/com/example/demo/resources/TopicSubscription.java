package com.example.demo.resources;

import lombok.Data;

@Data
public class TopicSubscription {

	//Attributes 
	private String name;
	private int subscriptions;

	//Constructors
	public TopicSubscription(String name, int subscriptions) {
		super();
		this.name = name;
		this.subscriptions = subscriptions;
	}
	
	public TopicSubscription(String name) {
		super();
		this.name = name;
		this.subscriptions = 1;
	}

	//Methods
	public void incrementSubscriptions() {
		subscriptions++;
	}
	
	public void decrementSubscriptions() {
		subscriptions--;
	}
}
