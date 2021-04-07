package com.newssite.demo.controllers;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.newssite.demo.configurations.KafkaConfig;
import com.newssite.demo.exceptions.NewsAPIJSONException;
import com.newssite.demo.exceptions.NewsAPIResponseErrorException;
import com.newssite.demo.models.MediaStack;
import com.newssite.demo.models.MediaStack.MediaStackBuilder;
import com.newssite.demo.models.NewsAPI;
import com.newssite.demo.models.NewsAPI.NewsAPIBuilder;
import com.newssite.demo.resources.Article;
import com.newssite.demo.resources.ErrorTemplate;

@RestController
@RequestMapping("/NewsFetcherService")
public class NewsFetcherController {

	// Attributes
	@Autowired
	private MediaStackBuilder mediaStackBuilder;

	@Autowired
	private NewsAPIBuilder newsAPIBuilder;

	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;
	
	private List<String> kafkaTopics = new LinkedList<String>(); 

	// Methods

	@RequestMapping("/PublishNews")
	public String publishNews(@RequestParam("kafkaTopic") String kafkaTopic, @RequestParam("category") String category,
			@RequestParam("country") String country, @RequestParam("language") String language) {

		ErrorTemplate errorTemplate;

		// Build the API request parameters
		MediaStack api = mediaStackBuilder.country(country).category(category).language(language).build();

		try {
			// Perform API request
			String apiJsonResponse = api.getLatestNews();
			Article[] articles = api.convertLatestNewsToArticles(apiJsonResponse);

			for (int i = 0; i < articles.length; i++) {
				try {
					kafkaTemplate.send(kafkaTopic, articles[i].toJSON());
				} catch (JsonProcessingException e) {
					e.getStackTrace();
				}
			}

			return "";

		} catch (NewsAPIResponseErrorException e) {
			// TODO Auto-generated catch block
			errorTemplate = new ErrorTemplate("api-error-responses", "All APIs produced errors in their responses");
			return errorTemplate.toJSON();

		} catch (NewsAPIJSONException e) {
			// TODO Auto-generated catch block
			errorTemplate = new ErrorTemplate("json-error", "Error processing JSON data");
			return errorTemplate.toJSON();
		}
	}

	@RequestMapping("/PublishNews2")
	public String publishNews2(@RequestParam("kafkaTopic") String kafkaTopic, @RequestParam("category") String category,
			@RequestParam("country") String country, @RequestParam("language") String language) {

		ErrorTemplate errorTemplate;

		// Build the API request parameters
		NewsAPI api = newsAPIBuilder.country(country).category(category).language(language).build();

		try {
			// Perform API request
			String apiJsonResponse = api.getLatestNews();
			Article[] articles = api.convertLatestNewsToArticles(apiJsonResponse);

			for (int i = 0; i < articles.length; i++) {
				try {
					
					System.out.println("TOPIC IS" + kafkaTopic + "I SENT " + articles[i].toString());
;					kafkaTemplate.send(kafkaTopic, articles[i].toJSON());
				} catch (JsonProcessingException e) {
					e.getStackTrace();
				}
			}

			return "";

		} catch (NewsAPIResponseErrorException e) {
			// TODO Auto-generated catch block
			errorTemplate = new ErrorTemplate("api-error-responses", "All APIs produced errors in their responses");
			return errorTemplate.toJSON();

		} catch (NewsAPIJSONException e) {
			// TODO Auto-generated catch block
			errorTemplate = new ErrorTemplate("json-error", "Error processing JSON data");
			return errorTemplate.toJSON();
		}
	}
}
