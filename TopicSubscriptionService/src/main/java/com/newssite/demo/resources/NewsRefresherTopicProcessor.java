package com.newssite.demo.resources;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import com.newssite.demo.interfaces.TopicProcessor;

import lombok.NonNull;

public class NewsRefresherTopicProcessor implements TopicProcessor {

	// Attributes
	private String stompTopic;
	private String userName;
	private SimpMessagingTemplate simpMessagerTemplate;

	// Constructor
	public NewsRefresherTopicProcessor(@NonNull String stompTopic, @NonNull String userName,
			@NonNull SimpMessagingTemplate simpMessagerTemplate) {
		super();
		this.stompTopic = stompTopic;
		this.userName = userName;
		this.simpMessagerTemplate = simpMessagerTemplate;
	}

	// Method
	@Override
	public void process(String key, String message) {
		// Send the received kafka message to the designated topic
		simpMessagerTemplate.convertAndSendToUser(userName, stompTopic, message);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((simpMessagerTemplate == null) ? 0 : simpMessagerTemplate.hashCode());
		result = prime * result + ((stompTopic == null) ? 0 : stompTopic.hashCode());
		result = prime * result + ((userName == null) ? 0 : userName.hashCode());
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
		NewsRefresherTopicProcessor other = (NewsRefresherTopicProcessor) obj;
		if (simpMessagerTemplate == null) {
			if (other.simpMessagerTemplate != null)
				return false;
		} else if (!simpMessagerTemplate.equals(other.simpMessagerTemplate))
			return false;
		if (stompTopic == null) {
			if (other.stompTopic != null)
				return false;
		} else if (!stompTopic.equals(other.stompTopic))
			return false;
		if (userName == null) {
			if (other.userName != null)
				return false;
		} else if (!userName.equals(other.userName))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "NewsRefresherTopicProcessor [stompTopic=" + stompTopic + ", userName=" + userName
				+ ", simpMessagerTemplate=" + simpMessagerTemplate + "]";
	}
}
