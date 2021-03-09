package com.newssite.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaTemplate;

@SpringBootApplication
public class ApacheKafkaServicePrototypeApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApacheKafkaServicePrototypeApplication.class, args);
	}

}
