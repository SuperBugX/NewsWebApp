package com.newssite.demo.consumers;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.Data;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.listener.AcknowledgingMessageListener;
import org.springframework.kafka.support.Acknowledgment;

import com.newssite.demo.interfaces.TopicProcessor;

@AllArgsConstructor
@Data
public class TopicAckMessageListener implements AcknowledgingMessageListener<String, String> {

	@NonNull
	private TopicProcessor topicProcessor;
	@NonNull
	private boolean autoAcknowledge;

	@Override
	public void onMessage(ConsumerRecord<String, String> consumerRecord, Acknowledgment acknowledgment) {

		// process message
		topicProcessor.process(consumerRecord.key(), consumerRecord.value());

		// commit offset
		if (autoAcknowledge) {
			acknowledgment.acknowledge();
		}
	}
}