package com.newssite.demo.resources;

import java.util.Timer;

import org.springframework.kafka.core.KafkaTemplate;

import lombok.NonNull;

public class TopicSubscription {

	// Attributes
	@NonNull
	private String topic;
	@NonNull
	private String kafkaTopic;
	@NonNull
	private String stompTopic;
	private int subscriptions;
	private final static int fixedScheduleRate = 1800000;
	private Timer newRetrieverTimer;
	private KafkaTemplate<String, String> kafkaTemplate;

	// User Filter Attributes
	private String country;

	// Constructors
	public TopicSubscription(String kafkaTopic, String stompTopic, int subscriptions,
			KafkaTemplate<String, String> kafkaTemplate) {
		super();
		this.kafkaTopic = kafkaTopic;
		this.stompTopic = stompTopic;
		this.subscriptions = subscriptions;
		this.kafkaTemplate = kafkaTemplate;
		newRetrieverTimer = new Timer();

		if (this.subscriptions > 0) {
			newRetrieverTimer.scheduleAtFixedRate(new RetrieveNewsTask(kafkaTopic, stompTopic, topic, country, kafkaTemplate),
					fixedScheduleRate, fixedScheduleRate);
		}
	}

	public TopicSubscription(String kafkaTopic, String stompTopic, KafkaTemplate<String, String> kafkaTemplate) {
		super();
		this.kafkaTopic = kafkaTopic;
		this.stompTopic = stompTopic;
		this.kafkaTemplate = kafkaTemplate;
		this.subscriptions = 1;
		newRetrieverTimer = new Timer();
		newRetrieverTimer.scheduleAtFixedRate(new RetrieveNewsTask(kafkaTopic, stompTopic, topic, country, kafkaTemplate),
				fixedScheduleRate, fixedScheduleRate);
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
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
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

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	// Methods
	public void incrementSubscriptions() {

		if (subscriptions == 0) {
			newRetrieverTimer.scheduleAtFixedRate(new RetrieveNewsTask(kafkaTopic, stompTopic, topic, country, kafkaTemplate),
					fixedScheduleRate, fixedScheduleRate);
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
		newRetrieverTimer.scheduleAtFixedRate(new RetrieveNewsTask(kafkaTopic, stompTopic, topic, country, kafkaTemplate),
				fixedScheduleRate, fixedScheduleRate);
	}

	public void resetTimer(int delay) {
		newRetrieverTimer.cancel();
		newRetrieverTimer.purge();
		newRetrieverTimer = new Timer();
		newRetrieverTimer.scheduleAtFixedRate(new RetrieveNewsTask(kafkaTopic, stompTopic, topic, country, kafkaTemplate),
				fixedScheduleRate + delay, fixedScheduleRate + delay);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((country == null) ? 0 : country.hashCode());
		result = prime * result + ((kafkaTemplate == null) ? 0 : kafkaTemplate.hashCode());
		result = prime * result + ((kafkaTopic == null) ? 0 : kafkaTopic.hashCode());
		result = prime * result + ((newRetrieverTimer == null) ? 0 : newRetrieverTimer.hashCode());
		result = prime * result + ((stompTopic == null) ? 0 : stompTopic.hashCode());
		result = prime * result + subscriptions;
		result = prime * result + ((topic == null) ? 0 : topic.hashCode());
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
		if (topic == null) {
			if (other.topic != null)
				return false;
		} else if (!topic.equals(other.topic))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "TopicSubscription [topic=" + topic + ", kafkaTopic=" + kafkaTopic + ", stompTopic=" + stompTopic
				+ ", subscriptions=" + subscriptions + ", newRetrieverTimer=" + newRetrieverTimer + ", kafkaTemplate="
				+ kafkaTemplate + ", country=" + country + "]";
	}
}
