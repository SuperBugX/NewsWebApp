package com.newssite.demo.consumers;

import lombok.NonNull;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.listener.MessageListener;

import com.newssite.demo.interfaces.TopicProcessor;

public class TopicMessageListener implements MessageListener<String, String> {

	private TopicProcessor topicProcessor;

	// Constructors
	public TopicMessageListener(@NonNull TopicProcessor topicProcessor) {
		super();
		this.topicProcessor = topicProcessor;
	}

	// Getters and Setters
	public TopicProcessor getTopicProcessor() {
		return topicProcessor;
	}

	public void setTopicProcessor(TopicProcessor topicProcessor) {
		this.topicProcessor = topicProcessor;
	}

	// Methods
	@Override
	public void onMessage(ConsumerRecord<String, String> consumerRecord) {

		topicProcessor.process(consumerRecord.key(), consumerRecord.value());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
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
		TopicMessageListener other = (TopicMessageListener) obj;
		if (topicProcessor == null) {
			if (other.topicProcessor != null)
				return false;
		} else if (!topicProcessor.equals(other.topicProcessor))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "TopicMessageListener [topicProcessor=" + topicProcessor + "]";
	}
}
