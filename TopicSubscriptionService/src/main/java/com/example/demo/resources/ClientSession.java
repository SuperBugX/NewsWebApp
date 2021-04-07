package com.example.demo.resources;

import java.util.LinkedList;
import java.util.List;
import lombok.Data;

@Data
public class ClientSession {

	// Attributes
	private String sessionID;
	private List<String> stompSubscriptions;

	// Constructors
	public ClientSession(String sessionID) {
		this.sessionID = sessionID;
		stompSubscriptions = new LinkedList<String>();
	}

	public ClientSession(String sessionID, LinkedList<String> stompSubscriptions) {
		this.sessionID = sessionID;
		this.stompSubscriptions = stompSubscriptions;
	}

	// Methods
	public boolean addSubscription(String stompSubscriptions) {
		return this.stompSubscriptions.add(stompSubscriptions);
	}

	public boolean removeSubscription(String stompSubscriptions) {
		return this.stompSubscriptions.remove(stompSubscriptions);
	}
}
