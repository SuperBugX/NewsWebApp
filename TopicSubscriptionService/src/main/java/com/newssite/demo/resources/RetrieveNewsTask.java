package com.newssite.demo.resources;

import java.util.TimerTask;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.beans.factory.annotation.Configurable;

@Configurable
public class RetrieveNewsTask extends TimerTask {

	// Attributes
	private static final String kafkaNewRequestTopic = "NewsRequests";
	private KafkaTemplate<String, String> kafkaTemplate;
	private String kafkaTopic;
	private String stompTopic;
	private String newsTopic;
	private NewsTopicFilters filters;

	// Constructors
	public RetrieveNewsTask(String kafkaTopic, String stompTopic, String newsTopic, NewsTopicFilters filters,
			KafkaTemplate<String, String> kafkaTemplate) {
		super();
		this.kafkaTemplate = kafkaTemplate;
		this.kafkaTopic = kafkaTopic;
		this.stompTopic = stompTopic;
		this.newsTopic = newsTopic;
		this.filters = filters;
	}

	// Getters and Setters

	public String getNewsTopic() {
		return newsTopic;
	}

	public void setNewsTopic(String newsTopic) {
		this.newsTopic = newsTopic;
	}

	public String getKafkaTopic() {
		return kafkaTopic;
	}

	public void setKafkaTopic(String kafkaTopic) {
		this.kafkaTopic = kafkaTopic;
	}

	public static String getKafkanewrequesttopic() {
		return kafkaNewRequestTopic;
	}

	public NewsTopicFilters getFilters() {
		return filters;
	}

	public void setFilters(NewsTopicFilters filters) {
		this.filters = filters;
	}

	public String getStompTopic() {
		return stompTopic;
	}

	public void setStompTopic(String stompTopic) {
		this.stompTopic = stompTopic;
	}

	public static String getKafkaNewRequestTopic() {
		return kafkaNewRequestTopic;
	}

	public KafkaTemplate<String, String> getKafkaTemplate() {
		return kafkaTemplate;
	}

	public void setKafkaTemplate(KafkaTemplate<String, String> kafkaTemplate) {
		this.kafkaTemplate = kafkaTemplate;
	}

	// Methods
	@Override
	public void run() {
		kafkaTemplate.send(kafkaNewRequestTopic, stompTopic);
		NewsRetriever.requestNewsProducer(kafkaTopic, kafkaTopic, filters);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((filters == null) ? 0 : filters.hashCode());
		result = prime * result + ((kafkaTemplate == null) ? 0 : kafkaTemplate.hashCode());
		result = prime * result + ((kafkaTopic == null) ? 0 : kafkaTopic.hashCode());
		result = prime * result + ((newsTopic == null) ? 0 : newsTopic.hashCode());
		result = prime * result + ((stompTopic == null) ? 0 : stompTopic.hashCode());
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
		RetrieveNewsTask other = (RetrieveNewsTask) obj;
		if (filters == null) {
			if (other.filters != null)
				return false;
		} else if (!filters.equals(other.filters))
			return false;
		if (kafkaTemplate == null) {
			if (other.kafkaTemplate != null)
				return false;
		} else if (!kafkaTemplate.equals(other.kafkaTemplate))
			return false;
		if (kafkaTopic == null) {
			if (other.kafkaTopic != null)
				return false;
		} else if (!kafkaTopic.equals(other.kafkaTopic))
			return false;
		if (newsTopic == null) {
			if (other.newsTopic != null)
				return false;
		} else if (!newsTopic.equals(other.newsTopic))
			return false;
		if (stompTopic == null) {
			if (other.stompTopic != null)
				return false;
		} else if (!stompTopic.equals(other.stompTopic))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "RetrieveNewsTask [kafkaTemplate=" + kafkaTemplate + ", kafkaTopic=" + kafkaTopic + ", stompTopic="
				+ stompTopic + ", newsTopic=" + newsTopic + ", filters=" + filters + "]";
	}
}
