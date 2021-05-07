package com.newssite.demo.controllers;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.newssite.demo.configurations.KafkaConfig;
import com.newssite.demo.consumers.TopicAckMessageListener;
import com.newssite.demo.exceptions.NewsAPIJSONException;
import com.newssite.demo.exceptions.NewsAPIResponseErrorException;
import com.newssite.demo.interfaces.LatestNewsAPI;
import com.newssite.demo.models.MediaStack.MediaStackBuilder;
import com.newssite.demo.models.NewsAPI.NewsAPIBuilder;
import com.newssite.demo.resources.Article;
import com.newssite.demo.resources.ReadTopicArticlesProcessor;
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
	@GetMapping("/PublishNews")
	public void publishNews(@RequestParam("kafkaTopic") String kafkaTopic, @RequestParam("category") String category,
			@RequestParam("country") String country) {

		boolean allAPIsHadError = false;
		Article[] articles = null;
		LatestNewsAPI newsAPIs[] = new LatestNewsAPI[2];
		newsAPIs[0] = newsAPIBuilder.country(country).category(category).build();
		newsAPIs[1] = mediaStackBuilder.country(country).category(category).build();

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
				continue;
			}
		}

		if (allAPIsHadError) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An Internal Server Error Occured");
		}

		if (articles != null && articles.length == 0) {
			throw new ResponseStatusException(HttpStatus.NO_CONTENT, "No New Articles Were Found");
		} else {

			if (!haveCommonArticles(articles, getAllArticlesFromKafkaTopic(kafkaTopic))) {
				pushAllArticlesToKafka(kafkaTopic, articles);
			}
		}
	}

	public void pushAllArticlesToKafka(String kafkaTopic, Article[] articles) {
		for (int i = 0; i < articles.length; i++) {
			kafkaTemplate.send(kafkaTopic, articles[i]);
		}
	}

	public List<Article> getAllArticlesFromKafkaTopic(String kafkaTopic) {

		ReadTopicArticlesProcessor articleTopicReader = new ReadTopicArticlesProcessor();
		KafkaConsumerUtil.startOrCreateConsumers(kafkaTopic, new TopicAckMessageListener(articleTopicReader, false), 1,
				KafkaConfig.getConsumerConfig());

		try {
			TimeUnit.SECONDS.sleep(3);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		KafkaConsumerUtil.removeConsumer(kafkaTopic);
		return articleTopicReader.getTopicArticles();
	}

	public boolean haveCommonArticles(Article[] newArticles, List<Article> existingArticles) {
		System.out.println("EXISTING IS" + existingArticles.toString());
		for (int i = 0; i < newArticles.length; i++) {
			if (existingArticles.contains(newArticles[i])) {
				System.out.println("I GOT TRUE");
				return true;
			}
		}
		System.out.println("I GOT FALSE");
		return false;
	}
}
