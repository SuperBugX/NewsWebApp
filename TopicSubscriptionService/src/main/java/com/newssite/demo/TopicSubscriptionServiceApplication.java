package com.newssite.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;

import com.newssite.demo.resources.NewsTopicFilters;
import com.newssite.demo.resources.NewsTopicFilters.NewsTopicFiltersBuilder;

@SpringBootApplication
@EnableEurekaClient
public class TopicSubscriptionServiceApplication {

	@Bean
	public NewsTopicFiltersBuilder getNewsTopicFilterBuilder() {
		return NewsTopicFilters.builder();
	}

	public static void main(String[] args) {
		SpringApplication.run(TopicSubscriptionServiceApplication.class, args);
	}
}
