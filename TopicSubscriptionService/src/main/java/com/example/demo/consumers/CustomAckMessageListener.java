package com.example.demo.consumers;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.listener.AcknowledgingMessageListener;
import org.springframework.kafka.support.Acknowledgment;

import com.example.demo.interfaces.TopicProcessor;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CustomAckMessageListener implements AcknowledgingMessageListener<String, String> {

	private TopicProcessor topicProcessor;

	@Override
	public void onMessage(ConsumerRecord<String, String> consumerRecord, Acknowledgment acknowledgment) {

		// process message
		topicProcessor.process(consumerRecord.key(), consumerRecord.value());

		// commit offset
		acknowledgment.acknowledge();
	}
}
