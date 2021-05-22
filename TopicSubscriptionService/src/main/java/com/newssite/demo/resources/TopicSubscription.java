package com.newssite.demo.resources;

import java.util.Timer;

import org.springframework.kafka.core.KafkaTemplate;

import lombok.NonNull;

public class TopicSubscription {

	// Attributes
	private final static int fixedScheduleRate = 1800000;
	private String newsTopic;
	private String kafkaTopic;
	private String stompTopic;
	private String country;
	private int subscriptions;
	private Timer newRetrieverTimer;
	private KafkaTemplate<String, String> kafkaTemplate;
	private NewsTopicFilters filters;

	// Constructors
	public TopicSubscription(@NonNull String newsTopic, @NonNull String kafkaTopic, @NonNull String stompTopic,
			int subscriptions, KafkaTemplate<String, String> kafkaTemplate, NewsTopicFilters filters) {
		super();
		this.newsTopic = newsTopic;
		this.kafkaTopic = kafkaTopic;
		this.stompTopic = stompTopic;
		this.subscriptions = subscriptions;
		this.kafkaTemplate = kafkaTemplate;
		this.filters = filters;
		newRetrieverTimer = new Timer();

		if (this.subscriptions > 0) {
			newRetrieverTimer.scheduleAtFixedRate(
					new RetrieveNewsTask(kafkaTopic, stompTopic, newsTopic, filters, kafkaTemplate), fixedScheduleRate,
					fixedScheduleRate);
		}
	}

	public TopicSubscription(@NonNull String newsTopic, @NonNull String kafkaTopic, @NonNull String stompTopic,
			KafkaTemplate<String, String> kafkaTemplate, NewsTopicFilters filters) {
		super();
		this.newsTopic = newsTopic;
		this.kafkaTopic = kafkaTopic;
		this.stompTopic = stompTopic;
		this.kafkaTemplate = kafkaTemplate;
		this.subscriptions = 1;
		this.filters = filters;
		newRetrieverTimer = new Timer();
		newRetrieverTimer.scheduleAtFixedRate(
				new RetrieveNewsTask(kafkaTopic, stompTopic, newsTopic, filters, kafkaTemplate), fixedScheduleRate,
				fixedScheduleRate);
	}

	// Getters and Setters
	public String getKafkaTopic() {
		return kafkaTopic;
	}

	public void setKafkaTopic(String kafkaTopic) {
		this.kafkaTopic = kafkaTopic;
	}

	public String getStompTopic() {
		return stompTopic;
	}

	public void setStompTopic(String stompTopic) {
		this.stompTopic = stompTopic;
	}

	public int getSubscriptions() {
		return subscriptions;
	}

	public void setSubscriptions(int subscriptions) {
		this.subscriptions = subscriptions;
	}

	public String getTopic() {
		return newsTopic;
	}

	public void setTopic(String topic) {
		this.newsTopic = topic;
	}

	public static int getFixedScheduleRate() {
		return fixedScheduleRate;
	}

	public Timer getNewRetrieverTimer() {
		return newRetrieverTimer;
	}

	public void setNewRetrieverTimer(Timer newRetrieverTimer) {
		this.newRetrieverTimer = newRetrieverTimer;
	}

	public KafkaTemplate<String, String> getKafkaTemplate() {
		return kafkaTemplate;
	}

	public void setKafkaTemplate(KafkaTemplate<String, String> kafkaTemplate) {
		this.kafkaTemplate = kafkaTemplate;
	}

	public NewsTopicFilters getFilters() {
		return filters;
	}

	public void setFilters(NewsTopicFilters filters) {
		this.filters = filters;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	// Methods
	public void incrementSubscriptions() {

		if (subscriptions == 0) {
			newRetrieverTimer.scheduleAtFixedRate(
					new RetrieveNewsTask(kafkaTopic, stompTopic, newsTopic, filters, kafkaTemplate), fixedScheduleRate,
					fixedScheduleRate);
		}

		subscriptions++;
	}

	public void decrementSubscriptions() {
		subscriptions--;

		if (subscriptions == 0) {
			newRetrieverTimer.cancel();
			newRetrieverTimer.purge();
			newRetrieverTimer = new Timer();
		}
	}

	public void resetTimer() {
		newRetrieverTimer.cancel();
		newRetrieverTimer.purge();
		newRetrieverTimer = new Timer();
		newRetrieverTimer.scheduleAtFixedRate(
				new RetrieveNewsTask(kafkaTopic, stompTopic, newsTopic, filters, kafkaTemplate), fixedScheduleRate,
				fixedScheduleRate);
	}

	public void resetTimer(int delay) {
		newRetrieverTimer.cancel();
		newRetrieverTimer.purge();
		newRetrieverTimer = new Timer();
		newRetrieverTimer.scheduleAtFixedRate(
				new RetrieveNewsTask(kafkaTopic, stompTopic, newsTopic, filters, kafkaTemplate),
				fixedScheduleRate + delay, fixedScheduleRate + delay);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((country == null) ? 0 : country.hashCode());
		result = prime * result + ((filters == null) ? 0 : filters.hashCode());
		result = prime * result + ((kafkaTemplate == null) ? 0 : kafkaTemplate.hashCode());
		result = prime * result + ((kafkaTopic == null) ? 0 : kafkaTopic.hashCode());
		result = prime * result + ((newRetrieverTimer == null) ? 0 : newRetrieverTimer.hashCode());
		result = prime * result + ((stompTopic == null) ? 0 : stompTopic.hashCode());
		result = prime * result + subscriptions;
		result = prime * result + ((newsTopic == null) ? 0 : newsTopic.hashCode());
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
		TopicSubscription other = (TopicSubscription) obj;
		if (country == null) {
			if (other.country != null)
				return false;
		} else if (!country.equals(other.country))
			return false;
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
		if (newRetrieverTimer == null) {
			if (other.newRetrieverTimer != null)
				return false;
		} else if (!newRetrieverTimer.equals(other.newRetrieverTimer))
			return false;
		if (stompTopic == null) {
			if (other.stompTopic != null)
				return false;
		} else if (!stompTopic.equals(other.stompTopic))
			return false;
		if (subscriptions != other.subscriptions)
			return false;
		if (newsTopic == null) {
			if (other.newsTopic != null)
				return false;
		} else if (!newsTopic.equals(other.newsTopic))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "TopicSubscription [topic=" + newsTopic + ", kafkaTopic=" + kafkaTopic + ", stompTopic=" + stompTopic
				+ ", subscriptions=" + subscriptions + ", newRetrieverTimer=" + newRetrieverTimer + ", kafkaTemplate="
				+ kafkaTemplate + ", filters=" + filters + ", country=" + country + "]";
	}
}
