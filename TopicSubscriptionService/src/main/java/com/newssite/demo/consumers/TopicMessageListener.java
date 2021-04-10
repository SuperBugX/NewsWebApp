package com.newssite.demo.consumers;

import lombok.AllArgsConstructor;
import lombok.NonNull;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.listener.MessageListener;

import com.newssite.demo.interfaces.TopicProcessor;

@AllArgsConstructor
public class TopicMessageListener implements MessageListener<String, String> {

	@NonNull
	private TopicProcessor topicProcessor;

	@Override
	public void onMessage(ConsumerRecord<String, String> consumerRecord) {

		topicProcessor.process(consumerRecord.key(), consumerRecord.value());
	}

}
