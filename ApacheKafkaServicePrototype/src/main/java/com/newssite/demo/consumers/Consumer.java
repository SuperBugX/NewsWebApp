package com.newssite.demo.consumers;


import org.slf4j.Logger;


import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.newssite.demo.resources.Article;

@Service
public class Consumer {

    @KafkaListener(topics = {"sports", "business", "entertainment", "health", "science", "technology"}, groupId = "group_id")
    public void consume(String message) throws JsonMappingException, JsonProcessingException {
    	
    	ObjectMapper jsonMapper = new ObjectMapper();
    	Article temp = jsonMapper.readValue(message, Article.class);
    	System.out.println("I CONSUMED" + temp.toString());
    	
		
		  	
    }
}
