package com.newssite.demo;

import org.springframework.boot.SpringApplication;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;

import com.newssite.demo.models.MediaStack;
import com.newssite.demo.models.MediaStack.MediaStackBuilder;

@SpringBootApplication
@EnableEurekaClient
public class NewsFetcherServiceApplication {

	@Bean
	public MediaStackBuilder getMediaStackAPIBuilder() {
		return MediaStack.builder();
	}

	public static void main(String[] args) {
		SpringApplication.run(NewsFetcherServiceApplication.class, args);
	}

}
