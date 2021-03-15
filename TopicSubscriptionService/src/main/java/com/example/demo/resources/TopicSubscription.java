package com.example.demo.resources;

public class TopicSubscription {

	private String name;
	private int subscriptions;

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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getSubscriptions() {
		return subscriptions;
	}

	public void setSubscriptions(int subscriptions) {
		this.subscriptions = subscriptions;
	}
	
	public void incrementSubscriptions() {
		subscriptions++;
	}
	
	public void decrementSubscriptions() {
		subscriptions--;
	}
}
