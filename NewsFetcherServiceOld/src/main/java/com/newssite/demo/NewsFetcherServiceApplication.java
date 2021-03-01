package com.newssite.demo;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

import com.newssite.demo.models.MediaStackAPI;
import com.newssite.demo.models.MediaStackAPI.MediaStackAPIBuilder;

@SpringBootApplication
public class NewsFetcherServiceApplication {
	
	@Bean
	public MediaStackAPIBuilder getMediaStackAPIBuilder() {
		return MediaStackAPI.builder();
	}
		
	public static void main(String[] args) {
		SpringApplication.run(NewsFetcherServiceApplication.class, args);
	}

}
