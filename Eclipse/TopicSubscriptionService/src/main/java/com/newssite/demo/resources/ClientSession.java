package com.newssite.demo.resources;

import java.util.LinkedList;
import java.util.List;

public class ClientSession {

	// Attributes
	private String sessionID;
	private List<String> stompSubscriptions;

	// Constructors
	public ClientSession(String sessionID) {
		this.sessionID = sessionID;
		stompSubscriptions = new LinkedList<String>();
	}

	public ClientSession(String sessionID, List<String> stompSubscriptions) {
		this.sessionID = sessionID;
		this.stompSubscriptions = stompSubscriptions;
	}

	// Getters and Setters
	public String getSessionID() {
		return sessionID;
	}

	public void setSessionID(String sessionID) {
		this.sessionID = sessionID;
	}

	public List<String> getStompSubscriptions() {
		return stompSubscriptions;
	}

	public void setStompSubscriptions(List<String> stompSubscriptions) {
		this.stompSubscriptions = stompSubscriptions;
	}

	// Methods
	public boolean removeSubscription(String stompSubscriptions) {
		return this.stompSubscriptions.remove(stompSubscriptions);
	}

	public boolean addSubscription(String stompSubscriptions) {
		return this.stompSubscriptions.add(stompSubscriptions);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((sessionID == null) ? 0 : sessionID.hashCode());
		result = prime * result + ((stompSubscriptions == null) ? 0 : stompSubscriptions.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ClientSession other = (ClientSession) obj;
		if (sessionID == null) {
			if (other.sessionID != null)
				return false;
		} else if (!sessionID.equals(other.sessionID))
			return false;
		if (stompSubscriptions == null) {
			if (other.stompSubscriptions != null)
				return false;
		} else if (!stompSubscriptions.equals(other.stompSubscriptions))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ClientSession [sessionID=" + sessionID + ", stompSubscriptions=" + stompSubscriptions + "]";
	}
}
