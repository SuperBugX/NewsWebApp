package com.newssite.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;

import com.newssite.demo.models.MediaStack;
import com.newssite.demo.models.MediaStack.MediaStackBuilder;
import com.newssite.demo.models.NewsAPI;
import com.newssite.demo.models.NewsAPI.NewsAPIBuilder;

@SpringBootApplication
@EnableEurekaClient
public class NewsFetcherServiceApplication {

	// Class builders (using lombok) that are used to create and configure API calls
	@Bean
	public MediaStackBuilder getMediaStackAPIBuilder() {
		return MediaStack.builder();
	}

	@Bean
	public NewsAPIBuilder getNewsAPIBuilder() {
		return NewsAPI.builder();
	}

	public static void main(String[] args) {
		SpringApplication.run(NewsFetcherServiceApplication.class, args);
	}
}
