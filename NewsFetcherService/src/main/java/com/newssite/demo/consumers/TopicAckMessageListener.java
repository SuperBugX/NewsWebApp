package com.newssite.demo.consumers;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.listener.AcknowledgingMessageListener;
import org.springframework.kafka.support.Acknowledgment;

import com.newssite.demo.interfaces.TopicProcessor;

import lombok.NonNull;

public class TopicAckMessageListener implements AcknowledgingMessageListener<String, String> {

	// Attributes
	private TopicProcessor topicProcessor;
	private boolean autoAcknowledge;

	// Constructors
	public TopicAckMessageListener(@NonNull TopicProcessor topicProcessor, boolean autoAcknowledge) {
		super();
		this.topicProcessor = topicProcessor;
		this.autoAcknowledge = autoAcknowledge;
	}

	// Getters and Setters
	public TopicProcessor getTopicProcessor() {
		return topicProcessor;
	}

	public void setTopicProcessor(TopicProcessor topicProcessor) {
		this.topicProcessor = topicProcessor;
	}

	public boolean isAutoAcknowledge() {
		return autoAcknowledge;
	}

	public void setAutoAcknowledge(boolean autoAcknowledge) {
		this.autoAcknowledge = autoAcknowledge;
	}

	// Methods
	@Override
	public void onMessage(ConsumerRecord<String, String> consumerRecord, Acknowledgment acknowledgment) {

		// process message
		topicProcessor.process(consumerRecord.key(), consumerRecord.value());

		// commit offset
		if (autoAcknowledge) {
			acknowledgment.acknowledge();
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (autoAcknowledge ? 1231 : 1237);
		result = prime * result + ((topicProcessor == null) ? 0 : topicProcessor.hashCode());
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
		TopicAckMessageListener other = (TopicAckMessageListener) obj;
		if (autoAcknowledge != other.autoAcknowledge)
			return false;
		if (topicProcessor == null) {
			if (other.topicProcessor != null)
				return false;
		} else if (!topicProcessor.equals(other.topicProcessor))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "TopicAckMessageListener [topicProcessor=" + topicProcessor + ", autoAcknowledge=" + autoAcknowledge
				+ "]";
	}
}