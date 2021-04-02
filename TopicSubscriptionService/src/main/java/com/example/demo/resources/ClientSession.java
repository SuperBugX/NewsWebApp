package com.example.demo.resources;

import java.util.LinkedList;
import java.util.List;
import lombok.Data;

@Data
public class ClientSession {

	// Attributes
	private String sessionID;
	private List<String> subscriptions;

	// Constructors
	public ClientSession(String sessionID) {
		this.sessionID = sessionID;
		subscriptions = new LinkedList<String>();
	}

	public ClientSession(String sessionID, LinkedList<String> subscriptions) {
		this.sessionID = sessionID;
		this.subscriptions = subscriptions;
	}

	// Methods
	public boolean addSubscription(String subscription) {
		return subscriptions.add(subscription);
	}

	public boolean removeSubscription(String subscription) {
		return subscriptions.remove(subscription);
	}
}
