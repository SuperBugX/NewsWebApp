package com.newssite.demo.controllers;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.newssite.demo.configurations.KafkaConfig;
import com.newssite.demo.consumers.TopicAckMessageListener;
import com.newssite.demo.exceptions.NewsAPIJSONException;
import com.newssite.demo.exceptions.NewsAPIResponseErrorException;
import com.newssite.demo.interfaces.LatestNewsAPI;
import com.newssite.demo.interfaces.SearcherNewsAPI;
import com.newssite.demo.models.MediaStack.MediaStackBuilder;
import com.newssite.demo.models.NewsAPI.NewsAPIBuilder;
import com.newssite.demo.resources.Article;
import com.newssite.demo.resources.NewsTopicFilters;
import com.newssite.demo.resources.TopicArticlesReaderProcessor;
import com.newssite.util.KafkaConsumerUtil;

@RestController
@RequestMapping("/NewsFetcherService")
public class NewsFetcherController {
	// Attributes
	@Autowired
	private MediaStackBuilder mediaStackBuilder;

	@Autowired
	private NewsAPIBuilder newsAPIBuilder;

	@Autowired
	private KafkaTemplate<String, Article> kafkaTemplate;

	// Methods
	// The supported categories for all request are: general, business,
	// entertainment, health, science, sports technology
	// The supported sorting values are: "popularity", "latest"

	@PostMapping(value="/SearchNews", consumes = {MediaType.APPLICATION_JSON}, produces = {MediaType.APPLICATION_JSON})
	public Article[] searchNews(@RequestParam(value="category", required = true) String category, @RequestBody(required = true) NewsTopicFilters filters) {

		boolean allAPIsHadError = false;
		Article[] articles = null;

		// Hardcode and configure all of the news APIs that can be used for article
		// searching
		SearcherNewsAPI newsAPIs[] = new SearcherNewsAPI[2];
		newsAPIs[0] = mediaStackBuilder.includedCountries(filters.getIncludedCountries())
				.includedCategories(new String[] { category }).includedKeyWords(filters.getIncludedKeyWords()).build();
		newsAPIs[1] = newsAPIBuilder.includedCountries(filters.getIncludedCountries())
				.includedCategories(new String[] { category }).includedKeyWords(filters.getIncludedKeyWords()).build();

		// Loop through each API until one returns a successful result
		for (int i = 0; i < newsAPIs.length; i++) {

			try {
				// Perform API request
				String apiJsonResponse = newsAPIs[i].getSpecificNews();
				articles = newsAPIs[i].convertSpecificNewsToArticles(apiJsonResponse);
				break;

			} catch (NewsAPIResponseErrorException | NewsAPIJSONException e) {
				e.printStackTrace();
				if (i == newsAPIs.length - 1) {
					allAPIsHadError = true;
				}
			}
		}

		if (allAPIsHadError) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An Internal Server Error Occured");
		}

		if (articles != null && articles.length == 0) {
			throw new ResponseStatusException(HttpStatus.NO_CONTENT, "No New Articles Were Found");
		} else {
			return articles;
		}
	}

	@PostMapping(value="/PublishNews", consumes = {MediaType.APPLICATION_JSON})
	public void publishNews(@RequestParam(value="kafkaTopic", required = true) String kafkaTopic, @RequestParam(value="category", required = true) String category,
			@RequestBody(required = true) NewsTopicFilters filters) {

		boolean allAPIsHadError = false;
		Article[] articles = null;
		LatestNewsAPI newsAPIs[] = new LatestNewsAPI[2];
		newsAPIs[0] = newsAPIBuilder.includedCountries(filters.getIncludedCountries())
				.includedCategories(new String[] { category }).build();
		newsAPIs[1] = mediaStackBuilder.includedCountries(filters.getIncludedCountries())
				.includedCategories(new String[] { category }).build();

		// Loop through each API until one returns a successful result
		for (int i = 0; i < newsAPIs.length; i++) {

			try {
				// Perform API request
				String apiJsonResponse = newsAPIs[i].getLatestNews();
				articles = newsAPIs[i].convertLatestNewsToArticles(apiJsonResponse);
				break;

			} catch (NewsAPIResponseErrorException | NewsAPIJSONException e) {
				e.printStackTrace();
				if (i == newsAPIs.length - 1) {
					allAPIsHadError = true;
				}
			}
		}

		if (allAPIsHadError) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An Internal Server Error Occured");
		}

		if (articles != null && articles.length == 0) {
			throw new ResponseStatusException(HttpStatus.NO_CONTENT, "No New Articles Were Found");
		} else {

			// Check if the designated Apache Kafka topic contains similar articles
			if (!haveCommonArticles(articles, getAllArticlesFromKafkaTopic(kafkaTopic))) {
				for (int i = 0; i < articles.length; i++) {
					kafkaTemplate.send(kafkaTopic, articles[i]);
				}
			}
		}
	}

	// Method returns a list of articles from Apache Kafka when provided with a
	// Kafka topic
	// Execution is blocked for at least 3 seconds
	public List<Article> getAllArticlesFromKafkaTopic(String kafkaTopic) {

		TopicArticlesReaderProcessor articleTopicReader = new TopicArticlesReaderProcessor();
		KafkaConsumerUtil.startOrCreateConsumers(kafkaTopic, new TopicAckMessageListener(articleTopicReader, false), 1,
				KafkaConfig.getConsumerProps());

		try {
			TimeUnit.SECONDS.sleep(3);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		KafkaConsumerUtil.removeConsumer(kafkaTopic);
		return articleTopicReader.getTopicArticles();
	}

	// Compare the provided data structures to find if any number shared articles
	// exist
	public boolean haveCommonArticles(Article[] newArticles, List<Article> existingArticles) {
		for (int i = 0; i < newArticles.length; i++) {
			if (existingArticles.contains(newArticles[i])) {
				return true;
			}
		}
		return false;
	}
}
