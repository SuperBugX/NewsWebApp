package com.example.demo.consumers;

import lombok.AllArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.listener.MessageListener;

import com.example.demo.interfaces.TopicProcessor;

@AllArgsConstructor
public class CustomMessageListener implements MessageListener<String, String> {

  private TopicProcessor topicProcessor;

  @Override
  public void onMessage(ConsumerRecord<String, String> consumerRecord) {

	  topicProcessor.process(consumerRecord.key(), consumerRecord.value());
  }
}